package com.kartus.api.domain.track.dto.response;

import java.util.List;

public record TrackSummaryListDTO(
        List<TrackSummaryDTO> tracks
) {
}
