package com.cdtphuhoi.oun_de_de.utils;

import lombok.experimental.UtilityClass;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

@UtilityClass
public class Utils {
    public static String generateShortString(int length) {
        if (length > 36) {
            throw new IllegalArgumentException("Should be shorter than uuid length");
        }
        return UUID.randomUUID().toString().substring(0, length);
    }

    public static String randomNumericString(int length) {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(ThreadLocalRandom.current().nextInt(0, 10));
        }
        return sb.toString();
    }

    public static Optional<UUID> toUUID(String value) {
        try {
            return Optional.of(UUID.fromString(value));
        } catch (IllegalArgumentException e) {
            return Optional.empty();
        }
    }

    public static String paddingZero(BigInteger value, int paddingLength) {
        var s = value.toString();
        return s.length() < paddingLength
            ? "0".repeat(paddingLength - s.length()) + s
            : s;
    }

    public static LocalDateTime cambodiaNow() {
        return  LocalDateTime.now(ZoneId.of("Asia/Phnom_Penh"));
    }
}
