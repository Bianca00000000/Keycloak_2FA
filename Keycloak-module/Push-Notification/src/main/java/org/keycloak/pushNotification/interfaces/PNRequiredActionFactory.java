package org.keycloak.pushNotification.interfaces;

import org.keycloak.Config;
import org.keycloak.authentication.RequiredActionFactory;
import org.keycloak.authentication.RequiredActionProvider;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.KeycloakSessionFactory;

public class PNRequiredActionFactory implements RequiredActionFactory {

    private static final PNRequiredAction SINGLETON = new PNRequiredAction();

    @Override
    public RequiredActionProvider create(KeycloakSession session) {
        return SINGLETON;
    }


    @Override
    public String getId() {
        return PNRequiredAction.PROVIDER_ID;
    }

    @Override
    public String getDisplayText() {
        return "Push Notification";
    }

    @Override
    public void init(Config.Scope config) {

    }

    @Override
    public void postInit(KeycloakSessionFactory factory) {

    }

    @Override
    public void close() {

    }
}
