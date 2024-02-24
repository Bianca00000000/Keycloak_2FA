package spring.framework._2fa_application_.totp.codeOTP;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CodeDataDTO {
    
    @Pattern(regexp = "^(HMacSHA256|HMacSHA512)$")
    private String algorithm;

    @Min(6)
    @Max(8)
    private Integer nrDigits;

    @Min(30)
    @Max(50)
    private Integer time;
}

