package org.keycloak.pushNotification.interfaces;

import org.keycloak.common.util.Time;
import org.keycloak.credential.*;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.models.UserModel;

import org.keycloak.pushNotification.model.AccessCode;
import org.keycloak.pushNotification.model.PNCredentialModel;
import org.keycloak.pushNotification.services.AccessCodeService;

// CredentialInputValidator => lets Keycloak know that this provider can also be used to validate an authentication for an Authenticator
public class PNCredentialProvider implements CredentialProvider<PNCredentialModel>, CredentialInputValidator {
    protected KeycloakSession session;
    private AccessCodeService accessCodeService;

    public PNCredentialProvider(KeycloakSession session) {this.session = session;}

    // returns the credential type
    @Override
    public String getType() {
        return PNCredentialModel.TYPE;
    }

    // Is responsible for providing metadata about a particular credential type it manages
    // By constructing and returning a CredentialTypeMetadata object with these details, the function allows the authentication system to
    // understand and handle different credential types in a standardized way
    @Override
    public CredentialTypeMetadata getCredentialTypeMetadata(CredentialTypeMetadataContext metadataContext) {
        return CredentialTypeMetadata.builder()
                .type(getType()) // Identifies the type of authentication or credential it represents
                .category(CredentialTypeMetadata.Category.TWO_FACTOR) // Specifies the security category to which the credential belongs, in this case, it is marked as belonging to the two-factor authentication category (TWO_FACTOR).
                .displayName(PNCredentialProviderFactory.PROVIDER_ID) // Provides an identifier or name for the credential provider, which can be displayed in user interfaces to identify the authentication method.
                .helpText("Push notification authentication is a secure and fast way to verify your identity. " +
                        "After entering your username and password, you will receive a notification on your mobile device. " +
                        "You just have to enter the access code provided on the device in the desired application. " +
                        "Make sure you have enabled notifications for our app on your mobile device to receive login notifications promptly. " +
                        "This method adds an extra layer of security, protecting you against unauthorized access.")
                .createAction(PNAuthenticatorFactory.PROVIDER_ID) // Specifies the action or process required to create or register a new instance of this credential, associated with a provider identifier.
                .removeable(false) // Indicates whether or not the credential can be removed by the user.
                .build(session);
    }

    // creating a PNCredentialModel from a CredentialModel
    @Override
    public PNCredentialModel getCredentialFromModel(CredentialModel model) {
        return PNCredentialModel.createFromCredentialModel(model);
    }

    // The next two methods are to create/delete a login. These methods call the UserModel's credential manager,
    // which is responsible for knowing where to read/write the credentials, e.g. local storage or federated storage
    @Override
    public boolean deleteCredential(RealmModel realm, UserModel user, String credentialId) {
        return user.credentialManager().removeStoredCredentialById(credentialId);
    }

    @Override
    public CredentialModel createCredential(RealmModel realm, UserModel user, PNCredentialModel credentialModel) {
        if(credentialModel.getCreatedDate() == null) {
            credentialModel.setCreatedDate(Time.currentTimeMillis());
        }
        return user.credentialManager().createStoredCredential(credentialModel);
    }

    // Check if the authentication type is configured for a specific user
    @Override
    public boolean supportsCredentialType(String credentialType) {
        return getType().equals(credentialType);
    }

    @Override
    public boolean isConfiguredFor(RealmModel realm, UserModel user, String credentialType) {
        if(!supportsCredentialType(credentialType))
            return false;
        return user.credentialManager().getStoredCredentialsByTypeStream(credentialType).findAny().isPresent();
    }

    // For the CredentialInputValidator interface we have the main method isValid() => which tests if a credential
    // is valid for a certain user in a certain realm. This is the method called by the Authenticator when it tries to validate the user's input.
    @Override
    public boolean isValid(RealmModel realm, UserModel user, CredentialInput input)
    {
        if(!(input instanceof AccessCode)) {
            return false;
        }

        if(!input.getType().equals(getType())) {
            return false;
        }

        String challengeResponse = input.getChallengeResponse();
        if(challengeResponse == null) {
            return false;
        }

        return accessCodeService.isCodeValid(user.getId(), challengeResponse);
    }
}
