package org.keycloak.pushNotification.interfaces;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.ws.rs.core.Response;
import org.keycloak.authentication.*;
import org.keycloak.credential.CredentialProvider;
import org.keycloak.models.UserModel;
import org.keycloak.pushNotification.model.PNCredentialModel;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Random;

public class PNRequiredAction implements RequiredActionProvider, CredentialRegistrator {
    private final FirebaseInitializer firebaseInitializer = new FirebaseInitializer();
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
                firebaseInitializer.initialize();
                String accessToken = firebaseInitializer.getAccessToken();
                HttpClient client = HttpClient.newHttpClient();
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(new URI("https://fcm.googleapis.com/fcm/send"))
                        .header("Content-Type", "application/json")
                        .header("Authorization", "Bearer " + accessToken)
                        .POST(HttpRequest.BodyPublishers.ofString(buildMessage(tokenFCM, "Autentificare noua", "Aici este codul tau: " + code)))
                        .build();

                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                System.out.println("Access token: " + accessToken);
                System.out.println("Response status: " + response.statusCode());
                System.out.println("Response body: " + response.body());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

//        if (tokenFCM != null) {
//            Message message = Message.builder()
//                    .setToken(tokenFCM)
//                    .setNotification(Notification.builder()
//                            .setTitle("Autentificare noua")
//                            .setBody("Aici este codul tau: " + code)
//                            .build())
//                    .build();
//
//            // LOGGER HERE
//            try {
//                String response = FirebaseMessaging.getInstance().send(message);
//                System.out.println("Successfully sent message: " + response);
//            } catch (Exception e) {
//                System.err.println("Failed to send message: " + e.getMessage());
//                return;
//            }
//        }

        Response challenge = context.form()
                .createForm("push-notification-form.ftl");
        context.challenge(challenge);
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
