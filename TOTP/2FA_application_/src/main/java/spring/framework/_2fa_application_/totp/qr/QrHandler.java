package spring.framework._2fa_application_.totp.qr;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import spring.framework._2fa_application_.totp.exceptions.QrGenerationException;

@Component
@RequiredArgsConstructor
public class QrHandler {

        private final QRCodeGeneratorService qrCodeGeneratorService;

        public Mono<ServerResponse> getQrCode(ServerRequest request) {
                return request.bodyToMono(QrData.class) // Parsing QrData from the request body
                                .flatMap(qrData -> Mono
                                                .fromCallable(() -> qrCodeGeneratorService.generateQrCode(qrData)))
                                .flatMap(imageBytes -> ServerResponse.ok()
                                                .contentType(MediaType.IMAGE_PNG)
                                                .body(BodyInserters.fromValue(imageBytes)))
                                .onErrorResume(QrGenerationException.class, ex -> ServerResponse.badRequest()
                                                .bodyValue("Failed to generate QR code: " + ex.getMessage()));
        }
}
