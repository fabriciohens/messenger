package action.room;

import action.ApiClientAction;
import com.messenger.model.Room;
import com.messenger.model.User;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class UpdateRoomAction extends ApiClientAction {

    private final String path = "/rooms";
    private final WebTarget webTarget;

    public UpdateRoomAction(final String username, final String password) {
        super(username, password);
        webTarget = getWebTargetInstance(path);
    }

    public Response update(final String id, final Room newRoom) {
        return webTarget.path(id).request(MediaType.APPLICATION_JSON).put(Entity.entity(newRoom, MediaType.APPLICATION_JSON));
    }

}
