package spring.framework._2fa_application_.totp.secret;

import com.google.cloud.spring.data.firestore.FirestoreReactiveRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SecretRepository extends FirestoreReactiveRepository<SecretData> {
}
