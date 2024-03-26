package spring.framework._2fa_application_.totp.qr;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.event.annotation.BeforeTestClass;
import org.springframework.test.web.reactive.server.WebTestClient;
import spring.framework._2fa_application_.totp.codeOTP.AlgorithmHash;
import spring.framework._2fa_application_.totp.exceptions.QrGenerationException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@AutoConfigureWebTestClient
@SpringBootTest
class QRCodeGeneratorServiceImplTest {

    @Autowired
    private QRCodeGeneratorServiceImpl service;

    @Autowired
    private WebTestClient webTestClient;

    @BeforeTestClass
    public static void setUpClass() {
        System.setProperty("GOOGLE_APPLICATION_CREDENTIALS",
                "here you must add the path to the json file with the credentials from the service-account");
    }

    @Test
    void testGetUri() {
        QrData data = new QrData.QrDataBuilder()
                .label("example@example.com")
                .shared_secret("the-secret-here")
                .algorithm(AlgorithmHash.HMacSHA256.toString())
                .nr_digits(6)
                .time(30)
                .build();

        String resultUri = service.getUri(data);

        assertEquals(resultUri,
                "otpauth://totp/example%40example.com?secret=the-secret-here&digits=6&algorithm=HMacSHA256&time=30");
    }

    @Test
    public void testNullFieldUri() {
        QrData data = new QrData.QrDataBuilder()
                .label(null)
                .shared_secret(null)
                .algorithm(AlgorithmHash.HMacSHA256.toString())
                .nr_digits(6)
                .time(30)
                .build();

        String resultUri = service.getUri(data);

        assertEquals(resultUri, "otpauth://totp/?secret=&digits=6&algorithm=HMacSHA256&time=30");
    }

    private QrData getData() {
        return new QrData.QrDataBuilder()
                .label("example@example.com")
                .shared_secret("the-secret-here")
                .algorithm(AlgorithmHash.HMacSHA256.toString())
                .nr_digits(6)
                .time(30)
                .build();
    }

    @Test
    public void testQrImage() throws QrGenerationException, IOException {

        QrData data = getData();

        byte[] qrCodeBytes = service.generateQrCode(data);

        Path filePath = Path.of("./qr.png");
        Files.write(filePath, qrCodeBytes);
    }

    // this test will not work without a database!!!
    @Test
    public void testQrRoute() {
        webTestClient.get()
                .uri("/api/qr/totp/example@example.com")
                .accept(MediaType.IMAGE_PNG)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.IMAGE_PNG);
    }
}