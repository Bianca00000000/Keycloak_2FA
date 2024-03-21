package org.keycloak.pushNotification.interfaces;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.UserModel;
import org.keycloak.pushNotification.credential.dto.TokenFCMRequest;
import org.keycloak.services.resource.RealmResourceProvider;
import jakarta.ws.rs.NotAuthorizedException;
import org.keycloak.services.managers.AppAuthManager;
import org.keycloak.services.managers.AuthenticationManager.AuthResult;

import java.util.Arrays;

public class MyResourceProvider implements RealmResourceProvider {
    private final KeycloakSession session;
    public MyResourceProvider(KeycloakSession session) {
        this.session = session;
    }
    @Override
    public Object getResource() {
        return this;
    }
    @Override
    public void close() {
    }

    @POST
    @Path("fcmToken")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response saveFcmToken(TokenFCMRequest tokenRequest) {
        AuthResult auth = checkAuth(session);
        if (auth == null) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }

        String userId = auth.getUser().getId();
        String fcmToken = tokenRequest.getToken();
        boolean isSaved = saveTokenFCMForUser(userId, fcmToken);

        if(isSaved) {
            return Response.ok().build();
        } else {
            return Response.status(Response.Status.BAD_REQUEST).entity("Could not save the token").build();
        }
    }

    private AuthResult checkAuth(KeycloakSession session) {
        AuthResult auth = new AppAuthManager.BearerTokenAuthenticator(session).authenticate();
        if (auth == null) {
            throw new NotAuthorizedException("Bearer");
        }
        return auth;
    }

    private boolean saveTokenFCMForUser(String userId, String fcmToken) {
        UserModel user = session.users().getUserById(session.getContext().getRealm(), userId);
        if (user != null) {
            user.setAttribute("tokenFCM", Arrays.asList(fcmToken));
            return true;
        } else {
            return false;
        }
    }

}
