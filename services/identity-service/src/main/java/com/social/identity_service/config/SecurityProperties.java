package com.social.identity_service.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Data
@Configuration
@ConfigurationProperties(prefix = "app.security")
public class SecurityProperties {

    private Jwt jwt = new Jwt();
    private Token token = new Token();
    private Cors cors = new Cors();

    @Data
    public static class Jwt {
        private String issuerUri = "http://localhost:8081";
        private String publicKey;
        private String privateKey;
        private String keyId;
    }

    @Data
    public static class Token {
        private long accessTokenValiditySeconds = 3600;
        private long refreshTokenValiditySeconds = 604800;
    }

    @Data
    public static class Cors {
        private List<String> allowedOrigins = List.of(
                "http://localhost:3000",
                "http://localhost:4200",
                "http://localhost:5173"
        );
    }
}
