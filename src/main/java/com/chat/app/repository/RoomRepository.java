package com.chat.app.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.chat.app.entity.Room;

public interface RoomRepository extends MongoRepository<Room, String> {

	Optional<Room> findByRoomId(String roomId);
}
