package action.room;

import action.ApiClientAction;
import com.messenger.model.Room;
import com.messenger.model.User;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class CreateRoomAction extends ApiClientAction {

    private final String path = "/rooms";
    private final WebTarget webTarget;

    public CreateRoomAction(final String username, final String password) {
        super(username, password);
        webTarget = getWebTargetInstance(path);
    }

    public Response create(final Room room) {
        return webTarget.request(MediaType.APPLICATION_JSON).post(Entity.entity(room, MediaType.APPLICATION_JSON));
    }

}
