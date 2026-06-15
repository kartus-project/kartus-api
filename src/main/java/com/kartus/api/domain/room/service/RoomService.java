package com.kartus.api.domain.room.service;

import com.kartus.api.domain.room.dto.request.RoomCreateRequestDTO;
import com.kartus.api.domain.room.dto.response.RoomCreateResponseDTO;
import com.kartus.api.domain.room.dto.response.RoomJoinResponseDTO;
import com.kartus.api.domain.room.dto.response.RoomSummaryDTO;
import com.kartus.api.domain.room.dto.response.RoomSummaryListDTO;
import com.kartus.api.domain.room.dto.response.RoomTrackUpdateResponseDTO;
import com.kartus.api.domain.room.entity.Room;
import com.kartus.api.domain.room.error.RoomErrorCode;
import com.kartus.api.domain.room.repository.RoomMemberRepository;
import com.kartus.api.domain.room.repository.RoomRepository;
import com.kartus.api.domain.track.entity.Track;
import com.kartus.api.domain.track.error.TrackErrorCode;
import com.kartus.api.domain.track.repository.TrackRepository;
import com.kartus.api.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RoomService {
    private final RoomRepository roomRepository;
    private final RoomMemberRepository roomMemberRepository;
    private final TrackRepository trackRepository;

    public RoomCreateResponseDTO create(Long ownerId, RoomCreateRequestDTO dto) {
        Long defaultTrackId = trackRepository.findFirstByOrderByIdAsc()
                .map(Track::getId)
                .orElseThrow(() -> new CustomException(TrackErrorCode.TRACK_NOT_FOUND));

        String roomId = UUID.randomUUID().toString();
        Room room = Room.create(roomId, ownerId, dto.title(), dto.maxPlayer(), defaultTrackId);

        roomMemberRepository.join(roomId, ownerId.toString());
        room.syncPlayerCount(roomMemberRepository.count(roomId));
        roomRepository.save(room);

        return new RoomCreateResponseDTO(roomId, room.getTitle(), room.getMaxPlayer(), room.getTrackId());
    }

    public RoomSummaryListDTO getRoomList() {
        List<RoomSummaryDTO> rooms = new ArrayList<>();
        roomRepository.findAll().forEach(r -> rooms.add(new RoomSummaryDTO(
                r.getId(),
                r.getTitle(), 
                r.getCurrentPlayer(),
                r.getMaxPlayer(),
                r.getTrackId()
            )));
        return new RoomSummaryListDTO(rooms);
    }

    public RoomJoinResponseDTO join(Long userId, String roomId) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new CustomException(RoomErrorCode.ROOM_NOT_FOUND));

        String userKey = userId.toString();
        if (roomMemberRepository.isMember(roomId, userKey)) {
            throw new CustomException(RoomErrorCode.ALREADY_JOINED);
        }

        if (room.getCurrentPlayer() >= room.getMaxPlayer()) {
            throw new CustomException(RoomErrorCode.ROOM_FULL);
        }

        roomMemberRepository.join(roomId, userKey);
        room.syncPlayerCount(roomMemberRepository.count(roomId));
        roomRepository.save(room);

        return new RoomJoinResponseDTO(room.getId(), room.getTitle(),
                room.getCurrentPlayer(), room.getMaxPlayer(), room.getTrackId());
    }

    public RoomTrackUpdateResponseDTO updateTrack(Long userId, String roomId, Long trackId) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new CustomException(RoomErrorCode.ROOM_NOT_FOUND));

        if (!room.getOwner().equals(userId)) {
            throw new CustomException(RoomErrorCode.NOT_ROOM_OWNER);
        }
        if (!trackRepository.existsById(trackId)) {
            throw new CustomException(TrackErrorCode.TRACK_NOT_FOUND);
        }

        room.changeTrack(trackId);
        roomRepository.save(room);

        return new RoomTrackUpdateResponseDTO(roomId, trackId);
    }

    public void leave(Long userId, String roomId) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new CustomException(RoomErrorCode.ROOM_NOT_FOUND));

        String userKey = userId.toString();
        if (!roomMemberRepository.isMember(roomId, userKey)) {
            throw new CustomException(RoomErrorCode.NOT_A_MEMBER);
        }

        roomMemberRepository.leave(roomId, userKey);
        long remaining = roomMemberRepository.count(roomId);

        if (remaining <= 0) {
            roomMemberRepository.deleteRoom(roomId);
            roomRepository.deleteById(roomId);
            return;
        }

        if (room.getOwner().equals(userId)) {
            roomMemberRepository.getMembers(roomId).stream().findFirst()
                    .map(Long::valueOf).ifPresent(room::changeOwner);
        }

        room.syncPlayerCount(remaining);
        roomRepository.save(room);
    }
}
