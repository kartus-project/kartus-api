package com.kartus.api.domain.client.dto.response;

import java.time.Instant;

public record ClientVersionResponseDTO(
        String version,
        Instant pubDate,
        String downloadUrl,
        String signature
) {
}
