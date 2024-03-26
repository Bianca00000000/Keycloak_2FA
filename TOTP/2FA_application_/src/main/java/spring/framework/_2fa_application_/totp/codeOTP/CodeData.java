package spring.framework._2fa_application_.totp.codeOTP;

import com.google.cloud.spring.data.firestore.Document;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collectionName = "codes")
public class CodeData {

    @Id
    private String id;

    @Pattern(regexp = "^(HMacSHA256|HMacSHA512)$")
    private String algorithm;

    @Min(6)
    @Max(8)
    private Integer nrDigits; // the number of digits that the OTP code will have

    @Min(30)
    @Max(50)
    private Integer time; // the lifetime of the OTP
}
