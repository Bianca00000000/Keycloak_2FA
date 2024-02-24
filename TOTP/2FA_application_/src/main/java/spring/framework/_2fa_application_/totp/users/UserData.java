package spring.framework._2fa_application_.totp.users;

import com.google.cloud.spring.data.firestore.Document;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.checkerframework.common.aliasing.qual.Unique;
import org.springframework.data.annotation.Id;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collectionName = "users")
public class UserData {

    @Id
    private String id;

    @NotBlank
    @Unique
    private String username;

    @NotBlank
    private String password;
}
