package com.kartus.api.domain.auth.dto.response;

public record AuthResponseDTO(
        String accessToken,
        String refreshToken
) {
}
