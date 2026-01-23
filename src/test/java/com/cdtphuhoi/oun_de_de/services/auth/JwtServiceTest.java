package com.cdtphuhoi.oun_de_de.services.auth;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import com.cdtphuhoi.oun_de_de.configs.properties.JwtProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import java.time.Duration;
import java.util.Date;

@ExtendWith(MockitoExtension.class)
class JwtServiceTest {

    @InjectMocks
    private JwtService jwtService;

    @Mock
    private JwtProperties jwtProperties;

    @BeforeEach
    void setUp() {
        // Example secret: 32 bytes base64 for HS256
        when(jwtProperties.getSecret()).thenReturn("Zm9vYmFyYmF6cXV4eHl6c2VjcmV0Zm9vYmFyYmF6cssa==");
        when(jwtProperties.getExpiration()).thenReturn(Duration.ofMinutes(10));
    }

    @Test
    void generateToken_ShouldReturnValidToken() {
        var username = "testuser";
        var token = jwtService.generateToken(username);
        assertNotNull(token);
        assertFalse(token.isEmpty());
    }

    @Test
    void extractUsername_ShouldReturnCorrectUsername() {
        var username = "testuser";
        var token = jwtService.generateToken(username);
        var extracted = jwtService.extractUsername(token);
        assertEquals(username, extracted);
    }

    @Test
    void extractExpiration_ShouldReturnFutureDate() {
        var token = jwtService.generateToken("user");
        var expiration = jwtService.extractExpiration(token);
        assertTrue(expiration.after(new Date()));
    }

    @Test
    void validateToken_ShouldReturnTrueForValidToken() {
        var username = "validuser";
        var token = jwtService.generateToken(username);

        var userDetails = mock(UserDetails.class);
        when(userDetails.getUsername()).thenReturn(username);

        assertTrue(jwtService.validateToken(token, userDetails));
    }

    @Test
    void validateToken_ShouldReturnFalseForInvalidUsername() {
        var token = jwtService.generateToken("userA");

        var userDetails = mock(UserDetails.class);
        when(userDetails.getUsername()).thenReturn("userB");

        assertFalse(jwtService.validateToken(token, userDetails));
    }

    @Test
    void isTokenExpired_ShouldReturnFalseForFreshToken() {
        var token = jwtService.generateToken("user");
        assertFalse(jwtService.extractExpiration(token).before(new Date()));
    }
}
