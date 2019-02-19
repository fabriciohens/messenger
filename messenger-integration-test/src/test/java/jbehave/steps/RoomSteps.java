package jbehave.steps;

import action.room.*;
import action.user.CreateUserAction;
import com.messenger.model.Room;
import com.messenger.model.User;
import com.messenger.utils.UserRole;
import context.IntegrationTestContext;
import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;
import org.junit.Assert;
import utils.AdminUserCredentials;
import utils.RandomString;

import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

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

        Assert.assertNotNull(createdUser.getId());
        Assert.assertEquals(201, response.getStatus());
    }

    @Given("$nameOfUSer creates a new room $nameOfRoom with $namesOfParticipants")
    public void givenNameOfUSerCreatesANewRoomNameOfRoomWithNamesOfParticipants(String nameOfUSer, String nameOfRoom, List<String> namesOfParticipants) {
        User user = (User) IntegrationTestContext.get(nameOfUSer);
        List<User> participants = new ArrayList<>();
        participants.add(user);
        for (String key : namesOfParticipants) {
            User u = (User) IntegrationTestContext.get(key);
            participants.add(u);
        }

        CreateRoomAction createRoomAction = new CreateRoomAction(user.getEmail(), user.getPassword());

        Room room = new Room(nameOfRoom, participants);

        Response response = createRoomAction.create(room);

        Room createdRoom = response.readEntity(Room.class);
        IntegrationTestContext.put(nameOfRoom, createdRoom);

        Assert.assertNotNull(createdRoom.getId());
        Assert.assertEquals(200, response.getStatus());
    }

    @Then("$nameOfUSer sees the room $nameOfRoom have $numOfParticipants participants")
    public void thenNameOfUSerSeesTheRoomNameOfRoomHaveNumOfParticipantsParticipants(String nameOfUser, String nameOfRoom, int numOfParticipants) {
        User user = (User) IntegrationTestContext.get(nameOfUser);
        Room room = (Room) IntegrationTestContext.get(nameOfRoom);

        GetRoomAction getRoomAction = new GetRoomAction(user.getEmail(), user.getPassword());

        Response response = getRoomAction.get(room.getId());
        Room returnedRoom = response.readEntity(Room.class);

        Assert.assertEquals(numOfParticipants, returnedRoom.getParticipants().size());
        Assert.assertEquals(200, response.getStatus());
    }

    @When("$nameOfUSer removes $nameOfUserToRemove from the room $nameOfRoom")
    public void whenNameOfUSerRemovesNameOfUserToRemoveFromTheRoomNameOfRoom(String nameOfUser, String nameOfUserToRemove, String nameOfRoom) {
        User user = (User) IntegrationTestContext.get(nameOfUser);
        User userToRemove = (User) IntegrationTestContext.get(nameOfUserToRemove);
        Room room = (Room) IntegrationTestContext.get(nameOfRoom);

        RemoveParticipantRoomAction action = new RemoveParticipantRoomAction(user.getEmail(), user.getPassword());

        Response response = action.removeParticipant(room.getId(), userToRemove.getId());
        Room returnedRoom = response.readEntity(Room.class);
        IntegrationTestContext.put(nameOfRoom, returnedRoom);

        Assert.assertNotNull(returnedRoom.getId());
        Assert.assertEquals(200, response.getStatus());
    }

    @Then("the $nameOfUser is able to fetche all rooms")
    public void thenTheNameOfUserIsAbleToFetcheAllRooms(String nameOfUser) {
        User user = (User) IntegrationTestContext.get(nameOfUser);

        FindAllRoomAction findAllRoomAction = new FindAllRoomAction(user.getEmail(), user.getPassword());

        Response response = findAllRoomAction.findAll();

        Assert.assertEquals(200, response.getStatus());
    }

    @Given("$nameOfUser updates name of the room $nameOfRoom to $newNameOfRoom")
    public void givenNameOfUserUpdatesNameOfTheRoomNameOfRoomToNewNameOfRoom(String nameOfUser, String nameOfRoom, String newNameOfRoom) {
        User user = (User) IntegrationTestContext.get(nameOfUser);

        UpdateRoomAction updateRoomAction = new UpdateRoomAction(user.getEmail(), user.getPassword());

        Room room = (Room) IntegrationTestContext.get(nameOfRoom);
        room.setName(newNameOfRoom);

        Response response = updateRoomAction.update(room.getId(), room);
        Room returnedRoom = response.readEntity(Room.class);
        IntegrationTestContext.put(nameOfRoom, returnedRoom);

        Assert.assertEquals(200, response.getStatus());
    }

    @Then("$nameOfUser sees the room $nameOfRoom has a new name $newNameOfRoom")
    public void thenNameOfUserSeesTheRoomNameOfRoomHasANewNameNewNameOfRoom(String nameOfUser, String nameOfRoom, String newNameOfRoom) {
        User user = (User) IntegrationTestContext.get(nameOfUser);
        Room room = (Room) IntegrationTestContext.get(nameOfRoom);

        GetRoomAction getRoomAction = new GetRoomAction(user.getEmail(), user.getPassword());

        Response response = getRoomAction.get(room.getId());
        Room returnedRoom = response.readEntity(Room.class);

        Assert.assertEquals(newNameOfRoom, returnedRoom.getName());
        Assert.assertEquals(200, response.getStatus());
    }

    @When("$nameOfUser deletes the room $nameOfRoom")
    public void whenNameOfUserDeletesTheRoomNameOfRoom(String nameOfUser, String nameOfRoom) {
        User user = (User) IntegrationTestContext.get(nameOfUser);
        Room room = (Room) IntegrationTestContext.get(nameOfRoom);

        DeleteRoomAction deleteRoomAction = new DeleteRoomAction(user.getEmail(), user.getPassword());

        Response response = deleteRoomAction.delete(room.getId());

        Assert.assertEquals(200, response.getStatus());
    }

    @Then("the user $nameOfUser sees the room $nameOfRoom does not exists anymore")
    public void thenTheUserNameOfUserSeesTheRoomNameOfRoomDoesNotExistsAnymore(String nameOfUser, String nameOfRoom) {
        User user = (User) IntegrationTestContext.get(nameOfUser);
        Room room = (Room) IntegrationTestContext.get(nameOfRoom);

        GetRoomAction getRoomAction = new GetRoomAction(user.getEmail(), user.getPassword());

        Response response = getRoomAction.get(room.getId());

        Assert.assertEquals(404, response.getStatus());
    }
}
