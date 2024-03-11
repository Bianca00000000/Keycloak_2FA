package org.keycloak.pushNotification.interfaces;

import org.keycloak.authentication.Authenticator;
import org.keycloak.authentication.CredentialValidator;
import org.keycloak.http.HttpCookie;
import org.keycloak.http.HttpResponse;
import org.keycloak.authentication.AuthenticationFlowContext;
import org.keycloak.authentication.AuthenticationFlowError;
import org.keycloak.authentication.RequiredActionFactory;
import org.keycloak.authentication.RequiredActionProvider;
import org.keycloak.credential.CredentialProvider;
import org.keycloak.models.AuthenticatorConfigModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.models.UserCredentialModel;
import org.keycloak.models.UserModel;

import jakarta.ws.rs.core.Cookie;
import jakarta.ws.rs.core.MultivaluedMap;
import jakarta.ws.rs.core.Response;
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
    public void setRequiredActions(KeycloakSession session, RealmModel realm, UserModel user) {
        user.addRequiredAction(PNRequiredAction.PROVIDER_ID);
    }

    public List<RequiredActionFactory> getRequiredActions(KeycloakSession session) {
        return Collections.singletonList((PNRequiredActionFactory)session.getKeycloakSessionFactory().getProviderFactory(RequiredActionProvider.class, PNRequiredAction.PROVIDER_ID));
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
    public void authenticate(AuthenticationFlowContext context) {
        if (hasCookie(context)) {
            context.success();
            return;
        }
        // DE IMPLEMENTAT LOGICA PENTRU PARTEA DE AUTENTIFICARE CU PN => AFISARE PAGINA WEB PT A INTRODUCE CODUL
    }

    @Override
    public void action(AuthenticationFlowContext context) {
        // In cazul in care este valid codul setam cookie-ul altfel da o eroare si ii spunem sa reincerce conectarea
    }

    protected boolean validateAnswer(AuthenticationFlowContext context) {
        return false;
        // De verficat codul introdus de utilizator.
    }

}
