package com.chat.app.controller;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.chat.app.constants.AppConstants;
import com.chat.app.entity.Message;
import com.chat.app.entity.Room;
import com.chat.app.service.RoomService;

@RestController
@RequestMapping("/api/v1/rooms")
@CrossOrigin(AppConstants.FRONT_END_URL)
public class RoomController {

    private static final Logger logger = LoggerFactory.getLogger(RoomController.class);

    @Autowired
    RoomService roomService;

    @PostMapping
    public ResponseEntity<?> createRoom(@RequestBody String roomId) {
        logger.info("Attempting to create room with roomId: {}", roomId);

        Optional<Room> room = roomService.findByRoomId(roomId);

        if (room.isPresent()) {
            logger.warn("Room with roomId: {} already exists", roomId);
            return ResponseEntity.badRequest().body("Room Already Exists");
        } else {
            Room newRoom = new Room();
            newRoom.setRoomId(roomId);
            Room savedRoom = roomService.saveRoom(newRoom);
            logger.info("Room created successfully with roomId: {}", roomId);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedRoom);
        }
    }

    @GetMapping("/{roomId}")
    public ResponseEntity<?> joinRoom(@PathVariable("roomId") String roomId) {
        logger.info("Attempting to join room with roomId: {}", roomId);

        Optional<Room> room = roomService.findByRoomId(roomId);
        if (room.isEmpty()) {
            logger.error("Room with roomId: {} not found", roomId);
            return ResponseEntity.badRequest().body("Room not Found");
        } else {
            logger.info("Room with roomId: {} found", roomId);
            return ResponseEntity.ok(room);
        }
    }

    @GetMapping("/{roomId}/messages")
    public ResponseEntity<List<Message>> getMessages(@PathVariable("roomId") String roomId,
                                                      @RequestParam(value = "page", defaultValue = "0", required = false) int page,
                                                      @RequestParam(value = "size", defaultValue = "20", required = false) int size) {
        logger.info("Fetching messages for roomId: {} with page: {} and size: {}", roomId, page, size);

        Optional<Room> room = roomService.findByRoomId(roomId);

        if (room.isEmpty()) {
            logger.error("Room with roomId: {} not found", roomId);
            return ResponseEntity.badRequest().build();
        }

        List<Message> messages = room.get().getMessages();

        int start = Math.max(0, messages.size() - (page + 1) * size);
        int end = Math.min(messages.size(), start + size);

        List<Message> paginatedMessages = messages.subList(start, end);
        logger.info("Fetched {} messages for roomId: {}", paginatedMessages.size(), roomId);
        return ResponseEntity.ok(paginatedMessages);
    }
}
