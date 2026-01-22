package com.cdtphuhoi.oun_de_de.controllers;

import com.cdtphuhoi.oun_de_de.controllers.dto.requests.SignUpRequest;
import com.cdtphuhoi.oun_de_de.controllers.dto.requests.SignInRequest;
import com.cdtphuhoi.oun_de_de.controllers.dto.responses.JwtResponse;
import com.cdtphuhoi.oun_de_de.services.auth.JwtService;
import com.cdtphuhoi.oun_de_de.services.auth.UserDetailsServiceImpl;
import com.cdtphuhoi.oun_de_de.services.auth.dto.SignUpData;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@RequestMapping("/auth")
public class AuthController {

    private final JwtService jwtService;

    private final AuthenticationManager authenticationManager;

    private final UserDetailsServiceImpl userDetailsService;

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
        return ResponseEntity.ok(
            JwtResponse.builder()
                .token(jwt)
                .build()
        );
    }

    @PostMapping("/sign-up")
    public ResponseEntity<String> register(
        @Valid @RequestBody SignUpRequest request) {
        userDetailsService.signUp(
            SignUpData.builder()
                .username(request.getUsername())
                .password(request.getPassword())
                .reEnteredPassword(request.getReEnteredPassword())
                .build()
        );
        return ResponseEntity.ok("Register successful");
    }

}