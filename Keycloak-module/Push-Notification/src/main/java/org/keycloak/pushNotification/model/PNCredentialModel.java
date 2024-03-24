package org.keycloak.pushNotification.model;

import org.keycloak.credential.CredentialModel;

public class PNCredentialModel extends CredentialModel {

    public static final String TYPE = "PUSH_NOTIFICATION";

    private PNCredentialModel() {

    }

    public static PNCredentialModel createFromCredentialModel(CredentialModel credentialModel){
        PNCredentialModel secretQuestionCredentialModel = new PNCredentialModel();
        secretQuestionCredentialModel.setUserLabel(credentialModel.getUserLabel());
        secretQuestionCredentialModel.setCreatedDate(credentialModel.getCreatedDate());
        secretQuestionCredentialModel.setType(TYPE);
        secretQuestionCredentialModel.setId(credentialModel.getId());
        return secretQuestionCredentialModel;
    }

    public static PNCredentialModel createPN()
    {
        return new PNCredentialModel();
    }
}
