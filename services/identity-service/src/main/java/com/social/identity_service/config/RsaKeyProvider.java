package com.social.identity_service.config;

import com.nimbusds.jose.jwk.RSAKey;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.UUID;

/**
 * Production-ready RSA Key Provider
 * Loads RSA keys from environment variables for production use.
 * If not configured, generates keys at startup (for development only).
 * For production, set these environment variables:
 * - RSA_PUBLIC_KEY: Base64 encoded public key
 * - RSA_PRIVATE_KEY: Base64 encoded private key
 * - RSA_KEY_ID: Unique key identifier (optional, generated if not set)
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class RsaKeyProvider {

    private final SecurityProperties securityProperties;

    private RSAPublicKey publicKey;
    private RSAPrivateKey privateKey;
    private String keyId;

    @PostConstruct
    public void init() {
        String publicKeyBase64 = securityProperties.getJwt().getPublicKey();
        String privateKeyBase64 = securityProperties.getJwt().getPrivateKey();
        String configuredKeyId = securityProperties.getJwt().getKeyId();

        if (publicKeyBase64 != null && !publicKeyBase64.isBlank()
                && privateKeyBase64 != null && !privateKeyBase64.isBlank()) {
            loadKeysFromBase64(publicKeyBase64, privateKeyBase64);
            this.keyId = (configuredKeyId != null && !configuredKeyId.isBlank())
                    ? configuredKeyId
                    : UUID.randomUUID().toString();
            log.info("RSA keys loaded from configuration (Production mode)");
        } else {
            log.warn("RSA keys not configured - generating new keys (Development mode only!)");
            log.warn("For production, set RSA_PUBLIC_KEY and RSA_PRIVATE_KEY environment variables");
            generateKeys();
            printKeysForConfiguration();
        }
    }

    private void loadKeysFromBase64(String publicKeyBase64, String privateKeyBase64) {
        try {
            byte[] publicKeyBytes = Base64.getDecoder().decode(publicKeyBase64);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(publicKeyBytes);
            this.publicKey = (RSAPublicKey) keyFactory.generatePublic(publicKeySpec);

            byte[] privateKeyBytes = Base64.getDecoder().decode(privateKeyBase64);
            PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(privateKeyBytes);
            this.privateKey = (RSAPrivateKey) keyFactory.generatePrivate(privateKeySpec);

        } catch (Exception e) {
            throw new IllegalStateException("Failed to load RSA keys from configuration", e);
        }
    }

    private void generateKeys() {
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(2048);
            KeyPair keyPair = keyPairGenerator.generateKeyPair();
            this.publicKey = (RSAPublicKey) keyPair.getPublic();
            this.privateKey = (RSAPrivateKey) keyPair.getPrivate();
            this.keyId = UUID.randomUUID().toString();
        } catch (Exception e) {
            throw new IllegalStateException("Failed to generate RSA key pair", e);
        }
    }

    private void printKeysForConfiguration() {
        String publicKeyBase64 = Base64.getEncoder().encodeToString(publicKey.getEncoded());
        String privateKeyBase64 = Base64.getEncoder().encodeToString(privateKey.getEncoded());

        log.info("========================================");
        log.info("GENERATED RSA KEYS - SAVE THESE FOR PRODUCTION:");
        log.info("========================================");
        log.info("RSA_PUBLIC_KEY={}", publicKeyBase64);
        log.info("RSA_PRIVATE_KEY={}", privateKeyBase64);
        log.info("RSA_KEY_ID={}", keyId);
        log.info("========================================");
        log.info("Set these as environment variables to persist keys across restarts");
        log.info("========================================");
    }

    public RSAPublicKey getPublicKey() {
        return publicKey;
    }

    public RSAPrivateKey getPrivateKey() {
        return privateKey;
    }

    public String getKeyId() {
        return keyId;
    }

    public RSAKey getRsaKey() {
        return new RSAKey.Builder(publicKey)
                .privateKey(privateKey)
                .keyID(keyId)
                .build();
    }
}
