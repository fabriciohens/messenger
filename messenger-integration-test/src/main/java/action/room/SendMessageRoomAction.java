package action.room;

import action.ApiClientAction;
import com.messenger.model.Message;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class SendMessageRoomAction extends ApiClientAction {

    private final String path = "/rooms";
    private final WebTarget webTarget;

    public SendMessageRoomAction(final String username, final String password) {
        super(username, password);
        webTarget = getWebTargetInstance(path);
    }

    public Response sendMessage(final String idRoom, final Message message) {
        return webTarget
                .path(idRoom)
                .path("/send-message")
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.entity(message, MediaType.APPLICATION_JSON));
    }

}
