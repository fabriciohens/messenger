package jbehave.steps;

import action.user.CreateUserAction;
import action.user.FindAllUserAction;
import action.user.GetUserAction;
import action.user.UpdateUserAction;
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

import static org.junit.Assert.assertNotNull;

public class UserSteps {

    private final RandomString randomString = new RandomString(10);

    @Given("a new user $nameOfUser with role $nameOfRole")
    public void givenANewUserNameOfUserWithRoleNameOfRole(final String nameOfUser, final String nameOfRole) {
        User newUser = new User(nameOfUser, nameOfUser, generateEmail(), generatePassword(), UserRole.valueOf(nameOfRole));
        CreateUserAction action = new CreateUserAction(AdminUserCredentials.EMAIL, AdminUserCredentials.PASSWORD);

        Response response = action.create(newUser);
        assertNotNull(response);
        User createdUser = response.readEntity(User.class);
        IntegrationTestContext.putNewObject(nameOfUser, createdUser);

        Assert.assertNotNull(createdUser.getId());
        Assert.assertEquals(201, response.getStatus());
    }

    @When("$nameOfUser creates a new user $nameOfUser with role $nameOfRole")
    public void whenNameOfUserCreatesANewUserNameOfUserWithRoleNameOfRole(final String nameOfUser,
                                                                          final String nameOfUserToUpdate,
                                                                          final String nameOfRole) {
        User rodrigo = (User) IntegrationTestContext.getObject(nameOfUser);
        User newUser = new User(nameOfUserToUpdate, nameOfUserToUpdate, generateEmail(), generatePassword(), UserRole.valueOf(nameOfRole));

        CreateUserAction createUserAction = new CreateUserAction(rodrigo.getEmail(), rodrigo.getPassword());
        Response response = createUserAction.create(newUser);
        assertNotNull(response);
        User createdUser = response.readEntity(User.class);
        IntegrationTestContext.putNewObject(nameOfUserToUpdate, createdUser);

        Assert.assertNotNull(createdUser.getId());
        Assert.assertEquals(201, response.getStatus());
    }

    private String generateEmail() {
        return randomString.nextString() + "@email.com";
    }

    private String generatePassword() {
        return randomString.nextString();
    }

    @Then("$nameOfUser is able to fetch all users")
    public void thenNameOfUserIsAbleToFetchAllUsers(final String nameOfUser) {
        User user = (User) IntegrationTestContext.getObject(nameOfUser);
        FindAllUserAction action = new FindAllUserAction(user.getEmail(), user.getPassword());
        Response response = action.findAll();
        Assert.assertEquals(200, response.getStatus());
    }

    @Then("$nameOfUser is unable to fetch all users")
    public void thenNameOfUserIsUnableToFetchAllUsers(final String nameOfUser) {
        User user = (User) IntegrationTestContext.getObject(nameOfUser);
        FindAllUserAction action = new FindAllUserAction(user.getEmail(), user.getPassword());
        Response response = action.findAll();
        Assert.assertEquals(403, response.getStatus());
    }

    @When("$nameOfUser updates role of $nameOfUserToUpdate to $nameOfRole")
    public void whenNameOfUserUpdatesRoleOfNameOfUserToUpdateToNameOfRole(final String nameOfUser,
                                                                          final String nameOfUserToUpdate,
                                                                          final String nameOfRole) {
        User rodrigo = (User) IntegrationTestContext.getObject(nameOfUser);
        UpdateUserAction action = new UpdateUserAction(rodrigo.getEmail(), rodrigo.getPassword());

        User user = (User) IntegrationTestContext.getObject(nameOfUserToUpdate);
        user.setUserRole(UserRole.valueOf(nameOfRole));

        Response response = action.update(user.getId(), user);
        assertNotNull(response);
        User updatedUser = response.readEntity(User.class);
        IntegrationTestContext.updateObject(nameOfUserToUpdate, updatedUser);

        Assert.assertNotNull(updatedUser.getId());
        Assert.assertEquals(200, response.getStatus());
    }

    @Then("$nameOfUser is able to fetch user $nameOfUserToFetch")
    public void thenNameOfUserIsAbleToFetchUserNameOfUserToFetch(final String nameOfUser,
                                                                 final String nameOfUserToFetch) {
        User user = (User) IntegrationTestContext.getObject(nameOfUser);
        GetUserAction action = new GetUserAction(user.getEmail(), user.getPassword());

        User userToFetch = (User) IntegrationTestContext.getObject(nameOfUserToFetch);
        Response response = action.get(userToFetch.getId());
        assertNotNull(response);
        User returnedUser = response.readEntity(User.class);

        Assert.assertNotNull(returnedUser);
        Assert.assertEquals(200, response.getStatus());
    }

}
