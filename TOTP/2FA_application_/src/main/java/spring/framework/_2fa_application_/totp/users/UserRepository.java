package spring.framework._2fa_application_.totp.users;

import com.google.cloud.spring.data.firestore.FirestoreReactiveRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends FirestoreReactiveRepository<UserData> {
}
