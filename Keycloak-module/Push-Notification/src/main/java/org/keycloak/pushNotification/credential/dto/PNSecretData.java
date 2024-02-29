package org.keycloak.pushNotification.credential.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

// contains a static JSON with information that cannot be passed outside of Keycloak
public class PNSecretData {

    // the unique identifier by which we can identify the token in the vault
    private final String tokenFCMId;

    @JsonCreator
    public PNSecretData(@JsonProperty("tokenFCMId") String tokenFCMId)
    {
        this.tokenFCMId = tokenFCMId;
    }

    public String getTokenFCMId()
    {
        return tokenFCMId;
    }
}
