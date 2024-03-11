package org.keycloak.pushNotification.model;

// Dupa ce utilizatorul introduce primul factor de autentificare => Keycloak creeaza un "UserSession" si il stocheaza in cache (folosind Infinispan).
// Aceasta sesiune nu este conpleta, dar indica faptul ca procesul e in curs de completare. Sesiunea va avea un atribut "state" care indica progresul
// in fluxul de autentificare.
// Legam codul de acces de o sesiune incmpleta de utilizator => pentru
public class AccessCode {
    private String code; // Codul generat random
    private String userSessionId; // ID-ul sesiunii utilizatorului de care e legat codul de acces
    public AccessCode(String code, String userSessionId) {
        this.code = code;
        this.userSessionId = userSessionId;
    }
    public String getCode(){
        return this.code;
    }
    public String getUserSessionId() {
        return userSessionId;
    }
    public void setCode(String code) {
        this.code = code;
    }
    public void setUserSessionId(String userSessionId) {
        this.userSessionId = userSessionId;
    }
}
