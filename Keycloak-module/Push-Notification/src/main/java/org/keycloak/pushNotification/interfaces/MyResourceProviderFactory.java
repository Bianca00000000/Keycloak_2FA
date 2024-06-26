package org.keycloak.pushNotification.interfaces;

import org.keycloak.Config;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.KeycloakSessionFactory;
import org.keycloak.services.resource.RealmResourceProvider;
import org.keycloak.services.resource.RealmResourceProviderFactory;

public class MyResourceProviderFactory implements RealmResourceProviderFactory {
    public static final String PROVIDER_ID = "token-fcm";

    @Override
    public RealmResourceProvider create(KeycloakSession keycloakSession) {
        return new MyResourceProvider(keycloakSession);
    }

    @Override
    public void init(Config.Scope scope) {
    }

    // This function will be run only once at start-up => firebase sdk initialization
    @Override
    public void postInit(KeycloakSessionFactory keycloakSessionFactory) {
    }

    @Override
    public void close() {
    }

    @Override
    public String getId() {
        return PROVIDER_ID;
    }

}
