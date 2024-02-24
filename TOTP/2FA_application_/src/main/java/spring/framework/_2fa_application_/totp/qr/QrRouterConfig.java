package spring.framework._2fa_application_.totp.qr;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
@RequiredArgsConstructor
public class QrRouterConfig {
    public static final String QR_PATH =  "/api/qr/{type}/{label}";
    private final QrHandler handler;

    @Bean
    public RouterFunction<ServerResponse> qrRoutes(){
        return route()
                .GET(QR_PATH, accept(MediaType.IMAGE_PNG), handler::getQrCode)
                .build();
    }
}
