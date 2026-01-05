package com.social.identity_service.controller;

import com.nimbusds.jose.jwk.JWKSet;
import com.social.identity_service.config.RsaKeyProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * JWKS (JSON Web Key Set) endpoint
 * Exposes public keys for other services to verify JWTs
 */
@RestController
@RequiredArgsConstructor
public class JwksController {

    private final RsaKeyProvider rsaKeyProvider;

    /**
     * Returns the public keys in JWKS format
     * Other services use this to verify JWT signatures
     */
    @GetMapping("/.well-known/jwks.json")
    public Map<String, Object> getJwks() {
        JWKSet jwkSet = new JWKSet(rsaKeyProvider.getRsaKey().toPublicJWK());
        return jwkSet.toJSONObject();
    }
}
