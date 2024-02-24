package spring.framework._2fa_application_.config;


import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.FileInputStream;
import java.io.IOException;

@Configuration
@EnableConfigurationProperties(CustomProperties.class)
@RequiredArgsConstructor
public class FirebaseConfig {

    private final CustomProperties googleCredentialsPath;

    // Obtinem o instanta FirebaseApp pentru a comunica cu diferite servicii: Firestore, FCM
    @Bean
    public FirebaseApp firebaseApp() {
        try{
            FileInputStream serviceAccount = new FileInputStream(googleCredentialsPath.getGoogle_application_credentials());

            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .build();

            return FirebaseApp.initializeApp(options);

        }catch(IOException e)
        {
            throw new RuntimeException("Could not initialize Firebase Admin SDK: " + e.getMessage());
        }
    }
}