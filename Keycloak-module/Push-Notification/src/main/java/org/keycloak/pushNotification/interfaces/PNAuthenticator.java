package org.keycloak.pushNotification.interfaces;

import jakarta.ws.rs.core.MultivaluedMap;
import org.keycloak.authentication.*;
import org.keycloak.http.HttpCookie;
import org.keycloak.http.HttpResponse;
import org.keycloak.credential.CredentialProvider;
import org.keycloak.models.AuthenticatorConfigModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.models.UserModel;

import jakarta.ws.rs.core.Cookie;
import jakarta.ws.rs.core.Response;
import org.keycloak.pushNotification.model.AccessCode;

import java.net.URI;
import java.util.Collections;
import java.util.List;

public class PNAuthenticator implements Authenticator, CredentialValidator<PNCredentialProvider> {

    @Override
    public boolean requiresUser() {
        return true;
    }

    @Override
    public boolean configuredFor(KeycloakSession session, RealmModel realm, UserModel user) {
        return getCredentialProvider(session).isConfiguredFor(realm, user, getType(session));
    }

    @Override
    public PNCredentialProvider getCredentialProvider(KeycloakSession session) {
        return (PNCredentialProvider) session.getProvider(CredentialProvider.class, PNCredentialProviderFactory.PROVIDER_ID);
    }

    @Override
    public void close() {

    }

    protected boolean hasCookie(AuthenticationFlowContext context) {
        Cookie cookie = context.getHttpRequest().getHttpHeaders().getCookies().get("PUSH_NOTIFICATION_COMPLETED");
        boolean result = cookie != null;
        if (result) {
            System.out.println("Bypassing push notification because cookie is set");
        }
        return result;
    }

    public void addCookie(AuthenticationFlowContext context, String name, String value, String path, String domain, String comment, int maxAge, boolean secure, boolean httpOnly) {
        HttpResponse response = context.getSession().getContext().getHttpResponse();
        response.setCookieIfAbsent(new HttpCookie(1, name, value, path, domain, comment, maxAge, secure, httpOnly, null));
    }

    protected void setCookie(AuthenticationFlowContext context) {
        AuthenticatorConfigModel config = context.getAuthenticatorConfig();
        int maxCookieAge = 60 * 60 * 24 * 30; // 30 days
        if (config != null) {
            maxCookieAge = Integer.valueOf(config.getConfig().get("cookie.max.age"));
        }
        URI uri = context.getUriInfo().getBaseUriBuilder().path("realms").path(context.getRealm().getName()).build();
        addCookie(context, "PUSH_NOTIFICATION_COMPLETED", "true",
                uri.getRawPath(),
                null, null,
                maxCookieAge,
                false, true);
    }

    @Override
    public void setRequiredActions(KeycloakSession session, RealmModel realm, UserModel user) {
        user.addRequiredAction(PNRequiredAction.PROVIDER_ID);
    }

    public List<RequiredActionFactory> getRequiredActions(KeycloakSession session) {
        return Collections.singletonList((PNRequiredActionFactory)session.getKeycloakSessionFactory().getProviderFactory(RequiredActionProvider.class, PNRequiredAction.PROVIDER_ID));
    }

    @Override
    public void authenticate(AuthenticationFlowContext context) {
        if (hasCookie(context)) {
            context.success();
            return;
        }
        UserModel user = context.getUser();
        List<String> tokensFCM = user.getAttributes().get("tokenFCM");
        String tokenFCM = tokensFCM.isEmpty() ? null : tokensFCM.get(0);
        if(tokenFCM == null) {
            context.challenge(
                    context.form().setError("Instaleaza aplicatia mobila.")
                            .createForm("required-action-config.ftl")
            );
            return;
        }

        Response challenge = context.form()
                .createForm("push-notification-form.ftl");
        context.challenge(challenge);
    }

    @Override
    public void action(AuthenticationFlowContext context) {
        // If the code is valid, we set the cookie, otherwise it gives an error and we tell it to try connecting again
        boolean validated = validateAnswer(context);
        if (!validated) {
            Response challenge =  context.form()
                    .setError("badAccessCode")
                    .createForm("push-notification-form.ftl");
            context.failureChallenge(AuthenticationFlowError.INVALID_CREDENTIALS, challenge);
            return;
        }
        setCookie(context);
        context.success();
    }

    protected boolean validateAnswer(AuthenticationFlowContext context) {
        MultivaluedMap<String, String> formData = context.getHttpRequest().getDecodedFormParameters();
        String secret = formData.getFirst("code_access");

        AccessCode input = new AccessCode(secret, context.getUser().getId());
        return getCredentialProvider(context.getSession()).isValid(context.getRealm(), context.getUser(), input);
    }
}
