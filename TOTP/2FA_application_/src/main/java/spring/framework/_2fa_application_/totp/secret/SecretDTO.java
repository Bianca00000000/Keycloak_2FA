package spring.framework._2fa_application_.totp.secret;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SecretDTO {
    @Length(min = 256, max = 512)
    private String secret;

    @NotBlank
    private String application;

    @NotNull
    @Positive
    private int id_username;
}
