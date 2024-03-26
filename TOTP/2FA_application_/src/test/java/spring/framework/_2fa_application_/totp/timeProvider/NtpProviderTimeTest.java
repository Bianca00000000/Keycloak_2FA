package spring.framework._2fa_application_.totp.timeProvider;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.event.annotation.BeforeTestClass;
import reactor.test.StepVerifier;

@SpringBootTest
@RequiredArgsConstructor
class NtpProviderTimeTest {

    @Autowired
    NtpProviderTimeServiceImpl ntpSync;

    @BeforeTestClass
    public static void setUpClass() {
        System.setProperty("GOOGLE_APPLICATION_CREDENTIALS",
                "here you must add the path to the json file with the credentials from the service-account");
    }

    @Test
    void getTime() {
        StepVerifier.create(ntpSync.getTime())
                .expectNextMatches(time -> {
                    System.out.println("Synchronized time: " + time);
                    return time.matches("\\d+"); // check if it is integer
                })
                .verifyComplete();
    }
}
