package com.messenger.controller;

import com.messenger.model.Message;
import com.messenger.model.Room;
import com.messenger.model.User;
import com.messenger.service.IMessageService;
import com.messenger.service.IRoomService;
import com.messenger.service.IUserService;
import com.messenger.utils.SearchType;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rooms")
public class RoomController {

    private final IRoomService roomService;
    private final IMessageService messageService;
    private final IUserService userService;

    public RoomController(final IRoomService roomService, final IMessageService messageService, final IUserService userService) {
        this.roomService = roomService;
        this.messageService = messageService;
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<Room> create(@RequestBody final Room room) {
        Room insertedRoom = roomService.insert(room);
        return ResponseEntity.status(HttpStatus.CREATED).body(insertedRoom);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Room> update(@PathVariable final String id, @RequestBody final Room newRoom) {
        Room UpdatedRoom = roomService.update(id, newRoom);
        return ResponseEntity.status(HttpStatus.OK).body(UpdatedRoom);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable final String id) {
        roomService.delete(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Room> find(@PathVariable final String id) {
        Room room = roomService.find(id);
        return ResponseEntity.status(HttpStatus.OK).body(room);
    }

    @GetMapping("/page/{numPage}")
    public ResponseEntity<Page<Room>> findAllRooms(@PathVariable final int numPage) {
        Page<Room> pageOfRooms = roomService.findAll(numPage);
        return ResponseEntity.status(HttpStatus.OK).body(pageOfRooms);
    }

    @PostMapping("/search")
    public ResponseEntity<List<Room>> search(@RequestParam final SearchType searchType, @RequestParam final String searchParam) {
        List<Message> messagesFound = messageService.search(searchType, searchParam);

        List<Room> roomsFound = roomService.findAllByMessages(messagesFound);
        return ResponseEntity.status(HttpStatus.OK).body(roomsFound);
    }

    @PostMapping("/{idRoom}/remove-participant/{idParticipant}")
    public ResponseEntity<Room> removeParticipant(@PathVariable final String idRoom, @PathVariable final String idParticipant) {
        Room updateRoom = roomService.removeParticipantFromRoom(idRoom, idParticipant);
        return ResponseEntity.status(HttpStatus.OK).body(updateRoom);
    }

    @GetMapping("/user/{idUser}")
    public ResponseEntity<List<Room>> findUsersRooms(@PathVariable final String idUser) {
        User user = userService.find(idUser);
        List<Room> rooms = roomService.findUsersRooms(user);
        return ResponseEntity.status(HttpStatus.OK).body(rooms);
    }

    @PostMapping("/{idRoom}/send-message")
    public ResponseEntity<Message> sendMessageInRoom(@PathVariable final String idRoom, @RequestBody final Message message) {
        Room room = roomService.find(idRoom);
        messageService.sendMessage(room, message);
        return ResponseEntity.status(HttpStatus.CREATED).body(message);
    }

}
