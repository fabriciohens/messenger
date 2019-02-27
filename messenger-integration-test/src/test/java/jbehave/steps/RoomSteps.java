package jbehave.steps;

import action.message.FindAllMessagesAction;
import action.message.GetMessageAction;
import action.message.SendMessageAction;
import action.room.*;
import com.messenger.model.Message;
import com.messenger.model.Room;
import com.messenger.model.User;
import com.messenger.utils.SearchType;
import context.IntegrationTestContext;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;

import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class RoomSteps {

    @When("$nameOfUSer creates a new room $nameOfRoom with $namesOfParticipants")
    public void whenNameOfUSerCreatesANewRoomNameOfRoomWithNamesOfParticipants(final String nameOfUSer,
                                                                               final String nameOfRoom,
                                                                               final List<String> namesOfParticipants) {
        User user = (User) IntegrationTestContext.getObject(nameOfUSer);
        List<User> participants = namesOfParticipants.stream().map(u -> (User) IntegrationTestContext.getObject(u)).collect(Collectors.toList());
        participants.add(0, user);

        CreateRoomAction createRoomAction = new CreateRoomAction(user.getEmail(), user.getPassword());

        Room room = new Room(nameOfRoom, participants);

        Response response = createRoomAction.create(room);
        assertNotNull(response);
        Room createdRoom = response.readEntity(Room.class);
        IntegrationTestContext.putNewObject(nameOfRoom, createdRoom);

        assertNotNull(createdRoom.getId());
        assertEquals(201, response.getStatus());
    }

    @When("$nameOfUser removes $nameOfUserToRemove from the room $nameOfRoom")
    public void whenNameOfUserRemovesNameOfUserToRemoveFromTheRoomNameOfRoom(final String nameOfUser,
                                                                             final String nameOfUserToRemove,
                                                                             final String nameOfRoom) {
        User user = (User) IntegrationTestContext.getObject(nameOfUser);
        User userToRemove = (User) IntegrationTestContext.getObject(nameOfUserToRemove);
        Room room = (Room) IntegrationTestContext.getObject(nameOfRoom);

        RemoveParticipantRoomAction action = new RemoveParticipantRoomAction(user.getEmail(), user.getPassword());
        Response response = action.removeParticipant(room.getId(), userToRemove.getId());
        assertNotNull(response);
        Room returnedRoom = response.readEntity(Room.class);
        IntegrationTestContext.updateObject(nameOfRoom, returnedRoom);

        assertNotNull(returnedRoom.getId());
        assertEquals(200, response.getStatus());
    }

    @Then("$nameOfUser sees the room $nameOfRoom have $numOfParticipants participants")
    public void thenNameOfUserSeesTheRoomNameOfRoomHaveNumOfParticipantsParticipants(final String nameOfUser,
                                                                                     final String nameOfRoom,
                                                                                     final int numOfParticipants) {
        User user = (User) IntegrationTestContext.getObject(nameOfUser);
        Room room = (Room) IntegrationTestContext.getObject(nameOfRoom);

        GetRoomAction getRoomAction = new GetRoomAction(user.getEmail(), user.getPassword());
        Response response = getRoomAction.get(room.getId());
        assertNotNull(response);
        Room returnedRoom = response.readEntity(Room.class);

        assertEquals(numOfParticipants, returnedRoom.getParticipants().size());
        assertEquals(200, response.getStatus());
    }

    @Then("$nameOfUser fetches all rooms")
    public void thenNameOfUserFetchesAllRooms(final String nameOfUser) {
        User user = (User) IntegrationTestContext.getObject(nameOfUser);

        FindAllRoomsAction findAllRoomsAction = new FindAllRoomsAction(user.getEmail(), user.getPassword());
        int page = 0;
        Response response = findAllRoomsAction.findAll(page);
        assertNotNull(response);
        assertEquals(200, response.getStatus());
    }

    @When("$nameOfUser updates name of the room $nameOfRoom to $newNameOfRoom")
    public void whenNameOfUserUpdatesNameOfTheRoomNameOfRoomToNewNameOfRoom(final String nameOfUser,
                                                                            final String nameOfRoom,
                                                                            final String newNameOfRoom) {
        User user = (User) IntegrationTestContext.getObject(nameOfUser);

        UpdateRoomAction updateRoomAction = new UpdateRoomAction(user.getEmail(), user.getPassword());

        Room room = (Room) IntegrationTestContext.getObject(nameOfRoom);
        room.setName(newNameOfRoom);

        Response response = updateRoomAction.update(room.getId(), room);
        assertNotNull(response);
        Room returnedRoom = response.readEntity(Room.class);
        IntegrationTestContext.updateObject(nameOfRoom, returnedRoom);

        assertEquals(200, response.getStatus());
    }

    @Then("$nameOfUser sees the room $nameOfRoom has a new name $newNameOfRoom")
    public void thenNameOfUserSeesTheRoomNameOfRoomHasANewNameNewNameOfRoom(final String nameOfUser,
                                                                            final String nameOfRoom,
                                                                            final String newNameOfRoom) {
        User user = (User) IntegrationTestContext.getObject(nameOfUser);
        Room room = (Room) IntegrationTestContext.getObject(nameOfRoom);

        GetRoomAction getRoomAction = new GetRoomAction(user.getEmail(), user.getPassword());

        Response response = getRoomAction.get(room.getId());
        assertNotNull(response);
        Room returnedRoom = response.readEntity(Room.class);

        assertEquals(newNameOfRoom, returnedRoom.getName());
        assertEquals(200, response.getStatus());
    }

    @When("$nameOfUser has room $nameOfRoom with $participants")
    public void whenNameOfUserHasRoomNameOfRoomWithParticipants(final String nameOfUser,
                                                                final String nameOfRoom,
                                                                final List<String> namesOfParticipants) {
        User user = (User) IntegrationTestContext.getObject(nameOfUser);
        Room room = (Room) IntegrationTestContext.getObject(nameOfRoom);

        GetRoomAction getRoomAction = new GetRoomAction(user.getEmail(), user.getPassword());

        Response response = getRoomAction.get(room.getId());
        assertNotNull(response);
        Room returnedRoom = response.readEntity(Room.class);

        assertArrayEquals(namesOfParticipants.toArray(), returnedRoom.getParticipants().stream().map(User::getFirstName).toArray());
        assertEquals(200, response.getStatus());
    }

    @When("$nameOfSender sends a message $message to $namesOfReceivers in room $nameOfRoom")
    public void whenNameOfSenderSendsAMessage$messageToNamesOfReceiversInRoomNameOfRoom(final String nameOfSender,
                                                                                        final String message,
                                                                                        final List<String> namesOfReceivers,
                                                                                        final String nameOfRoom) {
        User sender = (User) IntegrationTestContext.getObject(nameOfSender);
        Room room = (Room) IntegrationTestContext.getObject(nameOfRoom);

        List<User> receivers = namesOfReceivers.stream().map(u -> (User) IntegrationTestContext.getObject(u)).collect(Collectors.toList());
        Message newMessage = new Message(sender, receivers, message);
        SendMessageAction action = new SendMessageAction(sender.getEmail(), sender.getPassword());

        Response response = action.sendMessage(room.getId(), newMessage);
        assertNotNull(response);
        Message sentMessage = response.readEntity(Message.class);
        IntegrationTestContext.putNewObject(message, sentMessage);

        assertNotNull(sentMessage.getId());
        assertEquals(201, response.getStatus());
    }

    @Then("$nameOfSender message $message has $numOfReceivers receivers")
    public void thenNameOfSenderMessageMessageHasNumOfReceiversReceivers(final String nameOfSender,
                                                                         final String message,
                                                                         final int numOfReceivers) {
        User sender = (User) IntegrationTestContext.getObject(nameOfSender);
        Message sentMessage = (Message) IntegrationTestContext.getObject(message);

        assertEquals(sender.getId(), sentMessage.getSender().getId());
        assertEquals(numOfReceivers, sentMessage.getReceivers().size());
    }

    @Then("$namesOfReceivers is able to see the message $message $nameOfSender sent to them in room $nameOfRoom")
    public void thenNamesOfReceiversIsAbleToSeeTheMessageMessageNameOfSenderSentToThemInRoomNameOfRoom(final List<String> namesOfReceivers,
                                                                                                       final String message,
                                                                                                       final String nameOfSender,
                                                                                                       final String nameOfRoom) {
        User sender = (User) IntegrationTestContext.getObject(nameOfSender);
        Room room = (Room) IntegrationTestContext.getObject(nameOfRoom);
        Message sentMessage = (Message) IntegrationTestContext.getObject(message);

        GetRoomAction getRoomAction = new GetRoomAction(sender.getEmail(), sender.getPassword());
        Response response = getRoomAction.get(room.getId());
        assertNotNull(response);
        Room returnedRoom = response.readEntity(Room.class);

                assertTrue(returnedRoom.getMessages().contains(sentMessage));
        assertEquals(sender.getId(), sentMessage.getSender().getId());
        assertArrayEquals(namesOfReceivers.toArray(), sentMessage.getReceivers().stream().map(User::getFirstName).toArray());
    }

    @Then("$nameOfUser searches rooms by CONTENT $searchParam")
    public void thenNameOfUserSearchesRoomsByContentSearchParam(final String nameOfUser,
                                                                final String searchParam) {
        User user = (User) IntegrationTestContext.getObject(nameOfUser);

        SearchRoomAction action = new SearchRoomAction(user.getEmail(), user.getPassword());
        Response response = action.searchRoom(SearchType.CONTENT, searchParam);
        assertNotNull(response);
        List<Room> rooms = response.readEntity(new GenericType<List<Room>>() {
        });

        assertTrue(rooms.size() >= 1);
        assertEquals(200, response.getStatus());
    }

    @Then("$nameOfUser searches rooms by SENDER $searchParam")
    public void thenNameOfUserSearchesRoomsBySenderSearchParam(final String nameOfUser,
                                                               final String searchParam) {
        User user = (User) IntegrationTestContext.getObject(nameOfUser);
        User userToSearch = (User) IntegrationTestContext.getObject(searchParam);

        SearchRoomAction action = new SearchRoomAction(user.getEmail(), user.getPassword());
        Response response = action.searchRoom(SearchType.SENDER, userToSearch.getId());
        assertNotNull(response);
        List<Room> rooms = response.readEntity(new GenericType<List<Room>>() {
        });

        assertTrue(rooms.size() >= 1);
        assertEquals(200, response.getStatus());
    }

    @Then("$nameOfUser searches rooms by RECEIVER $searchParam")
    public void thenNameOfUserSearchesRoomsByReceiverSearchParam(final String nameOfUser,
                                                                 final String searchParam) {
        User user = (User) IntegrationTestContext.getObject(nameOfUser);
        User userToSearch = (User) IntegrationTestContext.getObject(searchParam);

        SearchRoomAction action = new SearchRoomAction(user.getEmail(), user.getPassword());
        Response response = action.searchRoom(SearchType.RECEIVER, userToSearch.getId());
        assertNotNull(response);
        List<Room> rooms = response.readEntity(new GenericType<List<Room>>() {
        });

        assertTrue(rooms.size() >= 1);
        assertEquals(200, response.getStatus());
    }

    @Then("the user $nameOfUser can fetch his rooms")
    public void thenNameOfUserCanFetchHisherRooms(final String nameOfUser) {
        User user = (User) IntegrationTestContext.getObject(nameOfUser);

        FindUsersRoomsAction action = new FindUsersRoomsAction(user.getEmail(), user.getPassword());
        Response response = action.findUsersRooms(user.getId());
        assertNotNull(response);
        List<Room> rooms = response.readEntity(new GenericType<List<Room>>() {
        });

        assertEquals(1, rooms.size());
        assertEquals(200, response.getStatus());
    }

    @Then("$nameOfUser fetches all messages")
    public void thenNameOfUserFetchesAllMessages(final String nameOfUser) {
        User user = (User) IntegrationTestContext.getObject(nameOfUser);

        FindAllMessagesAction action = new FindAllMessagesAction(user.getEmail(), user.getPassword());
        Response response = action.findAll();
        assertNotNull(response);
        assertEquals(200, response.getStatus());
    }

    @Then("$nameOfAuditor fetches message $nameOfMessage")
    public void thenNameOfAuditorFetchesMessageNameOfMessage(final String nameOfAuditor,
                                                             final String nameOfMessage) {
        User auditor = (User) IntegrationTestContext.getObject(nameOfAuditor);
        Message message = (Message) IntegrationTestContext.getObject(nameOfMessage);

        GetMessageAction action = new GetMessageAction(auditor.getEmail(), auditor.getPassword());
        Response response = action.get(message.getId());
        assertNotNull(response);
        Message returnedMessage = response.readEntity(Message.class);

        assertEquals(nameOfMessage, returnedMessage.getContent());
        assertEquals(200, response.getStatus());
    }

    @Then("$nameOfAuditor sees $nameOfSender sent message $nameOfMessage")
    public void thenNameOfAuditorSeesNameOfSenderSentMessageNameOfMessage(final String nameOfAuditor,
                                                                          final String nameOfSender,
                                                                          final String nameOfMessage) {
        User auditor = (User) IntegrationTestContext.getObject(nameOfAuditor);
        Message message = (Message) IntegrationTestContext.getObject(nameOfMessage);

        GetMessageAction action = new GetMessageAction(auditor.getEmail(), auditor.getPassword());
        Response response = action.get(message.getId());
        assertNotNull(response);
        Message returnedMessage = response.readEntity(Message.class);

        assertEquals(nameOfSender, returnedMessage.getSender().getFirstName());
        assertEquals(200, response.getStatus());
    }

    @Then("$nameOfAuditor sees $namesOfReceivers received message $nameOfMessage")
    public void thenNameOfAuditorSeesNamesOfReceiversReceivedMessageNameOfMessage(final String nameOfAuditor,
                                                                                  final List<String> namesOfReceivers,
                                                                                  final String nameOfMessage) {
        User auditor = (User) IntegrationTestContext.getObject(nameOfAuditor);
        Message message = (Message) IntegrationTestContext.getObject(nameOfMessage);

        GetMessageAction action = new GetMessageAction(auditor.getEmail(), auditor.getPassword());
        Response response = action.get(message.getId());
        assertNotNull(response);
        Message returnedMessage = response.readEntity(Message.class);
        assertArrayEquals(namesOfReceivers.toArray(), returnedMessage.getReceivers().stream().map(User::getFirstName).toArray());
        assertEquals(200, response.getStatus());
    }
}
