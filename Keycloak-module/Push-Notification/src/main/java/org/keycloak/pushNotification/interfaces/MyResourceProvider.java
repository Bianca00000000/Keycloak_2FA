package org.keycloak.pushNotification.interfaces;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.UserModel;
import org.keycloak.pushNotification.credential.dto.TokenFCMRequest;
import org.keycloak.pushNotification.credential.dto.TokenFCMResponse;
import org.keycloak.services.resource.RealmResourceProvider;
import org.keycloak.services.managers.AppAuthManager;
import org.keycloak.services.managers.AuthenticationManager.AuthResult;

import java.util.Arrays;
import java.util.List;

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

    @GET
    @Path("fcmToken")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getFcmToken() {
        AuthResult auth = checkAuth(session);
        if (auth == null) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }

        String userId = auth.getUser().getId();
        String fcmToken = getFcmTokenForUser(userId);

        TokenFCMResponse tokenResponse = null;
        if (fcmToken != null) {
            tokenResponse = new TokenFCMResponse(fcmToken);
            return Response.ok(tokenResponse, MediaType.APPLICATION_JSON).build();
        }
        else {
            return Response.status(Response.Status.NOT_FOUND).entity("Token FCM not found for user").build();
        }
    }

    private String getFcmTokenForUser(String userId) {
        UserModel user = session.users().getUserById(session.getContext().getRealm(), userId);
        if (user != null) {
            List<String> tokenFCM = user.getAttributes().get("tokenFCM");
            if(tokenFCM != null && !tokenFCM.isEmpty()) {
                return tokenFCM.get(0);
            }
        }
        return null;
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
            return Response.ok("FCM token transmitted successfully!", MediaType.APPLICATION_JSON).build();
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
