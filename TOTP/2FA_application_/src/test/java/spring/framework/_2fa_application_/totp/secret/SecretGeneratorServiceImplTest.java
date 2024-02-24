package spring.framework._2fa_application_.totp.secret;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.event.annotation.BeforeTestClass;
import reactor.test.StepVerifier;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class SecretGeneratorServiceImplTest {

    @Autowired
    SecretGeneratorServiceImpl secretGeneratorService;

    @BeforeTestClass
    public static void setUpClass() {
        System.setProperty("GOOGLE_APPLICATION_CREDENTIALS", "src/main/java/spring/framework/_2fa_application_/config/ConnectionString.json");
    }

    @Test
    void generate() {
        StepVerifier.create(secretGeneratorService.generate())
                .expectNextMatches(result -> result.matches("[0-9a-f]+"))
                .verifyComplete();
    }

    @Test
    void verifyLengthSecret() {

        String result = secretGeneratorService.generate().block();
        assertNotNull(result);
        assertEquals(64, result.length()); // pentru HMacSHA256
    }
}