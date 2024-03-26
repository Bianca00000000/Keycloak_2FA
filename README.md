# Keycloak_2FA

Trebuie deschis proiect din folderul: Keycloak-module/Push-Notification. => probleme la pom.xml in principal.
Creare jar: mvn clean si mvn clean package
Copiere din target a jar-ului Push-Notification-1.0-SNAPSHOT.jar in folderul Docker.
Date comenzile:
docker build -t biancadaniela/keycloak:latest .
docker compose up -d
Pornire din folderul 2FA_demo_website proiectul Frontend => npm start
Accesare: localhost:3000 => Adaugare credentiale de login si dupa cu comanda: docker logs docker-keycloak-1 => pentru a vedea unele erori care nu apar la build.
Credentiale login:

username: bianca.ionascu Parola: parola

Si daca instalezi si aplicatia android (FCMPushNotificationKeycloak) pe un device fizic ar trebui sa trimita o notificare push cu codul de acces => ce varianta legacy merge. Trebuie schimbat ip in folderul: FCMPushNotificationKeycloak la clasa Retrofit cu ip-ul masinii pe care se va rula aplicatia android.