FROM quay.io/keycloak/keycloak:24.0.1

USER root

# Seteaza directorul de lucru in interiorul containerului la "/opt/keycloak"
WORKDIR /opt/keycloak

# for demonstration purposes only, please make sure to use proper certificates in production instead
# Generam cu keytool doar pentru testare o pereche de chei si un certificat auto-semnat => TO DO: de inlocuit in productie
# cu certificate valide (certificatul este stocat in conf/server.keystore)
RUN keytool -genkeypair -storepass password -storetype PKCS12 -keyalg RSA -keysize 2048 -dname "CN=server" -alias server -ext "SAN:c=DNS:localhost,IP:127.0.0.1" -keystore conf/server.keystore

# Copiaza JAR-ul modulului tau in directorul deployments al Keycloak
COPY Push-Notification-1.0-SNAPSHOT.jar /opt/keycloak/providers/

COPY Theme/html/code-pn.ftl  themes/base/email/html/
COPY Theme/text/push-notification-form.ftl themes/base/email/text/
COPY Theme/messages/messages_en.properties themes/base/email/messages/
COPY Theme/messages/messages_ro.properties themes/base/email/messages/


# Rulam acest script pentru a pregati Keycloak pentru rulare bazandu-se pe variabilele de mediu si configuratiile setate anterior
RUN /opt/keycloak/bin/kc.sh build

USER 1000

# Definire punct de intrare al containerului => executare script kc.sh , care este punctul de intrare standard pentru
# pornirea serverului Keycloak.
ENTRYPOINT ["/opt/keycloak/bin/kc.sh", "start", "--hostname-strict=false", "--http-enabled=true", "--http-port=8080"]

# kc.sh => este un script de shell care se ocupa cu initializarea corecta a serverului Keycloak, inclusiv setarea
# configuratiilor de mediu, pregatirea bazelor de date si alte procese de startup necesare
# Folosind acest script ca ENTRYPOINT poti trece argumente suplimentare la containerul Docker la runtime care vor fi trasnmise scriptului permitand
# personalizarea comportamentului serverului
# Utilizarea scriptului standard kc.sh urmeaza documentatia si ghidurile oficiale Keycloak, ceea ce inseamna ca este mai probabil sa fie actualizat 
# si intretinut de catre dezvoltatorii Keycloak, asigurand compatibilitate si actualizari