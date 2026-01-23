package com.cdtphuhoi.oun_de_de.services.auth;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import com.cdtphuhoi.oun_de_de.configs.properties.JwtProperties;
import com.cdtphuhoi.oun_de_de.entities.RefreshToken;
import com.cdtphuhoi.oun_de_de.entities.User;
import com.cdtphuhoi.oun_de_de.exceptions.ForbiddenException;
import com.cdtphuhoi.oun_de_de.repositories.RefreshTokenRepository;
import com.cdtphuhoi.oun_de_de.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
class RefreshTokenServiceTest {

    @InjectMocks
    private RefreshTokenService refreshTokenService;

    @Mock
    private RefreshTokenRepository refreshTokenRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtProperties jwtProperties;

    @Test
    void createRefreshToken_ShouldReturnToken() {
        var user = new User();
        var usrId = UUID.randomUUID().toString();
        user.setId(usrId);
        when(userRepository.findById(usrId)).thenReturn(Optional.of(user));

        var token = new RefreshToken();
        token.setToken("new-token");
        token.setUser(user);
        token.setExpiryDate(Instant.now().plusSeconds(3600));
        when(refreshTokenRepository.save(any())).thenReturn(token);

        var result = refreshTokenService.createRefreshToken(usrId);
        assertNotNull(result);
        assertEquals("new-token", result.getToken());
        assertEquals(user, result.getUser());
    }

    @Test
    void deleteByToken_ShouldCallRepository() {
        var token = mock(RefreshToken.class);
        var tokenStr = "delete-token";
        when(token.getExpiryDate()).thenReturn(Instant.now().plusSeconds(5000));
        when(refreshTokenRepository.findByToken(eq(tokenStr))).thenReturn(Optional.of(token));

        refreshTokenService.delete(tokenStr);

        verify(refreshTokenRepository, times(1)).delete(token);
    }

    @Test
    void findAndValidateByToken_ShouldReturnToken_WhenNotExpired() {
        var token = new RefreshToken();
        token.setToken("valid-token");
        token.setExpiryDate(Instant.now().plusSeconds(3600));
        when(refreshTokenRepository.findByToken("valid-token")).thenReturn(Optional.of(token));

        var result = refreshTokenService.findAndValidateByToken("valid-token");
        assertNotNull(result);
        assertEquals("valid-token", result.getToken());
    }

    @Test
    void findAndValidateByToken_ShouldReturnEmpty_WhenExpired() {
        var token = new RefreshToken();
        token.setToken("expired-token");
        token.setExpiryDate(Instant.now().minusSeconds(10));
        when(refreshTokenRepository.findByToken("expired-token")).thenReturn(Optional.of(token));

        assertThrows(
            ForbiddenException.class,
            () -> refreshTokenService.findAndValidateByToken("expired-token")
        );
    }

    @Test
    void findAndValidateByToken_ShouldReturnEmpty_WhenNotFound() {
        when(refreshTokenRepository.findByToken("missing-token")).thenReturn(Optional.empty());

        assertThrows(
            ForbiddenException.class,
            () -> refreshTokenService.findAndValidateByToken("missing-token")
        );
    }
}
