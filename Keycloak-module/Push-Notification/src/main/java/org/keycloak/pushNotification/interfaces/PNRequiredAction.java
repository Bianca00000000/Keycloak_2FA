package org.keycloak.pushNotification.interfaces;

import org.keycloak.authentication.CredentialRegistrator;
import org.keycloak.authentication.RequiredActionProvider;

public class PNRequiredAction implements RequiredActionProvider, CredentialRegistrator {

    public static final String PROVIDER_ID = "push_notification_config";

}
