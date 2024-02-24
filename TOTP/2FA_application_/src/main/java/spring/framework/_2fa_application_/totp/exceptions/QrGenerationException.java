package spring.framework._2fa_application_.totp.exceptions;

public class QrGenerationException extends Exception{
    public QrGenerationException(String message, Throwable cause) {
        super(message, cause);
    }
}
