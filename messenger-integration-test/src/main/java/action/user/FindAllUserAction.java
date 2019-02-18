package action.user;

import action.ApiClientAction;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class FindAllUserAction extends ApiClientAction {

    private final String path = "/users";
    private final WebTarget webTarget;

    public FindAllUserAction(final String username, final String password) {
        super(username, password);
        webTarget = getWebTargetInstance(path);
    }

    public Response findAll() {
        return webTarget.request().accept(MediaType.APPLICATION_JSON).get();
    }

}
