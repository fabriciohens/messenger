package com.messenger.controller;

import com.messenger.model.Room;
import com.messenger.model.User;
import com.messenger.service.IRoomService;
import com.messenger.service.IUserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final IUserService userService;
    private final IRoomService roomService;

    public UserController(final IUserService userService, final IRoomService roomService) {
        this.userService = userService;
        this.roomService = roomService;
    }

    @PostMapping
    public ResponseEntity<User> create(@RequestBody final User user) {
        User insertedUser = userService.insert(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(insertedUser);
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> update(@PathVariable final String id, @RequestBody final User newUser) {
        User updatedUser = userService.update(id, newUser);
        return ResponseEntity.status(HttpStatus.OK).body(updatedUser);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable final String id) {
        userService.delete(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> find(@PathVariable final String id) {
        User user = userService.find(id);
        return ResponseEntity.status(HttpStatus.OK).body(user);
    }

    @GetMapping
    public ResponseEntity<List<User>> findAll() {
        List<User> users = userService.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(users);
    }

    @GetMapping("/{idUser}/rooms")
    public ResponseEntity<List<Room>> findUsersRooms(@PathVariable final String idUser) {
        User user = userService.find(idUser);
        List<Room> rooms = roomService.findUsersRooms(user);
        return ResponseEntity.status(HttpStatus.OK).body(rooms);
    }
}
