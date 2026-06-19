package com.kartus.api.domain.room.controller;

import com.kartus.api.domain.room.dto.request.RoomCreateRequestDTO;
import com.kartus.api.domain.room.dto.request.RoomTrackUpdateRequestDTO;
import com.kartus.api.domain.room.dto.response.RoomCreateResponseDTO;
import com.kartus.api.domain.room.dto.response.RoomJoinResponseDTO;
import com.kartus.api.domain.room.dto.response.RoomSummaryListDTO;
import com.kartus.api.domain.room.dto.response.RoomTrackUpdateResponseDTO;
import com.kartus.api.domain.room.service.RoomService;
import com.kartus.api.global.dto.GlobalApiResponse;
import com.kartus.api.global.security.UserPrincipal;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/rooms")
@RequiredArgsConstructor
public class RoomController {
    private final RoomService roomService;

    @PostMapping
    public ResponseEntity<GlobalApiResponse<RoomCreateResponseDTO>> create(
            @AuthenticationPrincipal UserPrincipal principal,
            @Valid @RequestBody RoomCreateRequestDTO dto
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(GlobalApiResponse.success(roomService.create(principal.getUserId(), dto)));
    }

    @GetMapping
    public ResponseEntity<GlobalApiResponse<RoomSummaryListDTO>> getRoomList() {
        return ResponseEntity.ok(GlobalApiResponse.success(roomService.getRoomList()));
    }

    @PostMapping("{roomId}/join")
    public ResponseEntity<GlobalApiResponse<RoomJoinResponseDTO>> join(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable String roomId
    ) {
        return ResponseEntity.ok(GlobalApiResponse.success(roomService.join(principal.getUserId(), roomId)));
    }

    @PatchMapping("{roomId}/track")
    public ResponseEntity<GlobalApiResponse<RoomTrackUpdateResponseDTO>> updateTrack(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable String roomId,
            @Valid @RequestBody RoomTrackUpdateRequestDTO dto
    ) {
        return ResponseEntity.ok(GlobalApiResponse.success(
                roomService.updateTrack(principal.getUserId(), roomId, dto.trackId())));
    }

    @DeleteMapping("{roomId}/members/me")
    public ResponseEntity<GlobalApiResponse<Void>> leave(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable String roomId
    ) {
        roomService.leave(principal.getUserId(), roomId);
        return ResponseEntity.ok(GlobalApiResponse.success("방에서 퇴장했습니다."));
    }
}
