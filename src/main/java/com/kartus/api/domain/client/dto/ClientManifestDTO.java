package com.kartus.api.domain.client.dto;

import java.time.Instant;
import java.util.Map;

public record ClientManifestDTO(
        String version,
        Instant pubDate,
        Map<String, PlatformDTO> platforms
) {
    public record PlatformDTO(
            String signature,
            String fileName
    ) {
    }
}
