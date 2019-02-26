package com.messenger.service;

import com.messenger.exception.RoomNotFoundException;
import com.messenger.model.Message;
import com.messenger.model.Room;
import com.messenger.model.User;
import org.springframework.data.domain.Page;

import java.util.List;

public interface RoomService {

    Room insert(final Room room) throws IllegalArgumentException;

    Room update(final String id, final Room room) throws IllegalArgumentException, RoomNotFoundException;

    void delete(final String id) throws RoomNotFoundException;

    Room find(final String id) throws RoomNotFoundException;

    Page<Room> findAll(final int numPage);

    List<Room> findUsersRooms(final User user);

    List<Room> findAllByMessages(final List<Message> messages);

    Room removeParticipantFromRoom(final String idRoom, final String idParticipant) throws RoomNotFoundException;
}
