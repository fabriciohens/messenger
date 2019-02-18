package action.room;

import action.ApiClientAction;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class DeleteRoomAction extends ApiClientAction {

    private final String path = "/rooms";
    private final WebTarget webTarget;

    public DeleteRoomAction(final String username, final String password) {
        super(username, password);
        webTarget = getWebTargetInstance(path);
    }

    public Response delete(final String id) {
        return webTarget.path(id).request().accept(MediaType.APPLICATION_JSON).delete();
    }

}
