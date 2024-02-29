package org.keycloak.pushNotification.interfaces;

import org.keycloak.Config;
import org.keycloak.authentication.Authenticator;
import org.keycloak.authentication.AuthenticatorFactory;
import org.keycloak.authentication.ConfigurableAuthenticatorFactory;
import org.keycloak.models.AuthenticationExecutionModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.KeycloakSessionFactory;
import org.keycloak.provider.ProviderConfigProperty;

import java.util.ArrayList;
import java.util.List;

public class PNAuthenticatorFactory implements AuthenticatorFactory, ConfigurableAuthenticatorFactory {

    public static final String PROVIDER_ID = "push-notification-authenticator";
    private static final PNAuthenticator SINGLETON = new PNAuthenticator();
    private static final List<ProviderConfigProperty> configProperties = new ArrayList<ProviderConfigProperty>();

    static {
        ProviderConfigProperty property;
        property = new ProviderConfigProperty();
        property.setName("cookie.max.age");
        property.setLabel("Cookie Max Age");
        property.setType(ProviderConfigProperty.STRING_TYPE);
        property.setHelpText("Max age in seconds of the PUSH_NOTIFICATION_COOKIE.");
        configProperties.add(property);
    }

    @Override
    public String getId() {
        return PROVIDER_ID;
    }

    @Override
    public Authenticator create(KeycloakSession session) {
        return SINGLETON;
    }

    // The options available for configuring how a particular authenticator will be used within an authentication flow.
    private static AuthenticationExecutionModel.Requirement[] REQUIREMENT_CHOICES = {
            AuthenticationExecutionModel.Requirement.REQUIRED, // authenticator is mandatory in the authentication process => the user must pass this step successfully to continue.
            AuthenticationExecutionModel.Requirement.ALTERNATIVE, // if there are multiple authenticators marked as ALTERNATIVES in a flow, the user only needs to successfully complete one of these steps to proceed.
            AuthenticationExecutionModel.Requirement.DISABLED // This option indicates that the authenticator is disabled and will not be considered in the authentication flow
    };

    @Override
    public AuthenticationExecutionModel.Requirement[] getRequirementChoices() {
        return REQUIREMENT_CHOICES;
    }

    // This method indicates whether users are allowed to configure or manage settings for this authenticator from the Keycloak UI.
    // If it returns true, it means that there are options or settings that users can adjust for this specific authenticator
    // (eg, configuring secondary authentication methods, preferences, etc.). If it returns false,
    // this capability is disabled, and users will not be able to perform settings or configurations related to this authenticator.
    @Override
    public boolean isUserSetupAllowed() {
        return true;
    }

    // This method indicates whether the authenticator is configurable at the administration level. If it returns true,
    // it means that administrators can adjust specific settings or parameters for this authenticator through the Keycloak admin console.
    // This may include configuring specific details of how the authenticator works, such as secret keys, redirect URLs,
    // and other parameters. If it returns false, it means the authenticator has no configurable options in the admin console.
    @Override
    public boolean isConfigurable() {
        return true;
    }

    @Override
    public String getHelpText() {
        return "Push notification authentication is a secure and fast way to verify your identity. " +
                "After entering your username and password, you will receive a notification on your mobile device. " +
                "You just have to enter the access code provided on the device in the desired application. " +
                "Make sure you have enabled notifications for our app on your mobile device to receive login notifications promptly. " +
                "This method adds an extra layer of security, protecting you against unauthorized access.";
    }

    @Override
    public String getDisplayType() {
        return "Push Notification";
    }

    @Override
    public String getReferenceCategory() {
        return "Push Notification";
    }

    @Override
    public List<ProviderConfigProperty> getConfigProperties() {
        return configProperties;
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
