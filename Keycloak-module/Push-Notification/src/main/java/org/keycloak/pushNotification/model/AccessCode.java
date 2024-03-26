package org.keycloak.pushNotification.model;

import org.keycloak.credential.CredentialInput;

// After the user enters the first authentication factor => Keycloak creates a "UserSession" and caches it (using Infinispan).
// This session is not complete, but it indicates that the process is being completed. The session will have a "state" attribute that indicates the progress in the authentication flow.
// Bind the access code to an incomplete user session
public class AccessCode implements CredentialInput {
    private String code; // Randomly generated code
    private String userID;

    public AccessCode(String code, String userID) {
        this.code = code;
        this.userID = userID;
    }

    public String getCode(){
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getUserID() {
        return  this.userID;
    }

    public void setUserID(String userID){
        this.userID = userID;
    }

    @Override
    public String getCredentialId() {
        return "push-notification";
    }

    @Override
    public String getType() {
        return "ACCESS_CODE";
    }

    @Override
    public String getChallengeResponse() {
        return this.code;
    }

}
