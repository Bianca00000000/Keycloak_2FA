package org.keycloak.pushNotification.interfaces;

import com.google.auth.oauth2.AccessToken;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
//import com.maxmind.geoip2.DatabaseReader;
//import com.maxmind.geoip2.exception.GeoIp2Exception;
//import com.maxmind.geoip2.model.CityResponse;
//import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.ws.rs.core.Response;
import org.keycloak.authentication.*;
import org.keycloak.credential.CredentialProvider;
import org.keycloak.models.UserModel;
import org.keycloak.pushNotification.model.PNCredentialModel;
import ua_parser.Client;
import ua_parser.Parser;

import java.io.*;
import java.net.InetAddress;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Random;

public class PNRequiredAction implements RequiredActionProvider, CredentialRegistrator {

    public static final String PROVIDER_ID = "push_notification_config";
//    @Inject
//    FirebaseMessaging firebaseMessaging;

//    private void sendPushNotification(String registrationToken, String title, String body) throws FirebaseMessagingException{
//        Message message = Message.builder()
//                .putData("title", title)
//                .putData("body", body)
//                .setToken(registrationToken)
//                .build();
//
//        // Send a message to the device corresponding to the provided
//        // registration token.
//        String response = firebaseMessaging.send(message);
//        // Response is a message ID string.
//        System.out.println("Successfully sent message: " + response);
//    }
    @Override
    public void evaluateTriggers(RequiredActionContext context) {

    }

    @Override
    public void requiredActionChallenge(RequiredActionContext context) {
        UserModel user = context.getUser();
        List<String> tokensFCM = user.getAttributes().get("tokenFCM");
        String tokenFCM = tokensFCM.isEmpty() ? null : tokensFCM.get(0);
        String code = generateCode();

        // device details
        String device = getDevice(context);

        // location details
        //String location = getLocation(context);

        // time + date
        String time = getTime();

        if (tokensFCM != null) {
            try {
                //sendPushNotification(tokenFCM, "Titlu", "Mesaj");
                HttpClient client = HttpClient.newHttpClient();
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(new URI("https://fcm.googleapis.com/v1/projects/fcmpushnotificationkeycloak/messages:send"))
                        .header("Content-Type", "application/json; UTF-8")
                        .header("Authorization", "Bearer " + getAccessToken())
                        //.uri(new URI("https://fcm.googleapis.com/fcm/send"))
                        //.header("Content-Type", "application/json")
                        //.header("Authorization", "Bearer " + "server key should be put here")
                        .POST(HttpRequest.BodyPublishers.ofString(buildV1Message(tokenFCM, "Title", "Code: " + code, user.getEmail(), device, "location", time)))
                        //.POST(HttpRequest.BodyPublishers.ofString(buildMessage(tokenFCM, "Login Detected", "Access Code: " + code, user.getEmail(), device, "location", time)))
                        .build();

                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                System.out.println("Access token: " + getAccessToken());
                System.out.println("Response status: " + response.statusCode());
                System.out.println("Response body: " + response.body());
                System.out.println("Device: " + device);
                System.out.println("Time: " + time);
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
        try (InputStream serviceAccount = PNRequiredAction.class.getResourceAsStream("/the file with the credentials from the service account must be placed here (it must also be in the resources folder)")) {
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

    private String buildV1Message(String token, String title, String body, String user, String device, String location, String time) {
        JsonObject message = Json.createObjectBuilder()
                .add("message", Json.createObjectBuilder()
                        .add("token", token)
                        .add("notification", Json.createObjectBuilder()
                                .add("title", title)
                                .add("body", body)))
                .add("data", Json.createObjectBuilder()
                        .add("user", user)
                        .add("device", device)
                        .add("location", location)
                        .add("time", time))
                .build();
        return message.toString();
    }

    private String buildMessage(String token, String title, String body, String user, String device, String location, String time) {
        JsonObject message = Json.createObjectBuilder()
                .add("to", token)
                .add("notification", Json.createObjectBuilder()
                        .add("title", title)
                        .add("body", body))
                .add("data", Json.createObjectBuilder()
                        .add("user", user)
                        .add("device", device)
                        .add("location", location)
                        .add("time", time))
                .build();
        return message.toString();
    }

    private String getDevice(RequiredActionContext context) {

        // use userAgent to determine device details (such as device type, operating system and browser)
        String userAgentString = context.getHttpRequest().getHttpHeaders().getHeaderString("User-Agent");
        Parser uaParser = new Parser();
        Client clientParser = uaParser.parse(userAgentString);
        String browser = clientParser.userAgent.family + " " + clientParser.userAgent.major + "." + clientParser.userAgent.minor;
        String so = clientParser.os.family + " " + clientParser.os.major + "." + clientParser.os.minor;
        String device = clientParser.device.family;

        return so + browser + device;
    }

//    private String getLocation(RequiredActionContext context) {
//        String ip = context.getConnection().getRemoteAddr();
//
//        try (InputStream databaseStream = getClass().getResourceAsStream("/here you need the path to the city database(from the resources folder)")) {
//            if (databaseStream == null) {
//                throw new IOException("Database not found in resources.");
//            }
//
//            DatabaseReader dbReader = new DatabaseReader.Builder(databaseStream).build();
//            InetAddress ipAddress = InetAddress.getByName(ip);
//            CityResponse response = dbReader.city(ipAddress);
//            String city = response.getCity().getName();
//            String country = response.getCountry().getName();
//
//            return city + ", " + country;
//        }
//        catch (IOException | GeoIp2Exception e) {
//            e.printStackTrace();
//            return "Location unknown";
//        }
//    }

    private String getTime(){
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d MMMM, yyyy, HH:mm");
        String formattedDateTime = now.format(formatter);
        return formattedDateTime;
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
