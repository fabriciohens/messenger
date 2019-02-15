package action;

import com.messenger.model.User;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class ApiClientUserAction extends ApiClient {

    private final String path = "/users";
    private final WebTarget webTarget;

    public ApiClientUserAction(final String username, final String password) {
        super(username, password);
        webTarget = getWebTargetInstance(path);
    }

    public Response get(final String id) {
        return webTarget.path(id).request().accept(MediaType.APPLICATION_JSON).get();
    }

    public Response post(final User user) {
        return webTarget.request(MediaType.APPLICATION_JSON).post(Entity.entity(user, MediaType.APPLICATION_JSON));
    }

}
