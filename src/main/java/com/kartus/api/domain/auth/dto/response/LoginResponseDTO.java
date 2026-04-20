package com.kartus.api.domain.auth.dto.response;

public record LoginResponseDTO(
        String nickname,
        String accessToken,
        String refreshToken
) {
}
