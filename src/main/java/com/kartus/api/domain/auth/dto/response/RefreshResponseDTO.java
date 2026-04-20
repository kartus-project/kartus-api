package com.kartus.api.domain.auth.dto.response;

public record RefreshResponseDTO(
        String accessToken,
        String refreshToken
) {
}
