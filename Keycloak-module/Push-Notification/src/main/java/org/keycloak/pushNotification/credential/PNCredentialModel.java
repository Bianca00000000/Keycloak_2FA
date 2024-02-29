package org.keycloak.pushNotification.credential;

import org.keycloak.common.util.Time;
import org.keycloak.credential.CredentialModel;
import org.keycloak.pushNotification.credential.dto.PNCredentialData;
import org.keycloak.pushNotification.credential.dto.PNSecretData;
import org.keycloak.util.JsonSerialization;

import java.io.IOException;

public class PNCredentialModel extends CredentialModel {

    // set the type of credential that we will use and that is how it will be recognized in the rest of the code
    public static final String TYPE = "PUSH_NOTIFICATION";

    // in this class we have a json with data that can be displayed in the Administration Console or sent via the REST API
    private final PNCredentialData credentialData;

    // data that must be protected by a leap and encryption algorithm in the database
    private final PNSecretData secretData;

    private PNCredentialModel(PNCredentialData credentialData, PNSecretData secretData){
        this.credentialData = credentialData;
        this.secretData = secretData;
    }

    private PNCredentialModel(String dateOfLastUse, String tokenFCMId) {
        credentialData = new PNCredentialData(dateOfLastUse);
        secretData = new PNSecretData(tokenFCMId);
    }

    // we set the data that will be saved in the database => some of the Credentials fields
    private void fillCredentialModelFields() {
        try{
            setCredentialData(JsonSerialization.writeValueAsString(credentialData));
            setSecretData(JsonSerialization.writeValueAsString(secretData));
            setType(TYPE);
            setCreatedDate(Time.currentTimeMillis()); // we can consider it as the time when the token was registered in the system
        }catch(IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    public static PNCredentialModel createPN(String dateOfLastUse, String tokenFCMId){
        PNCredentialModel credentialModel = new PNCredentialModel(dateOfLastUse, tokenFCMId);
        credentialModel.fillCredentialModelFields();
        return credentialModel;
    }

    // data extraction credential from the database => PN
    public static PNCredentialModel createFromCredentialModel(CredentialModel credentialModel){
        try {
            PNCredentialData credentialData = JsonSerialization.readValue(credentialModel.getCredentialData(), PNCredentialData.class);
            PNSecretData secretData = JsonSerialization.readValue(credentialModel.getSecretData(), PNSecretData.class);

            PNCredentialModel fcmTokenCredentialModel = new PNCredentialModel(credentialData, secretData);
            fcmTokenCredentialModel.setUserLabel(credentialModel.getUserLabel());
            fcmTokenCredentialModel.setCreatedDate(credentialModel.getCreatedDate());
            fcmTokenCredentialModel.setType(TYPE);
            fcmTokenCredentialModel.setId(credentialModel.getId());
            // fcmTokenCredentialModel.setSecretData(credentialModel.getSecretData());
            // fcmTokenCredentialModel.setCredentialData(credentialModel.getCredentialData());
            return fcmTokenCredentialModel;
        } catch (IOException ex){
            throw new RuntimeException(ex);
        }
    }

    public PNCredentialData getFCMTokenCredentialData() {
        return credentialData;
    }

    public PNSecretData getFCMTokenSecretData(){
        return secretData;
    }
}
