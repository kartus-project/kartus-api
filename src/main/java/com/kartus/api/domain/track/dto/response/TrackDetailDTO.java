package com.kartus.api.domain.track.dto.response;

import java.util.Map;

public record TrackDetailDTO(
        String name,
        Map<String, Object> trackData
) {
}
