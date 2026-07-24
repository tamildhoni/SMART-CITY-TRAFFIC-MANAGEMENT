package com.example.demo.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class JwtServiceTest {

    private JwtService jwtService;

    @BeforeEach
    void setUp() {
        jwtService = new JwtService();
        ReflectionTestUtils.setField(jwtService, "secretKey", "404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970");
        ReflectionTestUtils.setField(jwtService, "expirationMs", 86400000L);
    }

    @Test
    void testGenerateToken_WithUsernameAndRole() {
        String username = "testUser";
        String role = "TRAFFIC_CONTROLLER";
        String token = jwtService.generateToken(username, role);
        
        assertNotNull(token);
        assertTrue(token.split("\\.").length == 3);
        assertEquals(username, jwtService.extractUsername(token));
    }

    @Test
    void testGenerateToken_WithUserDetails() {
        UserDetails userDetails = User.builder()
                .username("adminUser")
                .password("password")
                .roles("CITY_ADMINISTRATOR")
                .build();

        String token = jwtService.generateToken(userDetails);
        
        assertNotNull(token);
        assertEquals("adminUser", jwtService.extractUsername(token));
        assertTrue(jwtService.isTokenValid(token, userDetails));
    }
}