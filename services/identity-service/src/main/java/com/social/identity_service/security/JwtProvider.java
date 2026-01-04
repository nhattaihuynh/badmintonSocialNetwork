package com.social.identity_service.security;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.social.identity_service.config.RsaKeyProvider;
import com.social.identity_service.config.SecurityProperties;
import com.social.identity_service.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.jose.jws.SignatureAlgorithm;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.UUID;

/**
 * JWT Provider for managing all token operations.
 * Responsibilities:
 * - Generate access tokens
 * - Generate refresh tokens
 * - Parse and validate tokens
 * - Extract claims from tokens
 * - Provide JWKS (JSON Web Key Set) for token verification
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class JwtProvider {

    private final JwtEncoder jwtEncoder;
    private final JwtDecoder jwtDecoder;
    private final SecurityProperties securityProperties;
    private final RsaKeyProvider rsaKeyProvider;

    public String generateAccessToken(User user) {
        Instant now = Instant.now();
        long validitySeconds = securityProperties.getToken().getAccessTokenValiditySeconds();
        String issuerUri = securityProperties.getJwt().getIssuerUri();

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer(issuerUri)
                .issuedAt(now)
                .expiresAt(now.plus(validitySeconds, ChronoUnit.SECONDS))
                .subject(user.getUsername())
                .id(UUID.randomUUID().toString())
                .claim("type", "access")
                .claim("scope", "openid profile read write")
                .claim("email", user.getEmail())
                .claim("userId", user.getId().toString())
                .build();

        return encodeToken(claims);
    }

    public String generateRefreshToken(User user, String tokenId) {
        Instant now = Instant.now();
        long validitySeconds = securityProperties.getToken().getRefreshTokenValiditySeconds();
        String issuerUri = securityProperties.getJwt().getIssuerUri();

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer(issuerUri)
                .issuedAt(now)
                .expiresAt(now.plus(validitySeconds, ChronoUnit.SECONDS))
                .subject(user.getUsername())
                .id(tokenId)
                .claim("type", "refresh")
                .build();

        return encodeToken(claims);
    }


    public Jwt parseToken(String token) {
        return jwtDecoder.decode(token);
    }


    public String extractTokenId(String token) {
        try {
            String[] parts = token.split("\\.");
            if (parts.length != 3) {
                throw new IllegalArgumentException("Invalid JWT format");
            }
            String payload = new String(java.util.Base64.getUrlDecoder().decode(parts[1]));
            int jtiStart = payload.indexOf("\"jti\":\"") + 7;
            int jtiEnd = payload.indexOf("\"", jtiStart);
            return payload.substring(jtiStart, jtiEnd);
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to extract token ID", e);
        }
    }


    public String extractSubject(String token) {
        Jwt jwt = parseToken(token);
        return jwt.getSubject();
    }


    public String extractTokenType(String token) {
        Jwt jwt = parseToken(token);
        return jwt.getClaimAsString("type");
    }


    public boolean isTokenExpired(String token) {
        try {
            Jwt jwt = parseToken(token);
            Instant expiration = jwt.getExpiresAt();
            return expiration != null && expiration.isBefore(Instant.now());
        } catch (JwtException e) {
            return true; // Treat invalid tokens as expired
        }
    }


    public boolean isValidAccessToken(String token) {
        try {
            Jwt jwt = parseToken(token);
            String type = jwt.getClaimAsString("type");
            return "access".equals(type) && !isTokenExpired(token);
        } catch (JwtException e) {
            return false;
        }
    }


    public boolean isValidRefreshToken(String token) {
        try {
            Jwt jwt = parseToken(token);
            String type = jwt.getClaimAsString("type");
            return "refresh".equals(type) && !isTokenExpired(token);
        } catch (JwtException e) {
            return false;
        }
    }


    public long getAccessTokenValidity() {
        return securityProperties.getToken().getAccessTokenValiditySeconds();
    }


    public long getRefreshTokenValidity() {
        return securityProperties.getToken().getRefreshTokenValiditySeconds();
    }


    public Map<String, Object> getJwks() {
        RSAKey rsaKey = new RSAKey.Builder(rsaKeyProvider.getPublicKey())
                .keyID(rsaKeyProvider.getKeyId())
                .build();
        return new JWKSet(rsaKey).toJSONObject();
    }


    private String encodeToken(JwtClaimsSet claims) {
        JwsHeader header = JwsHeader.with(SignatureAlgorithm.RS256)
                .keyId(rsaKeyProvider.getKeyId())
                .build();
        return jwtEncoder.encode(JwtEncoderParameters.from(header, claims)).getTokenValue();
    }
}
