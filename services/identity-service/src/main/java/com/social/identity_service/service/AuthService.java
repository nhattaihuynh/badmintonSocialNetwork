package com.social.identity_service.service;

import com.social.identity_service.config.SecurityProperties;
import com.social.identity_service.dto.LoginRequest;
import com.social.identity_service.dto.TokenResponse;
import com.social.identity_service.entity.RefreshToken;
import com.social.identity_service.entity.User;
import com.social.identity_service.repository.RefreshTokenRepository;
import com.social.identity_service.repository.UserRepository;
import com.social.identity_service.security.JwtProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;


@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final SecurityProperties securityProperties;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtProvider jwtProvider;

    @Transactional
    public TokenResponse login(LoginRequest request, String deviceInfo) {
        log.info("Login attempt for user: {}", request.getUsername());

        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new BadCredentialsException("Invalid username or password"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("Invalid username or password");
        }

        if (!user.isEnabled()) {
            throw new BadCredentialsException("User account is disabled");
        }

        String refreshTokenId = UUID.randomUUID().toString();
        String accessToken = jwtProvider.generateAccessToken(user);
        String refreshToken = jwtProvider.generateRefreshToken(user, refreshTokenId);

        storeRefreshToken(user.getUsername(), refreshTokenId, deviceInfo);

        log.info("Login successful for user: {}", user.getUsername());

        return TokenResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .tokenType("Bearer")
                .expiresIn(jwtProvider.getAccessTokenValidity())
                .scope("openid profile read write")
                .build();
    }


    @Transactional
    public void revokeToken(String tokenId) {
        int revoked = refreshTokenRepository.revokeByTokenId(tokenId, Instant.now());
        if (revoked > 0) {
            log.info("Revoked refresh token: {}", tokenId);
        }
    }


    @Transactional
    public void revokeAllTokens(String username) {
        int revoked = refreshTokenRepository.revokeAllByUsername(username, Instant.now());
        log.info("Revoked {} refresh tokens for user: {}", revoked, username);
    }


    @Transactional
    public TokenResponse refreshAccessToken(String refreshTokenValue, String deviceInfo) {
        try {
            String tokenId = jwtProvider.extractTokenId(refreshTokenValue);

            RefreshToken storedToken = refreshTokenRepository.findByTokenIdAndRevokedFalse(tokenId)
                    .orElseThrow(() -> new BadCredentialsException("Invalid or revoked refresh token"));

            if (storedToken.isExpired()) {
                throw new BadCredentialsException("Refresh token has expired");
            }

            User user = userRepository.findByUsername(storedToken.getUsername())
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));

            if (!user.isEnabled()) {
                throw new BadCredentialsException("User account is disabled");
            }

            storedToken.revoke();
            refreshTokenRepository.save(storedToken);

            String newRefreshTokenId = UUID.randomUUID().toString();
            String accessToken = jwtProvider.generateAccessToken(user);
            String newRefreshToken = jwtProvider.generateRefreshToken(user, newRefreshTokenId);

            storeRefreshToken(user.getUsername(), newRefreshTokenId, deviceInfo);

            return TokenResponse.builder()
                    .accessToken(accessToken)
                    .refreshToken(newRefreshToken)
                    .tokenType("Bearer")
                    .expiresIn(jwtProvider.getAccessTokenValidity())
                    .scope("openid profile read write")
                    .build();

        } catch (Exception e) {
            log.error("Failed to refresh token", e);
            throw new BadCredentialsException("Invalid refresh token");
        }
    }

    private void storeRefreshToken(String username, String tokenId, String deviceInfo) {
        long refreshTokenValiditySeconds = securityProperties.getToken().getRefreshTokenValiditySeconds();

        RefreshToken refreshToken = RefreshToken.builder()
                .tokenId(tokenId)
                .username(username)
                .issuedAt(Instant.now())
                .expiresAt(Instant.now().plus(refreshTokenValiditySeconds, ChronoUnit.SECONDS))
                .deviceInfo(deviceInfo)
                .build();

        refreshTokenRepository.save(refreshToken);
    }
}
