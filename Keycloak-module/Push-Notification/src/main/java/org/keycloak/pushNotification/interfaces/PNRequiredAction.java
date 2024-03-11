package org.keycloak.pushNotification.interfaces;

import jakarta.ws.rs.core.Response;
import org.keycloak.authentication.CredentialRegistrator;
import org.keycloak.authentication.RequiredActionContext;
import org.keycloak.authentication.RequiredActionProvider;

public class PNRequiredAction implements RequiredActionProvider, CredentialRegistrator {

    public static final String PROVIDER_ID = "push_notification_config";

    @Override
    public void evaluateTriggers(RequiredActionContext context) {

    }

    @Override
    public void requiredActionChallenge(RequiredActionContext context) {
        Response challenge = context.form().createForm("push-notification-config.ftl");
        context.challenge(challenge);
    }

    @Override
    public void processAction(RequiredActionContext context) {
        // aici de reimplementat sa vad cum fac aceasta actiun cum sa o gandesc
        /*String answer = (context.getHttpRequest().getDecodedFormParameters().getFirst("secret_answer"));
        PNCredentialProvider pncp = (SecretQuestionCredentialProvider) context.getSession().getProvider(CredentialProvider.class, "secret-question");
        sqcp.createCredential(context.getRealm(), context.getUser(), SecretQuestionCredentialModel.createSecretQuestion("What is your mom's first name?", answer));
        context.success();*/
    }

    @Override
    public void close() {

    }
}
