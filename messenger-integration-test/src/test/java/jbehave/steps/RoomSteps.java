package jbehave.steps;

import action.room.*;
import action.user.CreateUserAction;
import com.messenger.model.Message;
import com.messenger.model.Room;
import com.messenger.model.User;
import com.messenger.utils.SearchType;
import com.messenger.utils.UserRole;
import context.IntegrationTestContext;
import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;
import utils.AdminUserCredentials;
import utils.RandomString;

import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

public class RoomSteps {

    @Given("a new user $nameOfUser with role $nameOfRole")
    public void givenANewUserNameOfUserWithRoleNameOfRole(String nameOfUser, String nameOfRole) {
        CreateUserAction createUserAction = new CreateUserAction(AdminUserCredentials.EMAIL, AdminUserCredentials.PASSWORD);
        RandomString randomString = new RandomString(10);
        String email = randomString.nextString() + "@email.com";
        String password = randomString.nextString();

        User newUser = new User(nameOfUser, nameOfUser, email, password, UserRole.valueOf(nameOfRole));

        Response response = createUserAction.create(newUser);
        User createdUser = response.readEntity(User.class);
        IntegrationTestContext.put(nameOfUser, createdUser);

        assertNotNull(createdUser.getId());
        assertEquals(201, response.getStatus());
    }

    @When("$nameOfUSer creates a new room $nameOfRoom with $namesOfParticipants")
    public void whenNameOfUSerCreatesANewRoomNameOfRoomWithNamesOfParticipants(String nameOfUSer, String nameOfRoom, List<String> namesOfParticipants) {
        User user = (User) IntegrationTestContext.get(nameOfUSer);
        List<User> participants = namesOfParticipants.stream().map(u -> (User) IntegrationTestContext.get(u)).collect(Collectors.toList());
        participants.add(0, user);

        CreateRoomAction createRoomAction = new CreateRoomAction(user.getEmail(), user.getPassword());

        Room room = new Room(nameOfRoom, participants);

        Response response = createRoomAction.create(room);
        Room createdRoom = response.readEntity(Room.class);
        IntegrationTestContext.put(nameOfRoom, createdRoom);

        assertNotNull(createdRoom.getId());
        assertEquals(201, response.getStatus());
    }

    @When("$nameOfUser removes $nameOfUserToRemove from the room $nameOfRoom")
    public void whenNameOfUserRemovesNameOfUserToRemoveFromTheRoomNameOfRoom(String nameOfUser, String nameOfUserToRemove, String nameOfRoom) {
        User user = (User) IntegrationTestContext.get(nameOfUser);
        User userToRemove = (User) IntegrationTestContext.get(nameOfUserToRemove);
        Room room = (Room) IntegrationTestContext.get(nameOfRoom);

        RemoveParticipantRoomAction action = new RemoveParticipantRoomAction(user.getEmail(), user.getPassword());
        Response response = action.removeParticipant(room.getId(), userToRemove.getId());
        Room returnedRoom = response.readEntity(Room.class);
        IntegrationTestContext.put(nameOfRoom, returnedRoom);

        assertNotNull(returnedRoom.getId());
        assertEquals(200, response.getStatus());
    }

    @Then("$nameOfUser sees the room $nameOfRoom have $numOfParticipants participants")
    public void thenNameOfUserSeesTheRoomNameOfRoomHaveNumOfParticipantsParticipants(String nameOfUser, String nameOfRoom, int numOfParticipants) {
        User user = (User) IntegrationTestContext.get(nameOfUser);
        Room room = (Room) IntegrationTestContext.get(nameOfRoom);

        GetRoomAction getRoomAction = new GetRoomAction(user.getEmail(), user.getPassword());
        Response response = getRoomAction.get(room.getId());
        Room returnedRoom = response.readEntity(Room.class);

        assertEquals(numOfParticipants, returnedRoom.getParticipants().size());
        assertEquals(200, response.getStatus());
    }

