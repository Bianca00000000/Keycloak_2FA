package spring.framework._2fa_application_.totp.verifyCode;


import reactor.core.publisher.Mono;

public interface OTPCodeValidator {
    Mono<Boolean> isValidOtp(String code, String key, String username);
}
