package org.keycloak.pushNotification.services;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import com.fasterxml.jackson.databind.JsonNode;
import org.jboss.logging.Logger;
import org.keycloak.broker.provider.util.SimpleHttp;
import org.keycloak.models.KeycloakSession;

public class VaultService {
    private KeycloakSession session;
    private static final Logger logger = Logger.getLogger(VaultService.class);

    public VaultService(KeycloakSession session) {
        this.session = session;
    }

    /*
    Cu aceasta functie ar trebui sa putem obtine tokenul FCM din vault.
    vaultUrl => URL-ul instantei de HashiCorp pe care o sa o folosim. Acest URL o sa fie folosit pentru a contrui cererea HTTP catre Vault pentru a extrage secretul.
    realm => este folosit pentru a specifica un context sau un domeniu specific in cadrul caruia se afla secretul. Acesta poate fi utilizat pentru a organiza secretele in Vault, pentru diferite medii. Indica un anumit „path” în cadrul Vault unde secretele sunt stocate. Putem face chiar o corelare intre realmurile din keycloak cu acete zone din vault.
    vaultSecretEngineName => Motorul de secrete din vault de unde urmeaza sa fie recuperat secretul. Avem diferite motoare de secrete care au logici diferite de stocare, accesare si gestionare a secretelor.
    secretName => Numele secretului pe care vrem sa il recuperam => tokenul FCM.
    vaultToken => Token-ul de autentificare necesar pentru a accesa Vault. Acest token validează cererea ta și asigură că ai permisiunile necesare pentru a accesa secretul specificat.
    secretVersion => Versiunea secretului pe care dorești să o recuperezi. Vault suportă versiunea secretelor, permițându-ți să accesezi versiuni anterioare ale unui secret dacă este necesar.
     */
    public ByteBuffer getSecretFromVault(String vaultUrl, String realm, String vaultSecretEngineName, String secretName, String vaultToken, int secretVersion) {
        try {
            JsonNode node = SimpleHttp.doGet(vaultUrl + "v1/" + vaultSecretEngineName + "/data/" + realm+ "?version=" +secretVersion, session)
                    .header("X-Vault-Token", vaultToken)
                    .asJson();
            byte[] secretBytes = node.get("data").get("data").get(secretName).textValue().getBytes(StandardCharsets.UTF_8);
            return ByteBuffer.wrap(secretBytes);
        } catch (IOException e) {
            logger.error("Secret(FCM token) not available", e);
            return null;
        }
    }

    public boolean isVaultAvailable(String vaultUrl, String vaultToken) {
        String healthVaultUrl = vaultUrl + "v1/sys/health";
        try {
            JsonNode vaultHealthResponseNode = SimpleHttp.doGet(healthVaultUrl, session)
                    .asJson();
            boolean vaultIsInitialized = vaultHealthResponseNode.get("initialized").asBoolean();
            boolean vaultIsSealed = vaultHealthResponseNode.get("sealed").asBoolean();
            return (vaultIsInitialized && !vaultIsSealed);
        } catch (IOException e) {
            logger.error("vault service unavailable", e);
            return false;
        }
    }

}