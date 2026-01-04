package com.social.identity_service.controller;

import com.social.identity_service.dto.LoginRequest;
import com.social.identity_service.dto.RefreshTokenRequest;
import com.social.identity_service.dto.TokenResponse;
import com.social.identity_service.security.JwtProvider;
import com.social.identity_service.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final JwtProvider jwtProvider;


    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(
            @RequestBody LoginRequest request,
            HttpServletRequest httpRequest) {
        String deviceInfo = httpRequest.getHeader("User-Agent");
        TokenResponse response = authService.login(request, deviceInfo);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/refresh")
    public ResponseEntity<TokenResponse> refresh(
            @RequestBody RefreshTokenRequest request,
            HttpServletRequest httpRequest) {
        String deviceInfo = httpRequest.getHeader("User-Agent");
        TokenResponse response = authService.refreshAccessToken(request.getRefreshToken(), deviceInfo);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<Map<String, String>> logout(@RequestBody RefreshTokenRequest request) {
        String tokenId = jwtProvider.extractTokenId(request.getRefreshToken());
        authService.revokeToken(tokenId);
        return ResponseEntity.ok(Map.of("message", "Logged out successfully"));
    }

    @PostMapping("/logout-all")
    public ResponseEntity<Map<String, String>> logoutAll(@AuthenticationPrincipal Jwt jwt) {
        String username = jwt.getSubject();
        authService.revokeAllTokens(username);
        return ResponseEntity.ok(Map.of("message", "Logged out from all devices successfully"));
    }

    /**
     * JWKS endpoint - provides public keys for token verification.
     * Other services call this endpoint to get the public key.
     */
    @GetMapping("/jwks")
    public ResponseEntity<Map<String, Object>> jwks() {
        return ResponseEntity.ok(jwtProvider.getJwks());
    }
}
