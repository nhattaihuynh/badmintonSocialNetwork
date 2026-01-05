package com.social.identity_service.testhelper;

import com.social.identity_service.dto.LoginRequest;
import com.social.identity_service.dto.UserRegistrationRequest;
import com.social.identity_service.entity.User;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.UUID;

public final class UserTestHelper {

    private UserTestHelper() {
    }

    public static User makeUserForSaving(String username, String email, String password, boolean enabled, PasswordEncoder passwordEncoder) {
        return User.builder()
                .id(UUID.randomUUID())
                .username(username)
                .email(email)
                .password(passwordEncoder.encode(password))
                .enabled(enabled)
                .build();
    }

    public static User makeEnabledUser(String username, String email, String password, PasswordEncoder passwordEncoder) {
        return makeUserForSaving(username, email, password, true, passwordEncoder);
    }

    public static User makeDisabledUser(String username, String email, String password, PasswordEncoder passwordEncoder) {
        return makeUserForSaving(username, email, password, false, passwordEncoder);
    }

    public static UserRegistrationRequest prepareUserRegistrationRequest(String username, String email, String password) {
        return UserRegistrationRequest.builder()
                .username(username)
                .email(email)
                .password(password)
                .build();
    }

    public static LoginRequest prepareLoginRequest(String username, String password) {
        return LoginRequest.builder()
                .username(username)
                .password(password)
                .build();
    }
}
