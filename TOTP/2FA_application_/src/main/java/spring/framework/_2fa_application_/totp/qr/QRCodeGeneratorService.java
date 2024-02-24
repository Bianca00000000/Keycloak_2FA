package spring.framework._2fa_application_.totp.qr;

import reactor.core.publisher.Mono;

public interface QRCodeGeneratorService {
    String getUri(QrData data); // uri formatat pentru autentificarea cu 2FA
    byte[] generateQrCode(QrData data);
}
