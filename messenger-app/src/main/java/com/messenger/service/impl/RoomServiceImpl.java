package com.messenger.service.impl;

import com.messenger.exception.RoomNotFoundException;
import com.messenger.model.Message;
import com.messenger.model.Room;
import com.messenger.model.User;
import com.messenger.repository.RoomRepository;
import com.messenger.service.RoomService;
import org.apache.commons.lang.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoomServiceImpl implements RoomService {

    private final RoomRepository roomRepository;

    public RoomServiceImpl(final RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    @Override
    public Room insert(final Room room) throws IllegalArgumentException {
        this.checkIfRoomIsValid(room);
        return roomRepository.insert(room);
    }

    @Override
    public Room update(final String id, final Room newRoom) {
        this.checkIfRoomIsValid(newRoom);

        Room updateRoom = roomRepository.findById(id).orElseThrow(() -> new RoomNotFoundException(id));

        updateRoom.setName(newRoom.getName());
        updateRoom.setParticipants(newRoom.getParticipants());

        roomRepository.save(updateRoom);
        return updateRoom;
    }

    @Override
    public void delete(final String id) throws RoomNotFoundException {
        Room room = roomRepository.findById(id).orElseThrow(() -> new RoomNotFoundException(id));
        roomRepository.delete(room);
    }

    @Override
    public Room find(final String id) throws RoomNotFoundException {
        return roomRepository.findById(id).orElseThrow(() -> new RoomNotFoundException(id));
    }

    @Override
    public Page<Room> findAll(final int numPage) {
        int numberOfItemsPerPage = 5;
        Pageable pageable = PageRequest.of(numPage, numberOfItemsPerPage);
        return roomRepository.findAll(pageable);
    }

    @Override
    public List<Room> findAllByMessages(final List<Message> messages) {
        return roomRepository.findAllByMessagesIsContaining(messages);
    }

    @Override
    public List<Room> findUsersRooms(final User user) {
        return roomRepository.findAllByParticipantsIsContaining(user);
    }

    @Override
    public Room removeParticipantFromRoom(final String idRoom, final String idParticipant) throws RoomNotFoundException {
        Room updateRoom = roomRepository.findById(idRoom).orElseThrow(() -> new RoomNotFoundException(idRoom));
        updateRoom.getParticipants().removeIf(user -> user.getId().equals(idParticipant));
        roomRepository.save(updateRoom);
        return updateRoom;
    }

    private void checkIfRoomIsValid(final Room room) {
        StringBuilder errors = new StringBuilder();

        if (StringUtils.isEmpty(room.getName())) {
            errors.append("Name cannot be empty. ");
        }

        if (room.getParticipants().size() < 1) {
            errors.append("Room must have at least one participant to be created. ");
        }

        if (!StringUtils.isEmpty(errors.toString())) {
            throw new IllegalArgumentException(errors.toString());
        }
    }


}
