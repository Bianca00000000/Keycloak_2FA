package org.keycloak.pushNotification.credential.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

// contains a json with the static credential information that can be shared in the Admin Console or via the REST API
public class PNCredentialData {
    // the last time the respective token was requested
    private final String dateOfLastUse;

    @JsonCreator
    public PNCredentialData(@JsonProperty("dateOfLastUse") String dateOfLastUse)
    {
        this.dateOfLastUse = dateOfLastUse;
    }
    public String getDateOfLastUse()
    {
        return dateOfLastUse;
    }
}
