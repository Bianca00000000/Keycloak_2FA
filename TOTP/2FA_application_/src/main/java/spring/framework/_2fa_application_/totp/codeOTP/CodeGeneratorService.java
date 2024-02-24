package spring.framework._2fa_application_.totp.codeOTP;


import reactor.core.publisher.Mono;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public interface CodeGeneratorService {

    // vreau sa imi genereze mai multe coduri TOTP in paralel
    Mono<String> generateCode(String key, String time) throws NoSuchAlgorithmException, InvalidKeyException;
}
