package action.room;

import action.ApiClientAction;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class FindUsersRoomsAction extends ApiClientAction {

    private final String path = "/users";
    private final WebTarget webTarget;

    public FindUsersRoomsAction(final String username, final String password) {
        super(username, password);
        webTarget = getWebTargetInstance(path);
    }

    public Response findUsersRooms(final String idUser) {
        return webTarget
                .path(idUser)
                .path("/rooms")
                .request()
                .accept(MediaType.APPLICATION_JSON)
                .get();
    }

}
