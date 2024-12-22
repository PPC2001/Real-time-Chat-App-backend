package com.chat.app.entity;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Document(collection  = "rooms")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Room {

	@Id
	private String id;
	private String roomId;

	private List<Message> messages = new ArrayList<>();

}
