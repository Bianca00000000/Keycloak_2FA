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
    private Integer nrDigits; // numarul de cifre pe care o sa il aibe codul OTP

    @Min(30)
    @Max(50)
    private Integer time; // durata de viata a OTP-ului
}
