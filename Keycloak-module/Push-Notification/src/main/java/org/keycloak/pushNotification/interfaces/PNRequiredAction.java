package org.keycloak.pushNotification.interfaces;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import jakarta.ws.rs.core.Response;
import org.keycloak.authentication.*;
import org.keycloak.credential.CredentialProvider;
import org.keycloak.models.UserModel;

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
//        String tokenFCM = tokensFCM.isEmpty() ? null : tokensFCM.get(0);
//        String code = generateCode();
//
//        if (tokenFCM != null) {
//            Message message = Message.builder()
//                    .setToken(tokenFCM)
//                    .setNotification(Notification.builder()
//                            .setTitle("Autentificare noua")
//                            .setBody("Aici este codul tau: " + code)
//                            .build())
//                    .build();
//
//            // DE MODIFICAT CU LOGGER AICI
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
