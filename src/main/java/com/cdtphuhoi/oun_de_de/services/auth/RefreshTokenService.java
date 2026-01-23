package com.cdtphuhoi.oun_de_de.services.auth;

import com.cdtphuhoi.oun_de_de.configs.properties.JwtProperties;
import com.cdtphuhoi.oun_de_de.entities.RefreshToken;
import com.cdtphuhoi.oun_de_de.exceptions.ForbiddenException;
import com.cdtphuhoi.oun_de_de.repositories.RefreshTokenRepository;
import com.cdtphuhoi.oun_de_de.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.time.Instant;
import java.util.UUID;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final UserRepository userRepository;

    private final RefreshTokenRepository refreshTokenRepository;

    private final JwtProperties jwtProperties;

    public RefreshToken createRefreshToken(String userId) {
        var usr = userRepository.findById(userId)
            .orElseThrow(() -> new UsernameNotFoundException("User does not exist"));

        var refreshToken = RefreshToken.builder()
            .user(usr)
            .expiryDate(Instant.now().plusMillis(jwtProperties.getRefreshExpiration().toMillis()))
            .token(UUID.randomUUID().toString())
            .build();

        return refreshTokenRepository.save(refreshToken);
    }

    public RefreshToken findAndValidateByToken(@NotBlank String refreshToken) {
        var rfToken = refreshTokenRepository.findByToken(refreshToken)
            .orElseThrow(() -> new ForbiddenException(
                String.format("Failed for [%s]: Refresh token is invalid!", refreshToken)
            ));
        validateRefreshToken(rfToken);
        return rfToken;
    }

    private void validateRefreshToken(@NotNull RefreshToken refreshToken) {
        if (refreshToken.getExpiryDate().compareTo(Instant.now()) < 0) {
            refreshTokenRepository.delete(refreshToken);
            throw new ForbiddenException(
                String.format(
                    "Refresh token [%s] was expired. Please make a new sign-in request",
                    refreshToken.getToken()
                )
            );
        }
    }

    public void delete(String refreshToken) {
        var rfToken = findAndValidateByToken(refreshToken);
        refreshTokenRepository.delete(rfToken);
    }
}
