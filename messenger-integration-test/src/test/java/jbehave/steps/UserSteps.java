package jbehave.steps;

import action.user.*;
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

public class UserSteps {

    @Given("a new user $nameOfUser with role $nameOfRole")
    public void givenANewUserNameofuserWithRoleNameofrole(String nameOfUser, String nameOfRole) {
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

    @When("$nameOfUser creates a new user $nameOfUser with role $nameOfRole")
    public void whenNameOfUserCreatesANewUserNameOfUserWithRoleNameOfRole(String nameOfUser, String nameOfUserToUpdate, String nameOfRole) {
        User rodrigo = (User) IntegrationTestContext.get(nameOfUser);
        CreateUserAction createUserAction = new CreateUserAction(rodrigo.getEmail(), rodrigo.getPassword());

        RandomString randomString = new RandomString(10);
        String email = randomString.nextString() + "@email.com";
        String password = randomString.nextString();

        User newUser = new User(nameOfUserToUpdate, nameOfUserToUpdate, email, password, UserRole.valueOf(nameOfRole));

        Response response = createUserAction.create(newUser);

        User createdUser = response.readEntity(User.class);
        IntegrationTestContext.put(nameOfUserToUpdate, createdUser);

        Assert.assertNotNull(createdUser.getId());
        Assert.assertEquals(201, response.getStatus());
    }

    @Then("$nameOfUser is able to fetch all users")
    public void thenNameOfUserIsAbleToFetchAllUsers(String nameOfUser) {
        User user = (User) IntegrationTestContext.get(nameOfUser);

        FindAllUserAction findAllUserAction = new FindAllUserAction(user.getEmail(), user.getPassword());

        Response response = findAllUserAction.findAll();

        Assert.assertEquals(200, response.getStatus());
    }

    @Then("$nameOfUser is unable to fetch all users")
    public void thenNameOfUserIsUnableToFetchAllUsers(String nameOfUser) {
        User user = (User) IntegrationTestContext.get(nameOfUser);

        FindAllUserAction findAllUserAction = new FindAllUserAction(user.getEmail(), user.getPassword());

        Response response = findAllUserAction.findAll();

        Assert.assertEquals(403, response.getStatus());
    }

    @When("$nameOfUser updates role of $nameOfUser to $nameOfRole")
    public void whenNameOfUserUpdatesRoleOfNameOfUserToNameOfRole(String nameOfUser, String nameOfUserToUpdate, String nameOfRole) {
        User rodrigo = (User) IntegrationTestContext.get(nameOfUser);
        UpdateUserAction updateUserAction = new UpdateUserAction(rodrigo.getEmail(), rodrigo.getPassword());

        User user = (User) IntegrationTestContext.get(nameOfUserToUpdate);
        user.setUserRole(UserRole.valueOf(nameOfRole));

        Response response = updateUserAction.update(user.getId(), user);

        User updatedUser = response.readEntity(User.class);
        IntegrationTestContext.put(nameOfUserToUpdate, updatedUser);

        Assert.assertNotNull(updatedUser.getId());
        Assert.assertEquals(200, response.getStatus());
    }

    @Then("$nameOfUser is able to fetch user $nameOfUserToFetch")
    public void thenNameOfUserIsAbleToFetchUserNameOfUserToFetch(String nameOfUser, String nameOfUserToFetch) {
        User user = (User) IntegrationTestContext.get(nameOfUser);
        GetUserAction getUserAction = new GetUserAction(user.getEmail(), user.getPassword());

        User userToFetch = (User) IntegrationTestContext.get(nameOfUserToFetch);
        Response response = getUserAction.get(userToFetch.getId());
        User returnedUser = response.readEntity(User.class);

        Assert.assertNotNull(returnedUser);
        Assert.assertEquals(200, response.getStatus());
    }

}
