package spring.framework._2fa_application_.totp.codeOTP;

import com.google.cloud.spring.data.firestore.FirestoreReactiveRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CodeRepository extends FirestoreReactiveRepository<CodeData> {
}
