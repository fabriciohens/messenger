package jbehave.steps;

import action.room.CreateRoomAction;
import action.room.GetRoomAction;
import action.user.*;
import com.messenger.model.Room;
import com.messenger.model.User;
import com.messenger.utils.UserRole;
import context.IntegrationTestContext;
import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Named;
import org.jbehave.core.annotations.When;
import org.junit.Assert;
import utils.RandomString;

import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

public class RoomSteps {

    @Given("a new user with role normal containing an id")
    public void givenANewUserContainingAnId(@Named("user") String user) {
        CreateUserAction createUserAction = new CreateUserAction("admin@email.com", "secret");
        RandomString randomString = new RandomString(10);
        String email = randomString.nextString() + "@email.com";
        String password = randomString.nextString();

        User newUser = new User(user, user, email, password, UserRole.NORMAL);

        Response response = createUserAction.create(newUser);

        User createdUser = response.readEntity(User.class);
        IntegrationTestContext.put(user, createdUser);

        Assert.assertNotNull(createdUser.getId());
        Assert.assertEquals(201, response.getStatus());
    }

    @Given("an user and a new room with participants containing an id")
    public void givenAnUpdatedUserBRUNAwithRoleADMINAndHerID(@Named("user") String nameOfUser,
                                                             @Named("room") String nameOfRoom,
                                                             @Named("participants") List<String> nameOfParticipants) {
        User user = (User) IntegrationTestContext.get(nameOfUser);
        List<User> participants = new ArrayList<>();
        for (String key : nameOfParticipants){
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

    @When("the user fetches his room by id using his credentials the api responds with the room")
    public void whenBRUNAFetchesHerselfByIdUsingNewCredentialsTheApiRespondsWithNewBRUNA(@Named("user") String nameOfUser,
                                                                                         @Named("room") String nameOfRoom) {
        User user = (User) IntegrationTestContext.get(nameOfUser);
        Room room = (Room) IntegrationTestContext.get(nameOfRoom);

        GetRoomAction getRoomAction = new GetRoomAction(user.getEmail(), user.getPassword());

        Response response = getRoomAction.get(room.getId());
        Room returnedRoom = response.readEntity(Room.class);

        Assert.assertNotNull(returnedRoom);
        Assert.assertEquals(200, response.getStatus());
    }

}
