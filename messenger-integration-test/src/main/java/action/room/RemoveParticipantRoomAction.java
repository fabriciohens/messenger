package action.room;

import action.ApiClientAction;
import com.messenger.model.Room;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class RemoveParticipantRoomAction extends ApiClientAction {

    private final String path = "/rooms";
    private final WebTarget webTarget;

    public RemoveParticipantRoomAction(final String username, final String password) {
        super(username, password);
        webTarget = getWebTargetInstance(path);
    }

    public Response removeParticipant(final String idRoom, final String idPart) {
        return webTarget
                .path(idRoom)
                .path("/remove-participant")
                .path(idPart)
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.entity(Room.class, MediaType.APPLICATION_JSON));
    }

}
