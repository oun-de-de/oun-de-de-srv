package com.cdtphuhoi.oun_de_de.utils;

import com.cdtphuhoi.oun_de_de.services.auth.dto.UserDetailsImpl;
import lombok.experimental.UtilityClass;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import java.util.Optional;
import java.util.function.Function;

@UtilityClass
public class SecurityContextUtils {

    /**
     * Get the property of current signed-in usr
     *
     * @return current signed-in usr property
     */
    public static <T> T getCurrentUserProperty(Function<UserDetailsImpl, T> userResolver) {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        return Optional.ofNullable(authentication)
            .map(Authentication::getPrincipal)
            .filter(UserDetailsImpl.class::isInstance)
            .map(UserDetailsImpl.class::cast)
            .map(userResolver)
            .orElse(null);
    }
}
