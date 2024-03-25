package org.keycloak.pushNotification.interfaces;

import com.google.auth.oauth2.AccessToken;
import com.google.auth.oauth2.GoogleCredentials;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.ws.rs.core.Response;
import org.keycloak.authentication.*;
import org.keycloak.credential.CredentialProvider;
import org.keycloak.models.UserModel;
import org.keycloak.pushNotification.model.PNCredentialModel;

import java.io.*;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Random;

public class PNRequiredAction implements RequiredActionProvider, CredentialRegistrator {

    public static final String PROVIDER_ID = "push_notification_config";

    @Override
    public void evaluateTriggers(RequiredActionContext context) {

    }

    @Override
    public void requiredActionChallenge(RequiredActionContext context) {
        UserModel user = context.getUser();
        List<String> tokensFCM = user.getAttributes().get("tokenFCM");
        String tokenFCM = tokensFCM.isEmpty() ? null : tokensFCM.get(0);
        String code = generateCode();

        if (tokensFCM != null) {
            try {
                HttpClient client = HttpClient.newHttpClient();
                HttpRequest request = HttpRequest.newBuilder()
                        //.uri(new URI("https://fcm.googleapis.com/v1/projects/fcmpushnotificationkeycloak/messages:send"))
                        //.header("Content-Type", "application/json; UTF-8")
                        //.header("Authorization", "Bearer " + getAccessToken())
                        .uri(new URI("https://fcm.googleapis.com/fcm/send"))
                        .header("Content-Type", "application/json")
                        .header("Authorization", "Bearer " + "AAAAodfaR5o:APA91bGmGRGhTSNwxZlkke-JrFrI7o5iKG7Va456qDIpwENxlQ0d0Ruvv5XnxkaRAqJR1tPvW5OePMiLYgcnI-cA3653Dun0M4iIvBmXwP870scKEygEI8GQadS5_gGJ54psLOQJp_Ut")
                        //.POST(HttpRequest.BodyPublishers.ofString(buildV1Message(tokenFCM, "Title", "Code: " + code)))
                        .POST(HttpRequest.BodyPublishers.ofString(buildMessage(tokenFCM, "Title", "Code: " + code)))
                        .build();

                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                // System.out.println("Access token: " + getAccessToken());
                System.out.println("Response status: " + response.statusCode());
                System.out.println("Response body: " + response.body());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        Response challenge = context.form()
                .createForm("push-notification-form.ftl");
        context.challenge(challenge);
    }

    private static String getAccessToken() throws IOException {
        // When loading resources in this way, it's important to close the stream after we're done with it to
        // avoid memory leaks => I use a try-with-resources (try (...) { ... })
        // which will automatically closes the InputStream after the try block finishes.
        try (InputStream serviceAccount = PNRequiredAction.class.getResourceAsStream("/fcm.json")) {
            if (serviceAccount == null) {
                throw new FileNotFoundException("Could not find file fcm.json in resources.");
            }

            GoogleCredentials googleCredentials = GoogleCredentials
                    .fromStream(serviceAccount)
                    .createScoped(List.of("https://www.googleapis.com/auth/firebase.messaging"));
            googleCredentials.refreshIfExpired();
            AccessToken token = googleCredentials.getAccessToken();
            return token.getTokenValue();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    private String buildV1Message(String token, String title, String body) {
        JsonObject message = Json.createObjectBuilder()
                .add("message", Json.createObjectBuilder()
                        .add("token", token)
                        .add("notification", Json.createObjectBuilder()
                                .add("title", title)
                                .add("body", body)))
                .build();
        return message.toString();
    }

    private String buildMessage(String token, String title, String body) {
        JsonObject message = Json.createObjectBuilder()
                .add("to", token)
                .add("notification", Json.createObjectBuilder()
                        .add("title", title)
                        .add("body", body))
                .build();
        return message.toString();
    }


    @Override
    public void processAction(RequiredActionContext context) {
        String answer = (context.getHttpRequest().getDecodedFormParameters().getFirst("code_access"));
        PNCredentialProvider pn = (PNCredentialProvider) context.getSession().getProvider(CredentialProvider.class, "push-notification");
        pn.createCredential(context.getRealm(), context.getUser(), PNCredentialModel.createPN());
        context.success();
    }

    @Override
    public void close() {

    }

    private String generateCode() {
        Random random = new Random();
        int code = 100000 + random.nextInt(900000);
        return String.valueOf(code);
    }
}
