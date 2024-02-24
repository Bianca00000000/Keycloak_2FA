package spring.framework._2fa_application_.totp.secret;

import com.google.cloud.spring.data.firestore.Document;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.data.annotation.Id;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collectionName = "secrets")
public class SecretData {

    @Id
    private String id;

    @Length(min = 256, max = 512)
    private String secret;

    @NotBlank
    private String application;

    @NotNull
    @Positive
    private int id_username;
}
