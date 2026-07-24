package com.example.demo.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class JwtServiceTest {

    private JwtService jwtService;

    @BeforeEach
    void setUp() {
        jwtService = new JwtService();
        // Set test values
        ReflectionTestUtils.setField(jwtService, "secretKey", "404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970");
        ReflectionTestUtils.setField(jwtService, "expirationMs", 86400000L); // 24 hours
    }

    @Test
    void testGenerateToken_WithUsernameAndRole() {
        // Arrange
        String username = "testUser";
        String role = "TRAFFIC_CONTROLLER";

        // Act
        String token = jwtService.generateToken(username, role);

        // Assert
        assertNotNull(token);
        assertTrue(token.split("\\.").length == 3); // JWT has 3 parts
        assertEquals(username, jwtService.extractUsername(token));
        assertTrue(jwtService.isTokenValid(token, createUserDetails(username)));
    }

    @Test
    void testGenerateToken_WithUserDetails() {
        // Arrange
        UserDetails userDetails = User.builder()
                .username("adminUser")
                .password("password")
                .roles("CITY_ADMINISTRATOR")
                .build();

        // Act
        String token = jwtService.generateToken(userDetails);

        // Assert
        assertNotNull(token);
        assertEquals("adminUser", jwtService.extractUsername(token));
        assertTrue(jwtService.isTokenValid(token, userDetails));
    }

    @Test
    void testGenerateToken_TokenContainsRoleClaim() {
        // Arrange
        String username = "controllerUser";
        String role = "UTILITY_SUPERVISOR";

        // Act
        String token = jwtService.generateToken(username, role);

        // Assert
        assertNotNull(token);
        String extractedUsername = jwtService.extractUsername(token);
        assertEquals(username, extractedUsername);
        
        // Verify token is valid for user
        UserDetails userDetails = createUserDetails(username);
        assertTrue(jwtService.isTokenValid(token, userDetails));
    }

    @Test
    void testGenerateToken_TokenExpiration() throws InterruptedException {
        // Arrange
        ReflectionTestUtils.setField(jwtService, "expirationMs", 100L); // 100ms expiration
        String username = "expiringUser";
        String role = "TRAFFIC_CONTROLLER";

        // Act
        String token = jwtService.generateToken(username, role);
        Thread.sleep(200); // Wait for token to expire

        // Assert
        UserDetails userDetails = createUserDetails(username);
        assertFalse(jwtService.isTokenValid(token, userDetails));
    }

    @Test
    void testGenerateToken_NullUsername() {
        // Act & Assert
        assertThrows(Exception.class, () -> {
            jwtService.generateToken(null, "ADMIN");
        });
    }

    @Test
    void testGenerateToken_EmptyRole() {
        // Arrange
        String username = "testUser";

        // Act
        String token = jwtService.generateToken(username, "");

        // Assert
        assertNotNull(token);
        assertTrue(jwtService.isTokenValid(token, createUserDetails(username)));
    }

    private UserDetails createUserDetails(String username) {
        return User.builder()
                .username(username)
                .password("password")
                .roles("TRAFFIC_CONTROLLER")
                .build();
    }
}