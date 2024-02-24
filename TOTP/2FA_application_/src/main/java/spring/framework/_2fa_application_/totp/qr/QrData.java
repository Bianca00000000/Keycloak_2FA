package spring.framework._2fa_application_.totp.qr;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class QrData {
    private final String shared_secret;
    private final int nr_digits; // cate cifre va avea codul OTP
    private final String algorithm; // algoritmul folosit in generarea codului OTP - HMAC-SHA256 sau 512
    private final int time; // perioada de valabilitate a codului OTP
    private final int imageSize = 550; // dimensiunea imaginii cu QrCode
    private final String type = "totp"; // tipul de autentificare
    private final String label;
}