    @Then("$nameOfUser fetches all rooms")
    public void thenNameOfUserFetchesAllRooms(String nameOfUser) {
        User user = (User) IntegrationTestContext.get(nameOfUser);

        FindAllRoomsAction findAllRoomsAction = new FindAllRoomsAction(user.getEmail(), user.getPassword());
        int page = 0;
        Response response = findAllRoomsAction.findAll(page);

        assertEquals(200, response.getStatus());
    }

    @When("$nameOfUser updates name of the room $nameOfRoom to $newNameOfRoom")
    public void whenNameOfUserUpdatesNameOfTheRoomNameOfRoomToNewNameOfRoom(String nameOfUser, String nameOfRoom, String newNameOfRoom) {
        User user = (User) IntegrationTestContext.get(nameOfUser);

        UpdateRoomAction updateRoomAction = new UpdateRoomAction(user.getEmail(), user.getPassword());

        Room room = (Room) IntegrationTestContext.get(nameOfRoom);
        room.setName(newNameOfRoom);

        Response response = updateRoomAction.update(room.getId(), room);
        Room returnedRoom = response.readEntity(Room.class);
        IntegrationTestContext.put(nameOfRoom, returnedRoom);

        assertEquals(200, response.getStatus());
    }

    @Then("$nameOfUser sees the room $nameOfRoom has a new name $newNameOfRoom")
    public void thenNameOfUserSeesTheRoomNameOfRoomHasANewNameNewNameOfRoom(String nameOfUser, String nameOfRoom, String newNameOfRoom) {
        User user = (User) IntegrationTestContext.get(nameOfUser);
        Room room = (Room) IntegrationTestContext.get(nameOfRoom);

        GetRoomAction getRoomAction = new GetRoomAction(user.getEmail(), user.getPassword());

        Response response = getRoomAction.get(room.getId());
        Room returnedRoom = response.readEntity(Room.class);

        assertEquals(newNameOfRoom, returnedRoom.getName());
        assertEquals(200, response.getStatus());
    }

    @When("$nameOfUser has room $nameOfRoom with $participants")
    public void whenNameOfUserHasRoomNameOfRoomWithParticipants(String nameOfUser, String nameOfRoom, List<String> namesOfParticipants) {
        User user = (User) IntegrationTestContext.get(nameOfUser);
        Room room = (Room) IntegrationTestContext.get(nameOfRoom);

        GetRoomAction getRoomAction = new GetRoomAction(user.getEmail(), user.getPassword());

        Response response = getRoomAction.get(room.getId());
        Room returnedRoom = response.readEntity(Room.class);

        assertArrayEquals(namesOfParticipants.toArray(), returnedRoom.getParticipants().stream().map(User::getFirstName).toArray());
        assertEquals(200, response.getStatus());
    }

    @When("$nameOfSender sends a message $message to $namesOfReceivers in room $nameOfRoom")
    public void whenNameOfSenderSendsAMessage$messageToNamesOfReceiversInRoomNameOfRoom(
            String nameOfSender, String message, List<String> namesOfReceivers, String nameOfRoom) {
        User sender = (User) IntegrationTestContext.get(nameOfSender);
        Room room = (Room) IntegrationTestContext.get(nameOfRoom);

        List<User> receivers = namesOfReceivers.stream().map(u -> (User) IntegrationTestContext.get(u)).collect(Collectors.toList());
        Message newMessage = new Message(sender, receivers, message);
        SendMessageRoomAction action = new SendMessageRoomAction(sender.getEmail(), sender.getPassword());

        Response response = action.sendMessage(room.getId(), newMessage);
        Message sentMessage = response.readEntity(Message.class);
        IntegrationTestContext.put(message, sentMessage);

        assertNotNull(sentMessage.getId());
        assertEquals(201, response.getStatus());
    }

