package com.kartus.api.domain.track.service;

import com.kartus.api.domain.track.dto.query.TrackIdHashDTO;
import com.kartus.api.domain.track.dto.response.TrackSummaryDTO;
import com.kartus.api.domain.track.dto.response.TrackSummaryListDTO;
import com.kartus.api.domain.track.repository.TrackRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TrackService {
    private final TrackRepository trackRepository;

    public TrackSummaryListDTO getTrackSummaryList() {
        List<TrackIdHashDTO> trackIdHashes = trackRepository.findAllIdAndHash();

        return new TrackSummaryListDTO(
                trackIdHashes.stream().map(t ->
                        new TrackSummaryDTO(t.id(), t.hash())
                ).toList()
        );
    }
}
