package com.social.identity_service.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.social.identity_service.dto.LoginRequest;
import com.social.identity_service.dto.UserRegistrationRequest;
import com.social.identity_service.entity.User;
import com.social.identity_service.repository.RefreshTokenRepository;
import com.social.identity_service.repository.UserRepository;
import com.social.identity_service.testhelper.UserTestHelper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest(
        properties = {
                "spring.cloud.config.enabled=false",
                "spring.cloud.discovery.enabled=false",
                "eureka.client.enabled=false",
                "flyway.enabled=false",
                "flywaypf.active=false"
        }
)
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class AuthIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserRepository userRepository;

    @MockitoBean
    private RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ObjectMapper objectMapper;


    @Test
    public void givenValidUserData_whenRegisterUser_thenReturnsUserCreated() throws Exception {
        UserRegistrationRequest request = UserRegistrationRequest.builder()
                .username("testuser")
                .email("test@example.com")
                .password("password123")
                .build();

        User mockSavedUser = User.builder()
                .id(UUID.randomUUID())
                .username("testuser")
                .email("test@example.com")
                .password(passwordEncoder.encode("password123"))
                .enabled(true)
                .build();

        when(userRepository.existsByUsername(eq("testuser"))).thenReturn(false);
        when(userRepository.existsByEmail(eq("test@example.com"))).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(mockSavedUser);

        mockMvc.perform(post("/api/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.username").value("testuser"))
                .andExpect(jsonPath("$.email").value("test@example.com"));
    }

    @Test
    public void givenDuplicateEmail_whenRegisterUser_thenReturnsConflict() throws Exception {
        UserRegistrationRequest request = UserRegistrationRequest.builder()
                .username("newuser")
                .email("test@example.com")
                .password("password123")
                .build();

        when(userRepository.existsByUsername(eq("newuser"))).thenReturn(false);
        when(userRepository.existsByEmail(eq("test@example.com"))).thenReturn(true);

        mockMvc.perform(post("/api/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("Email already exists: test@example.com"));
    }

    @Test
    public void givenDuplicateUsername_whenRegisterUser_thenReturnsConflict() throws Exception {
        UserRegistrationRequest request = UserRegistrationRequest.builder()
                .username("testuser")
                .email("newemail@example.com")
                .password("password123")
                .build();

        when(userRepository.existsByUsername(eq("testuser"))).thenReturn(true);
        when(userRepository.existsByEmail(eq("newemail@example.com"))).thenReturn(false);

        mockMvc.perform(post("/api/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("Username already exists: testuser"));
    }

    @Test
    public void givenInvalidEmail_whenRegisterUser_thenReturnsBadRequest() throws Exception {
        UserRegistrationRequest request = UserTestHelper.prepareUserRegistrationRequest(
                "testuser", "invalid-email", "password123"
        );

        mockMvc.perform(post("/api/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void givenShortPassword_whenRegisterUser_thenReturnsBadRequest() throws Exception {
        UserRegistrationRequest request = UserTestHelper.prepareUserRegistrationRequest(
                "testuser", "test@example.com", "123"
        );

        mockMvc.perform(post("/api/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void givenShortUsername_whenRegisterUser_thenReturnsBadRequest() throws Exception {
        UserRegistrationRequest request = UserTestHelper.prepareUserRegistrationRequest(
                "ab", "test@example.com", "password123"
        );

        mockMvc.perform(post("/api/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void givenBlankUsername_whenRegisterUser_thenReturnsBadRequest() throws Exception {
        UserRegistrationRequest request = UserTestHelper.prepareUserRegistrationRequest(
                "", "test@example.com", "password123"
        );

        mockMvc.perform(post("/api/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }


    @Test
    public void givenValidCredentials_whenLogin_thenReturnsTokens() throws Exception {
        String rawPassword = "password123";
        User mockUser = UserTestHelper.makeEnabledUser(
                "loginTestUser", "logintest@example.com", rawPassword, passwordEncoder
        );

        when(userRepository.findByUsername(eq("loginTestUser"))).thenReturn(Optional.of(mockUser));

        LoginRequest loginRequest = UserTestHelper.prepareLoginRequest("loginTestUser", rawPassword);

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.access_token").isNotEmpty())
                .andExpect(jsonPath("$.refresh_token").isNotEmpty());
    }

    @Test
    public void givenWrongPassword_whenLogin_thenReturnsUnauthorized() throws Exception {
        String correctPassword = "correctPassword";
        User mockUser = UserTestHelper.makeEnabledUser(
                "wrongPasswordUser", "wrongpassword@example.com", correctPassword, passwordEncoder
        );

        when(userRepository.findByUsername(eq("wrongPasswordUser"))).thenReturn(Optional.of(mockUser));

        LoginRequest loginRequest = UserTestHelper.prepareLoginRequest("wrongPasswordUser", "wrongPassword");

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void givenNonExistentUser_whenLogin_thenReturnsUnauthorized() throws Exception {
        LoginRequest request = UserTestHelper.prepareLoginRequest("nonexistentuser", "password123");

        when(userRepository.findByUsername(eq("nonexistentuser"))).thenReturn(Optional.empty());

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void givenDisabledUser_whenLogin_thenReturnsUnauthorized() throws Exception {
        String password = "password123";
        LoginRequest request = UserTestHelper.prepareLoginRequest("disableduser", password);

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized());
    }


    @Test
    public void givenJwksEndpoint_whenGetJwks_thenReturnsJwksSuccessfully() throws Exception {
        mockMvc.perform(get("/.well-known/jwks.json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.keys").isArray())
                .andExpect(jsonPath("$.keys[0].kty").value("RSA"))
                .andExpect(jsonPath("$.keys[0].kid").isNotEmpty());
    }
}
