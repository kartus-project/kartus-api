package com.kartus.api.domain.track.controller;

import com.kartus.api.domain.track.dto.response.TrackSummaryListDTO;
import com.kartus.api.domain.track.service.TrackService;
import com.kartus.api.global.dto.GlobalApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/tracks")
@RequiredArgsConstructor
public class TrackController {
    private final TrackService trackService;

    @GetMapping
    public ResponseEntity<GlobalApiResponse<TrackSummaryListDTO>> getTrackSummaryList() {
        return ResponseEntity.ok(GlobalApiResponse.success(trackService.getTrackSummaryList()));
    }
}
