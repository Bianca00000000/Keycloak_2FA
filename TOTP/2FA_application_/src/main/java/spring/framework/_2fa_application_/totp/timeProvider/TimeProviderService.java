package spring.framework._2fa_application_.totp.timeProvider;

import reactor.core.publisher.Mono;

public interface TimeProviderService {
    Mono<String> getTime();
}
