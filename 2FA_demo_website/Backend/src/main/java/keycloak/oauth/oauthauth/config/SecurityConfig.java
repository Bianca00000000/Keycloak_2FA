package keycloak.oauth.oauthauth.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.SecurityFilterChain;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

// I was inspired by quickstarts and https://www.youtube.com/watch?v=oWUGe-Z0WF0&list=PLHXvj3cRjbzs8TaT-RX1qJYYK2MjRro-P&index=9
// and from https://github.com/ali-bouali/spring-boot-3-jwt-security

@Configuration
@EnableWebSecurity
public class SecurityConfig {

        @Value("${spring.security.oauth2.resourceserver.jwt.jwk-set-uri}")
        String jwkSetUri;
        private static final String[] WHITE_LIST_URL = {
                        "spi/v1/auth/**"
        };

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
                http
                                .csrf(AbstractHttpConfigurer::disable)
                                .authorizeHttpRequests(request -> request.requestMatchers(WHITE_LIST_URL)
                                                .permitAll()
                                                .anyRequest()
                                                .authenticated())
                                .sessionManagement(session -> session.sessionCreationPolicy(STATELESS))
                                .oauth2ResourceServer(oauth2 -> oauth2
                                                .jwt(jwt -> jwt.decoder(jwtDecoder())));

                return http.build();
        }

        @Bean
        JwtDecoder jwtDecoder() {
                return NimbusJwtDecoder.withJwkSetUri(this.jwkSetUri).build();
        }
}
