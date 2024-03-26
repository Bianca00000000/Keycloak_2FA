package org.keycloak.pushNotification.services;

import org.infinispan.Cache;
import org.keycloak.models.KeycloakSession;
import java.util.concurrent.TimeUnit;
import org.keycloak.connections.infinispan.InfinispanConnectionProvider;
import org.keycloak.pushNotification.model.AccessCode;

public class AccessCodeService {
    private final KeycloakSession session;
    private Cache<String, String> accessCodesCache;

    public AccessCodeService(KeycloakSession session) {
        this.session = session;
        // for performance, we initialize the cache only once here
        accessCodesCache = session.getProvider(InfinispanConnectionProvider.class).getCache("accessCodesCache");
    }

    public void storeAccessCode(String userID, String code) {
        AccessCode accessCode = new AccessCode(code, userID);
        accessCodesCache.put(accessCode.getUserID(), accessCode.getCode(), 2, TimeUnit.MINUTES); // it expires after 2 minutes
    }

    public boolean isCodeValid(String userID, String code) {
        String codeChace = retrieveAccessCode(userID);
        if(codeChace == code)
        {
            return true;
        }
        return false;
    }

    public String retrieveAccessCode(String userID) {
        return accessCodesCache.get(userID);
    }
}
