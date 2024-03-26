package spring.framework._2fa_application_.totp.qr;

public interface QRCodeGeneratorService {
    String getUri(QrData data); // uri formatted for 2FA authentication

    byte[] generateQrCode(QrData data);
}
