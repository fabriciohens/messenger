package com.messenger.controller;

import com.messenger.model.Message;
import com.messenger.model.Room;
import com.messenger.service.IMessageService;
import com.messenger.service.IRoomService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/messages")
public class MessageController {

    private final IRoomService roomService;
    private final IMessageService messageService;

    public MessageController(final IRoomService roomService, final IMessageService messageService) {
        this.roomService = roomService;
        this.messageService = messageService;
    }

    @PostMapping("/room/{idRoom}")
    public ResponseEntity<Message> sendMessageInRoom(@PathVariable final String idRoom, @RequestBody final Message message) {
        Room room = roomService.find(idRoom);
        Message sentMessage = messageService.sendMessage(room, message);
        return ResponseEntity.status(HttpStatus.CREATED).body(sentMessage);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Message> find(@PathVariable final String id) {
        Message message = messageService.find(id);
        return ResponseEntity.status(HttpStatus.OK).body(message);
    }

    @GetMapping
    public ResponseEntity<List<Message>> findAll() {
        List<Message> messages = messageService.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(messages);
    }

}
