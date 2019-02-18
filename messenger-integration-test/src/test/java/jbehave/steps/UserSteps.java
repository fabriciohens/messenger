package jbehave.steps;

import action.user.*;
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
import java.util.List;

public class UserSteps {

    @Given("a new user with role admin containing an id")
    public void givenANewUserWithRoleAdminContainingAnId(@Named("user") String user) {
        CreateUserAction createUserAction = new CreateUserAction("admin@email.com", "secret");
        RandomString randomString = new RandomString(10);
        String email = randomString.nextString() + "@email.com";
        String password = randomString.nextString();

        User newUser = new User(user, user, email, password, UserRole.ADMIN);

        Response response = createUserAction.create(newUser);

        User createdUser = response.readEntity(User.class);
        IntegrationTestContext.put(user, createdUser);

        Assert.assertNotNull(createdUser.getId());
        Assert.assertEquals(201, response.getStatus());
    }

    @When("the user fetches his user by id the api responds with detailed user")
    public void whenTheUserFetchesHisUserByIdTheApiRespondsWithDetailedUser(@Named("user") String user) {
        User userObject = (User) IntegrationTestContext.get(user);

        GetUserAction getUserAction = new GetUserAction(userObject.getEmail(), userObject.getPassword());

        Response response = getUserAction.get(userObject.getId());
        User returnedUser = response.readEntity(User.class);

        Assert.assertNotNull(returnedUser);
        Assert.assertEquals(200, response.getStatus());
    }

    @Given("an updated $user with role admin and his id")
    public void givenAnUpdatedUserWithRoleAdminAndHisId(String user) {
        User userObject = (User) IntegrationTestContext.get(user);

        UpdateUserAction updateUserAction = new UpdateUserAction(userObject.getEmail(), userObject.getPassword());

        userObject.setEmail("new" + userObject.getEmail());
        userObject.setPassword("new" + userObject.getPassword());

        Response response = updateUserAction.update(userObject.getId(), userObject);

        User updatedUser = response.readEntity(User.class);
        IntegrationTestContext.put(user, updatedUser);

        Assert.assertNotNull(updatedUser.getId());
        Assert.assertEquals(200, response.getStatus());
    }

    @When("the user fetches his user by id using new credentials the api responds with the user")
    public void whenTheUserFetchesHisUserByIdUsingNewCredentialsTheApiRespondsWithTheUser(@Named("user") String user) {
        User userObject = (User) IntegrationTestContext.get(user);

        GetUserAction getUserAction = new GetUserAction(userObject.getEmail(), userObject.getPassword());

        Response response = getUserAction.get(userObject.getId());
        User returnedUser = response.readEntity(User.class);

        Assert.assertNotNull(returnedUser);
        Assert.assertEquals(200, response.getStatus());
    }

    @When("an user lists all users the api responds with a list of all users")
    public void whenAnUserListsAllUsersTheApiRespondsWithAListOfAllUsers(@Named("user") String user) {
        User userObject = (User) IntegrationTestContext.get(user);

        FindAllUserAction findAllUserAction = new FindAllUserAction(userObject.getEmail(), userObject.getPassword());

        Response response = findAllUserAction.findAll();
        List<User> users = response.readEntity(new GenericType<List<User>>() {
        });

        Assert.assertTrue(users.size() >= 2);
        Assert.assertEquals(200, response.getStatus());
    }

    @Given("a deleted user with role admin and his id")
    public void givenADeletedUserWithRoleAdminAndHisId(@Named("user") String user) {
        User userObject = (User) IntegrationTestContext.get(user);

        DeleteUserAction deleteUserAction = new DeleteUserAction(userObject.getEmail(), userObject.getPassword());

        Response response = deleteUserAction.delete(userObject.getId());

        Assert.assertEquals(200, response.getStatus());
    }

    @When("the user fetches his user by id using his credentials the api responds with unauthorized")
    public void givenTheUserFetchesHisUserByIdUsingHisCredentialsTheApiRespondsWithUnauthorized(@Named("user") String user) {
        User userObject = (User) IntegrationTestContext.get(user);

        GetUserAction getUserAction = new GetUserAction(userObject.getEmail(), userObject.getPassword());

        Response response = getUserAction.get(userObject.getId());

        Assert.assertEquals(401, response.getStatus());
    }
}
