package spring.framework._2fa_application_.totp.codeOTP;

import reactor.core.publisher.Mono;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public interface CodeGeneratorService {

    // I want to generate several TOTP codes in parallel
    Mono<String> generateCode(String key, String time) throws NoSuchAlgorithmException, InvalidKeyException;
}
