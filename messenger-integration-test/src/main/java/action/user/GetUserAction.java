package action.user;

import action.ApiClientAction;
import com.messenger.model.User;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class GetUserAction extends ApiClientAction {

    private final String path = "/users";
    private final WebTarget webTarget;

    public GetUserAction(final String username, final String password) {
        super(username, password);
        webTarget = getWebTargetInstance(path);
    }

    public Response get(final String id) {
        return webTarget.path(id).request().accept(MediaType.APPLICATION_JSON).get();
    }

}
