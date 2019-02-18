package action.user;

import action.ApiClientAction;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class DeleteUserAction extends ApiClientAction {

    private final String path = "/users";
    private final WebTarget webTarget;

    public DeleteUserAction(final String username, final String password) {
        super(username, password);
        webTarget = getWebTargetInstance(path);
    }

    public Response delete(final String id) {
        return webTarget.path(id).request().accept(MediaType.APPLICATION_JSON).delete();
    }

}
