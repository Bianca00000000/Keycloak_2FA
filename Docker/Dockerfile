FROM quay.io/keycloak/keycloak:24.0.1

USER root

# Set the working directory inside the container to "/opt/keycloak"
WORKDIR /opt/keycloak

# for demonstration purposes only, please make sure to use proper certificates in production instead
# We generate with keytool only for testing a pair of keys and a self-signed certificate => TODO: to be replaced in production
# with valid certificates (the certificate is stored in conf/server.keystore)
RUN keytool -genkeypair -storepass password -storetype PKCS12 -keyalg RSA -keysize 2048 -dname "CN=server" -alias server -ext "SAN:c=DNS:localhost,IP:127.0.0.1" -keystore conf/server.keystore

# Copy the JAR of your module to the deployments directory of Keycloak
COPY Push-Notification-1.0-SNAPSHOT.jar /opt/keycloak/providers/

COPY Theme/html/code-pn.ftl  themes/base/email/html/
COPY Theme/text/push-notification-form.ftl themes/base/email/text/
COPY Theme/messages/messages_en.properties themes/base/email/messages/
COPY Theme/messages/messages_ro.properties themes/base/email/messages/

# We run this script to prepare Keycloak to run based on previously set environment variables and configurations
RUN /opt/keycloak/bin/kc.sh build

USER 1000

# Define container entry point => execute kc.sh script, which is the standard entry point for starting the Keycloak server.
ENTRYPOINT ["/opt/keycloak/bin/kc.sh", "start", "--hostname-strict=false", "--http-enabled=true", "--http-port=8080"]

# kc.sh => is a shell script that takes care of properly initializing the Keycloak server, including setting
# environment configurations, database preparation and other necessary startup processes
# Using this script as an ENTRYPOINT you can pass additional arguments to the Docker container at runtime that will be passed to the script allowing
# customizing server behavior
# Using the standard kc.sh script follows the official Keycloak documentation and guides, which means it's more likely to be updated
# and maintained by Keycloak developers, ensuring compatibility and updates