package spring.framework._2fa_application_;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import spring.framework._2fa_application_.config.CustomProperties;


@SpringBootApplication
@ComponentScan({"spring.framework._2fa_application_.totp.codeOTP", "spring.framework._2fa_application_.totp.qr", "spring.framework._2fa_application_.totp.timeProvider", "spring.framework._2fa_application_.totp.secret"})
@EnableConfigurationProperties(CustomProperties.class)
public class Application {

    public static void main(String[] args) {

        SpringApplication.run(Application.class, args);

    }

}
