package org.keycloak.pushNotification.interfaces;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import jakarta.enterprise.context.ApplicationScoped;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import javax.annotation.PostConstruct;

@ApplicationScoped
public class FirebaseInitializer {
    @PostConstruct
    public void initialize() {
        try {
            InputStream serviceAccount = MyResourceProviderFactory.class.getResourceAsStream("/fcm.json");
            if (serviceAccount == null) {
                throw new FileNotFoundException("Could not find 'fcm.json' in the classpath");
            }
            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .build();

            if (FirebaseApp.getApps().isEmpty()) { // check if it has been initialized before
                FirebaseApp.initializeApp(options);
            }
        } catch (
                IOException e) {
            throw new RuntimeException("Failed to initialize Firebase", e);
        }
    }
}
