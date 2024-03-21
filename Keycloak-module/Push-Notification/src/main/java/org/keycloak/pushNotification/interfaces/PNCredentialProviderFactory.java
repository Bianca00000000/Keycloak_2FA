package org.keycloak.pushNotification.interfaces;

import org.keycloak.credential.CredentialProvider;
import org.keycloak.credential.CredentialProviderFactory;
import org.keycloak.models.KeycloakSession;

public class PNCredentialProviderFactory implements CredentialProviderFactory<PNCredentialProvider> {
    public static final String PROVIDER_ID = "push-notification";
    @Override
    public String getId() {
        return  PROVIDER_ID;
    }

    @Override
    public CredentialProvider create(KeycloakSession session)
    {
        return new PNCredentialProvider(session);
    }
}
