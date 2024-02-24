package keycloak.oauth.oauthauth.service.interfaces;

import keycloak.oauth.oauthauth.modelDTO.UserAuthenticationResponse;
import keycloak.oauth.oauthauth.modelDTO.UserRegisterRequest;
import org.springframework.http.ResponseEntity;

public interface AuthenticationService {
    public ResponseEntity<?> register(UserRegisterRequest request) throws Exception;
    public UserAuthenticationResponse login(UserAuthenticationResponse request);
}
