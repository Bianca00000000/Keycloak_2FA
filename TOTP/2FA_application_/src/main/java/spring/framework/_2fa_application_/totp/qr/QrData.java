package spring.framework._2fa_application_.totp.qr;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class QrData {
    private final String shared_secret;
    private final int nr_digits; // how many digits will the OTP code have
    private final String algorithm; // the algorithm used to generate the OTP code - HMAC-SHA256 or 512
    private final int time; // the validity period of the OTP code
    private final int imageSize = 550; // image size with QrCode
    private final String type = "totp"; // authentication type
    private final String label;
}
