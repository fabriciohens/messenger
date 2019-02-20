package action.room;

import action.ApiClientAction;
import com.messenger.model.Message;
import com.messenger.utils.SearchType;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class SearchRoomAction extends ApiClientAction {

    private final String path = "/rooms/search";
    private final WebTarget webTarget;

    public SearchRoomAction(final String username, final String password) {
        super(username, password);
        webTarget = getWebTargetInstance(path);
    }

    public Response searchRoom(final SearchType searchType, final String searchParam) {
        return webTarget
                .queryParam("searchType", searchType)
                .queryParam("searchParam", searchParam)
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.entity(null, MediaType.APPLICATION_JSON));
    }

}
