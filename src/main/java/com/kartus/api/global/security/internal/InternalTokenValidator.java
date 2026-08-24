package com.kartus.api.global.security.internal;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Component
public class InternalTokenValidator {
    private static final int MIN_TOKEN_LENGTH = 32;
    private static final String HASH_ALGORITHM = "SHA-256";

    private final byte[] expectedHash;

    public InternalTokenValidator(@Value("${internal.auth.token}") String token) {
        if (!StringUtils.hasText(token)) {
            throw new IllegalStateException("internal.auth.token must not be blank. Set INTERNAL_TOKEN in .env");
        }

        if (token.length() < MIN_TOKEN_LENGTH) {
            throw new IllegalStateException("internal.auth.token must be at least " + MIN_TOKEN_LENGTH + " characters");
        }

        this.expectedHash = hash(token);
    }
    
    public boolean matches(String presentedToken) {
        if (presentedToken == null || presentedToken.isEmpty()) {
            return false;
        }

        return MessageDigest.isEqual(expectedHash, hash(presentedToken));
    }

    private static byte[] hash(String value) {
        try {
            return MessageDigest.getInstance(HASH_ALGORITHM).digest(value.getBytes(StandardCharsets.UTF_8));
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(HASH_ALGORITHM + " algorithm is not available", e);
        }
    }
}
