package action.message;

import action.ApiClientAction;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class FindAllMessagesAction extends ApiClientAction {

    private final String path = "/messages";
    private final WebTarget webTarget;

    public FindAllMessagesAction(final String username, final String password) {
        super(username, password);
        webTarget = getWebTargetInstance(path);
    }

    public Response findAll() {
        return webTarget.request().accept(MediaType.APPLICATION_JSON).get();
    }

}
