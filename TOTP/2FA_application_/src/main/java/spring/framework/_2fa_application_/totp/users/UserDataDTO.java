package spring.framework._2fa_application_.totp.users;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.checkerframework.common.aliasing.qual.Unique;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDataDTO {

    @NotBlank
    @Unique
    private String username;

    @NotBlank
    private String password;
}
