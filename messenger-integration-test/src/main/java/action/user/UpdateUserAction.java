package action.user;

import action.ApiClientAction;
import com.messenger.model.User;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class UpdateUserAction extends ApiClientAction {

    private final String path = "/users";
    private final WebTarget webTarget;

    public UpdateUserAction(final String username, final String password) {
        super(username, password);
        webTarget = getWebTargetInstance(path);
    }

    public Response update(final String id, final User newUser) {
        return webTarget.path(id).request(MediaType.APPLICATION_JSON).put(Entity.entity(newUser, MediaType.APPLICATION_JSON));
    }

}
