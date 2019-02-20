package action.room;

import action.ApiClientAction;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class FindAllRoomsAction extends ApiClientAction {

    private final String path = "/rooms/page/";
    private final WebTarget webTarget;

    public FindAllRoomsAction(final String username, final String password) {
        super(username, password);
        webTarget = getWebTargetInstance(path);
    }

    public Response findAll(int page) {
        return webTarget.path(String.valueOf(page)).request().accept(MediaType.APPLICATION_JSON).get();
    }

}
