package org.keycloak.pushNotification.services;

import org.infinispan.Cache;
import org.keycloak.models.KeycloakSession;
import java.util.concurrent.TimeUnit;
import org.keycloak.connections.infinispan.InfinispanConnectionProvider;
import org.keycloak.pushNotification.model.AccessCode;

// va trebui adaugat in standalone.xml sau in standalone-ha.xml cache-ul pentru codurile de acces: accessCodesCache.
// acest fisier se gaseste in standalone/configuration/ => asta se face la runtime
// trebuie facuta si o securizare a accesului la acest cache => trb facute politici de securitate
public class AccessCodeService {
    private final KeycloakSession session;
    private Cache<String, String> accessCodesCache;

    public AccessCodeService(KeycloakSession session) {
        this.session = session;
        // pentru performanta initalizam cache-ul o singura data aici
        accessCodesCache = session.getProvider(InfinispanConnectionProvider.class).getCache("accessCodesCache");
    }

    public void storeAccessCode(String usserSessionId, String code) {
        AccessCode accessCode = new AccessCode(code, usserSessionId);
        accessCodesCache.put(accessCode.getUserSessionId(), accessCode.getCode(), 2, TimeUnit.MINUTES); // expira dupa 2 minute
    }

    public String retrieveAccessCode(String usserSessionId) {
        return accessCodesCache.get(usserSessionId);
    }
}
