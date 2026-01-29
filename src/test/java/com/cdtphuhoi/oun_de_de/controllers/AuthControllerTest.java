package com.cdtphuhoi.oun_de_de.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import com.cdtphuhoi.oun_de_de.configs.properties.JwtProperties;
import com.cdtphuhoi.oun_de_de.controllers.dto.auth.SignInRequest;
import com.cdtphuhoi.oun_de_de.controllers.dto.auth.SignUpRequest;
import com.cdtphuhoi.oun_de_de.controllers.dto.auth.TokenRefreshRequest;
import com.cdtphuhoi.oun_de_de.entities.RefreshToken;
import com.cdtphuhoi.oun_de_de.services.auth.JwtService;
import com.cdtphuhoi.oun_de_de.services.auth.RefreshTokenService;
import com.cdtphuhoi.oun_de_de.services.auth.UserDetailsServiceImpl;
import com.cdtphuhoi.oun_de_de.services.auth.dto.SignUpData;
import com.cdtphuhoi.oun_de_de.services.auth.dto.UserDetailsImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UserDetailsServiceImpl userDetailsService;

    @Mock
    private JwtService jwtService;

    @Mock
    private RefreshTokenService refreshTokenService;

    @Mock
    private JwtProperties jwtProperties;

    @InjectMocks
    private AuthController authController;

    @Test
    void testAuthenticateAndGetToken_Success() {
        var request = new SignInRequest();
        request.setUsername("user");
        request.setPassword("pass");

        var authentication = mock(Authentication.class);
        when(authentication.isAuthenticated()).thenReturn(true);
        var userDetails = mock(UserDetailsImpl.class);
        var mockUserId = UUID.randomUUID().toString();
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getId()).thenReturn(mockUserId);
        when(userDetails.getUsername()).thenReturn("user");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
        when(jwtService.generateToken("user")).thenReturn("jwt-token");
        var refreshToken = mock(RefreshToken.class);
        when(refreshToken.getToken()).thenReturn("refresh-token");
        when(refreshTokenService.createRefreshToken(mockUserId)).thenReturn(refreshToken);
        when(jwtProperties.getExpiration()).thenReturn(java.time.Duration.ofSeconds(3600));

        var response = authController.authenticateAndGetToken(request);

        assertEquals(200, response.getStatusCode().value());
        assertEquals("jwt-token", response.getBody().getAccessToken());
        assertEquals("refresh-token", response.getBody().getRefreshToken());
        assertEquals("user", response.getBody().getUsername());
        assertEquals(3600, response.getBody().getExpiresIn());
    }

    @Test
    void testAuthenticateAndGetToken_Failure() {
        var request = new SignInRequest();
        request.setUsername("user");
        request.setPassword("wrong");

        var authentication = mock(Authentication.class);
        when(authentication.isAuthenticated()).thenReturn(false);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);

        assertThrows(UsernameNotFoundException.class, () -> authController.authenticateAndGetToken(request));
    }

    @Test
    void testSignUp() {
        var request = new SignUpRequest();
        request.setUsername("newuser");
        request.setPassword("pass");
        request.setReEnteredPassword("pass");

        doNothing().when(userDetailsService).signUp(any(SignUpData.class));

        var response = authController.signUp(request);

        assertEquals(201, response.getStatusCode().value());
        assertEquals("Sign up successfully", response.getBody());
    }

    @Test
    void testSignOut() {
        var request = new TokenRefreshRequest();
        request.setRefreshToken("refresh-token");

        doNothing().when(refreshTokenService).delete("refresh-token");

        var response = authController.signOut(request);

        assertEquals(200, response.getStatusCode().value());
        assertEquals("Signed out successfully", response.getBody());
    }
}