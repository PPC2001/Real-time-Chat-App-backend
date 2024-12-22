package com.chat.app.controller;

import java.time.LocalDateTime;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;

import com.chat.app.constants.AppConstants;
import com.chat.app.entity.Message;
import com.chat.app.entity.Room;
import com.chat.app.payload.MessageRequest;
import com.chat.app.service.RoomService;

@Controller
@CrossOrigin(AppConstants.FRONT_END_URL)
public class ChatController {

    private static final Logger logger = LoggerFactory.getLogger(ChatController.class);

    @Autowired
    RoomService roomService;

    @MessageMapping("/sendMessage/{roomId}")
    @SendTo("/topic/room/{roomId}")
    public Message sendMessage(@DestinationVariable("roomId") String roomId, @RequestBody MessageRequest request) {
        logger.info("Received message request for roomId: {}", roomId);

        Optional<Room> room = roomService.findByRoomId(request.getRoomId());

        Message message = new Message();
        message.setContent(request.getContent());
        message.setSender(request.getSender());
        message.setTimeStamp(LocalDateTime.now());

        if (room.isPresent()) {
            logger.info("Room found. Adding message to room: {}", roomId);
            room.get().getMessages().add(message);
            roomService.saveRoom(room.get());
        } else {
            logger.error("Room not found for roomId: {}", roomId);
            throw new RuntimeException("Room not found");
        }

        logger.info("Message sent: {}", message);
        return message;
    }
}
