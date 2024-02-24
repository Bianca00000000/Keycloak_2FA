FROM quay.io/keycloak/keycloak:22.0.1 as builder

# Enable health and metrics support
ENV KC_HEALTH_ENABLED=true
ENV KC_METRICS_ENABLED=true

# Seteaza directorul de lucru in interiorul containerului la "/opt/keycloak"
WORKDIR /opt/keycloak

# for demonstration purposes only, please make sure to use proper certificates in production instead
# Generam cu keytool doar pentru testare o pereche de chei si un certificat auto-semnat => TO DO: de inlocuit in productie
# cu certificate valide (certificatul este stocat in conf/server.keystore)
RUN keytool -genkeypair -storepass password -storetype PKCS12 -keyalg RSA -keysize 2048 -dname "CN=server" -alias server -ext "SAN:c=DNS:localhost,IP:127.0.0.1" -keystore conf/server.keystore

# Rulam acest script pentru a pregati Keycloak pentru rulare bazandu-se pe variabilele de mediu si configuratiile setate anterior
RUN /opt/keycloak/bin/kc.sh build


# De aici, incepe a doua etapa a build-ului 

FROM quay.io/keycloak/keycloak:22.0.1
# Copiaza fisierele rezultate din etapa de build in noua etapa a imaginii
COPY --from=builder /opt/keycloak/ /opt/keycloak/

# Definire punct de intrare al containerului => executare script kc.sh , care este punctul de intrare standard pentru
# pornirea serverului Keycloak.
ENTRYPOINT ["/opt/keycloak/bin/kc.sh"]

# kc.sh => este un script de shell care se ocupa cu initializarea corecta a serverului Keycloak, inclusiv setarea
# configuratiilor de mediu, pregatirea bazelor de date si alte procese de startup necesare
# Folosind acest script ca ENTRYPOINT poti trece argumente suplimentare la containerul Docker la runtime care vor fi trasnmise scriptului permitand
# personalizarea comportamentului serverului
# Utilizarea scriptului standard kc.sh urmează documentația și ghidurile oficiale Keycloak, ceea ce înseamnă că este mai probabil să fie actualizat 
# și întreținut de către dezvoltatorii Keycloak, asigurând compatibilitate și actualizări