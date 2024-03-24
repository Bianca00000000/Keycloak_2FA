package org.keycloak.pushNotification.interfaces;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@ApplicationScoped
public class FirebaseInitializer {
    private String accessToken;

    @PostConstruct
    public void initialize() {
        try {
            InputStream serviceAccount = PNRequiredAction.class.getResourceAsStream("/fcm.json");
            if (serviceAccount == null) {
                throw new FileNotFoundException("Could not find 'fcm.json' in the classpath");
            }
            GoogleCredentials googleCredentials = GoogleCredentials.fromStream(serviceAccount);
            googleCredentials.refreshIfExpired();
            accessToken = googleCredentials.getAccessToken().getTokenValue();
;
            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(googleCredentials)
                    .build();

            if (FirebaseApp.getApps().isEmpty()) {
                System.out.println("S-a realizat initializarea");
                FirebaseApp.initializeApp(options);
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to initialize Firebase", e);
        }
    }

    public String getAccessToken() {
        return accessToken;
    }
}
