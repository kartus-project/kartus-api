package com.kartus.api.domain.client.dto;

import java.util.Map;

public record ClientManifestDTO(
        String version,
        Map<String, PlatformDTO> platforms
) {
    public record PlatformDTO(
            String signature,
            String fileName
    ) {
    }
}
