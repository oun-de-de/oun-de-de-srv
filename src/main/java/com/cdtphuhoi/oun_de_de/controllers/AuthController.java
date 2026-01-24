package com.cdtphuhoi.oun_de_de.controllers;

import static com.cdtphuhoi.oun_de_de.common.Constants.BEARER_TOKEN_TYPE;
import static com.cdtphuhoi.oun_de_de.common.Constants.SWAGGER_SECURITY_SCHEME_NAME;
import com.cdtphuhoi.oun_de_de.configs.properties.JwtProperties;
import com.cdtphuhoi.oun_de_de.controllers.dto.auth.SignInRequest;
import com.cdtphuhoi.oun_de_de.controllers.dto.auth.SignUpRequest;
import com.cdtphuhoi.oun_de_de.controllers.dto.auth.TokenRefreshRequest;
import com.cdtphuhoi.oun_de_de.controllers.dto.auth.JwtResponse;
import com.cdtphuhoi.oun_de_de.controllers.dto.auth.TokenRefreshResponse;
import com.cdtphuhoi.oun_de_de.services.auth.JwtService;
import com.cdtphuhoi.oun_de_de.services.auth.RefreshTokenService;
import com.cdtphuhoi.oun_de_de.services.auth.UserDetailsServiceImpl;
import com.cdtphuhoi.oun_de_de.services.auth.dto.SignUpData;
import com.cdtphuhoi.oun_de_de.services.auth.dto.UserDetailsImpl;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import jakarta.validation.Valid;

@Slf4j
@RestController
@RequiredArgsConstructor
@SecurityRequirement(name = SWAGGER_SECURITY_SCHEME_NAME)
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;

    private final UserDetailsServiceImpl userDetailsService;

    private final JwtService jwtService;

    private final RefreshTokenService refreshTokenService;

    private final JwtProperties jwtProperties;

    @PostMapping("/sign-in")
    public ResponseEntity<JwtResponse> authenticateAndGetToken(
        @Valid @RequestBody SignInRequest request) {
        var authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                request.getUsername(),
                request.getPassword()
            )
        );
        if (!authentication.isAuthenticated()) {
            throw new UsernameNotFoundException("Invalid user request!");
        }
        var jwt = jwtService.generateToken(request.getUsername());
        var usrDetails = ((UserDetailsImpl) authentication.getPrincipal());
        var refreshToken = refreshTokenService.createRefreshToken(
            usrDetails.getId()
        );
        return ResponseEntity.ok(
            JwtResponse.builder()
                .userId(usrDetails.getId())
                .accessToken(jwt)
                .refreshToken(refreshToken.getToken())
                .username(usrDetails.getUsername())
                .type(BEARER_TOKEN_TYPE)
                .expiresIn(jwtProperties.getExpiration().toSeconds())
                .build()
        );
    }

    @PostMapping("/sign-up")
    public ResponseEntity<String> signUp(
        @Valid @RequestBody SignUpRequest request) {
        userDetailsService.signUp(
            SignUpData.builder()
                .username(request.getUsername())
                .password(request.getPassword())
                .reEnteredPassword(request.getReEnteredPassword())
                .build()
        );
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body("Sign up successfully");
    }

    @PostMapping("/token/refresh")
    public ResponseEntity<TokenRefreshResponse> refreshToken(
        @Valid @RequestBody TokenRefreshRequest request) {
        var reIssueToken = refreshTokenService.reIssueToken(request.getRefreshToken());
        return ResponseEntity.ok(
            TokenRefreshResponse.builder()
                .accessToken(reIssueToken.getAccessToken())
                .refreshToken(reIssueToken.getRefreshToken())
                .type(BEARER_TOKEN_TYPE)
                .build()
        );
    }

    @PostMapping("/sign-out")
    public ResponseEntity<String> signOut(
        @Valid @RequestBody TokenRefreshRequest request) {
        refreshTokenService.delete(request.getRefreshToken());
        return ResponseEntity.ok("Signed out successfully");
    }

}