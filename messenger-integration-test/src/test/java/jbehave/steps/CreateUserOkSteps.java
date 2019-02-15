package jbehave.steps;

import action.ApiClientUserAction;
import com.messenger.model.User;
import com.messenger.utils.UserRole;
import context.ApiTestContext;
import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.When;
import org.junit.Assert;

import javax.ws.rs.core.Response;

public class CreateUserOkSteps {

    private final ApiClientUserAction action;

    public CreateUserOkSteps() {
        action = new ApiClientUserAction("admin@email.com", "secret");
    }

    @Given("a new user BRUNA with role ADMIN containing an id")
    public void givenANewUserBRUNAContainingAnId() {
        User newUser = new User("Bruna", "Raul", "bruna@email.com", "secret", UserRole.ADMIN);

        Response response = action.post(newUser);

        User createdUser = response.readEntity(User.class);
        ApiTestContext.put("brunaId", createdUser.getId());

        Assert.assertNotNull(createdUser.getId());
        Assert.assertEquals(201, response.getStatus());
    }


    @When("BRUNA fetches herself by id the api responds with detailed BRUNA")
    public void whenBRUNAFetchesHerByIdTheApiRespondsWithDetailedBRUNA() {
        String brunaId = (String) ApiTestContext.get("brunaId");

        Response response = action.get(brunaId);
        User returnedUser = response.readEntity(User.class);

        Assert.assertNotNull(returnedUser);
        Assert.assertEquals(200, response.getStatus());
    }
}
