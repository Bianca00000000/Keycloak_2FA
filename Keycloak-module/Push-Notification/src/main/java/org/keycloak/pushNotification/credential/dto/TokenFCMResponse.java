package org.keycloak.pushNotification.credential.dto;

public class TokenFCMResponse {
    private String token;

    public TokenFCMResponse(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
