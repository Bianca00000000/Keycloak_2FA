package spring.framework._2fa_application_.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "google")
@Data
public class CustomProperties {
    private String google_application_credentials;
}