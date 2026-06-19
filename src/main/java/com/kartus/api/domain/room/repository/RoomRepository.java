package com.kartus.api.domain.room.repository;

import com.kartus.api.domain.room.entity.Room;
import org.springframework.data.repository.CrudRepository;

public interface RoomRepository extends CrudRepository<Room, String> {
}
