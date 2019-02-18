package action.user;

import action.ApiClientAction;
import com.messenger.model.User;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class CreateUserAction extends ApiClientAction {

    private final String path = "/users";
    private final WebTarget webTarget;

    public CreateUserAction(final String username, final String password) {
        super(username, password);
        webTarget = getWebTargetInstance(path);
    }

    public Response create(final User user) {
        return webTarget.request(MediaType.APPLICATION_JSON).post(Entity.entity(user, MediaType.APPLICATION_JSON));
    }

}
