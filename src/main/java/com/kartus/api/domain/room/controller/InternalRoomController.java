package com.kartus.api.domain.room.controller;

import com.kartus.api.domain.room.service.RoomService;
import com.kartus.api.global.dto.GlobalApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("internal/rooms")
@RequiredArgsConstructor
public class InternalRoomController {
    private final RoomService roomService;

    @DeleteMapping("{roomId}/members/{userId}")
    public ResponseEntity<GlobalApiResponse<Void>> cleanupMember(
            @PathVariable String roomId,
            @PathVariable Long userId
    ) {
        roomService.cleanupMember(userId, roomId);
        return ResponseEntity.ok(GlobalApiResponse.success("방 참여 정보를 정리했습니다."));
    }
}
