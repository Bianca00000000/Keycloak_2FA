package org.keycloak.pushNotification.model;

import org.keycloak.credential.CredentialInput;

// Dupa ce utilizatorul introduce primul factor de autentificare => Keycloak creeaza un "UserSession" si il stocheaza in cache (folosind Infinispan).
// Aceasta sesiune nu este conpleta, dar indica faptul ca procesul e in curs de completare. Sesiunea va avea un atribut "state" care indica progresul
// in fluxul de autentificare.
// Legam codul de acces de o sesiune incompleta de utilizator
public class AccessCode implements CredentialInput {
    private String code; // Codul generat random
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
