package keycloak.oauth.oauthauth.service.implementation;

import keycloak.oauth.oauthauth.modelDTO.UserAuthenticationResponse;
import keycloak.oauth.oauthauth.modelDTO.UserRegisterRequest;
import keycloak.oauth.oauthauth.service.interfaces.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {


    @Override
    public ResponseEntity<?> register(UserRegisterRequest request) throws Exception {
        return null;
    }

    @Override
    public UserAuthenticationResponse login(UserAuthenticationResponse request) {
        return null;
    }
}
