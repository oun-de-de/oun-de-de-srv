package com.cdtphuhoi.oun_de_de.services.auth;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
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

    @Mock
    private JwtService jwtService;

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
    void testReIssueToken_Success() {
        var oldToken = "oldRefreshToken";
        var newToken = "newAccessToken";
        var refreshToken = mock(RefreshToken.class);
        when(refreshToken.getExpiryDate()).thenReturn(Instant.now().plusSeconds(5000));
        when(refreshTokenRepository.findByToken(oldToken)).thenReturn(Optional.of(refreshToken));
        var mockUser = mock(User.class);
        var username = "username";
        when(mockUser.getUsername()).thenReturn(username);
        when(refreshToken.getUser()).thenReturn(mockUser);
        when(jwtService.generateToken(anyString())).thenReturn(newToken);

        var result = refreshTokenService.reIssueToken(oldToken);

        assertEquals(newToken, result.getAccessToken());
        verify(refreshTokenRepository).findByToken(oldToken);
    }

    @Test
    void testReIssueToken_TokenNotFound() {
        var oldToken = "invalidToken";
        when(refreshTokenRepository.findByToken(oldToken)).thenReturn(Optional.empty());

        assertThrows(ForbiddenException.class, () -> {
            refreshTokenService.reIssueToken(oldToken);
        });
    }
}
