package spring.framework._2fa_application_.totp.secret;

import reactor.core.publisher.Mono;

public interface SecretGeneratorService {
    Mono<String> generate();
}
