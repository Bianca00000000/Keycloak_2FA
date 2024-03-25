# Keycloak_2FA

1. Trebuie deschis proiect din folderul: Keycloak-module.
2. Creare jar: mvn clean si mvn clean package
3. Copiere din target a jar-ului Push-Notification-1.0-SNAPSHOT.jar in folderul Docker.
4. Date comenzile:
   - docker build -t biancadaniela/keycloak:latest .
   - docker compose up -d
