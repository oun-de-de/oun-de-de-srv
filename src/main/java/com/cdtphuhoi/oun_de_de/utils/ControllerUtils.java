package com.cdtphuhoi.oun_de_de.utils;

import com.cdtphuhoi.oun_de_de.entities.User;
import com.cdtphuhoi.oun_de_de.exceptions.ForbiddenException;
import com.cdtphuhoi.oun_de_de.repositories.UserRepository;
import com.cdtphuhoi.oun_de_de.services.auth.dto.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ControllerUtils {
    private final UserRepository userRepository;

    public User getCurrentSignedInUser() {
        var usrId = SecurityContextUtils.getCurrentUserProperty(UserDetailsImpl::getId);
        return userRepository.findOneById(usrId)
            .orElseThrow(
                () -> new ForbiddenException(
                    String.format("User [id=%s] not found", usrId)
                )
            );
    }
}
