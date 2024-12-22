package com.chat.app.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.chat.app.entity.Room;
import com.chat.app.repository.RoomRepository;

@Service
public class RoomService {

	@Autowired
	RoomRepository roomRepository;

	public Optional<Room> findByRoomId(String roomId) {
		return roomRepository.findByRoomId(roomId);
	}

	public Room saveRoom(Room room) {
		return roomRepository.save(room);
	}

}