    @Then("$nameOfSender message $message has $numOfReceivers receivers")
    public void thenNameOfSenderMessageMessageHasNumOfReceiversReceivers(
            String nameOfSender, String message, int numOfReceivers) {
        User sender = (User) IntegrationTestContext.get(nameOfSender);
        Message sentMessage = (Message) IntegrationTestContext.get(message);

        assertEquals(sender.getId(), sentMessage.getSender().getId());
        assertEquals(numOfReceivers, sentMessage.getReceivers().size());
    }

    @Then("$namesOfReceivers is able to see the message $message $nameOfSender sent to them in room $nameOfRoom")
    public void thenNamesOfReceiversIsAbleToSeeTheMessageMessageNameOfSenderSentToThemInRoomNameOfRoom(
            List<String> namesOfReceivers, String message, String nameOfSender, String nameOfRoom) {
        User sender = (User) IntegrationTestContext.get(nameOfSender);
        Room room = (Room) IntegrationTestContext.get(nameOfRoom);
        Message sentMessage = (Message) IntegrationTestContext.get(message);

        GetRoomAction getRoomAction = new GetRoomAction(sender.getEmail(), sender.getPassword());
        Room returnedRoom = getRoomAction.get(room.getId()).readEntity(Room.class);

        assertTrue(returnedRoom.getMessages().contains(sentMessage));
        assertEquals(sender.getId(), sentMessage.getSender().getId());
        assertArrayEquals(namesOfReceivers.toArray(), sentMessage.getReceivers().stream().map(User::getFirstName).toArray());
    }

    @Then("$nameOfUser searches rooms by CONTENT $searchParam")
    public void thenNameOfUserSearchesRoomsByContentSearchParam
            (String nameOfUser, String searchParam) {
        User user = (User) IntegrationTestContext.get(nameOfUser);

        SearchRoomAction action = new SearchRoomAction(user.getEmail(), user.getPassword());
        Response response = action.searchRoom(SearchType.CONTENT, searchParam);
        List<Room> rooms = response.readEntity(new GenericType<List<Room>>() {
        });

        assertTrue(rooms.size() >= 1);
        assertEquals(200, response.getStatus());
    }

    @Then("$nameOfUser searches rooms by SENDER $searchParam")
    public void thenNameOfUserSearchesRoomsBySenderSearchParam
            (String nameOfUser, String searchParam) {
        User user = (User) IntegrationTestContext.get(nameOfUser);
        User userToSearch = (User) IntegrationTestContext.get(searchParam);

        SearchRoomAction action = new SearchRoomAction(user.getEmail(), user.getPassword());
        Response response = action.searchRoom(SearchType.SENDER, userToSearch.getId());
        List<Room> rooms = response.readEntity(new GenericType<List<Room>>() {
        });

        assertTrue(rooms.size() >= 1);
        assertEquals(200, response.getStatus());
    }

    @Then("$nameOfUser searches rooms by RECEIVER $searchParam")
    public void thenNameOfUserSearchesRoomsByReceiverSearchParam
            (String nameOfUser, String searchParam) {
        User user = (User) IntegrationTestContext.get(nameOfUser);
        User userToSearch = (User) IntegrationTestContext.get(searchParam);

        SearchRoomAction action = new SearchRoomAction(user.getEmail(), user.getPassword());
        Response response = action.searchRoom(SearchType.RECEIVER, userToSearch.getId());
        List<Room> rooms = response.readEntity(new GenericType<List<Room>>() {
        });

        assertTrue(rooms.size() >= 1);
        assertEquals(200, response.getStatus());
    }

    @Then("the user $nameOfUser can fetch his rooms")
    public void thenNameOfUserCanFetchHisherRooms(String nameOfUser) {
        User user = (User) IntegrationTestContext.get(nameOfUser);

        FindUsersRoomsAction action = new FindUsersRoomsAction(user.getEmail(), user.getPassword());
        Response response = action.findUsersRooms(user.getId());
        List<Room> rooms = response.readEntity(new GenericType<List<Room>>() {
        });

        assertEquals(1, rooms.size());
        assertEquals(200, response.getStatus());
    }
}
