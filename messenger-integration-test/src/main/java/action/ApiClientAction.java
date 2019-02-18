package action;

import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.UriBuilder;
import java.net.URI;

public class ApiClientAction {

    private final String server = "http://localhost:8080";
    private final Client client;

    public ApiClientAction(final String username, final String password) {
        HttpAuthenticationFeature feature = HttpAuthenticationFeature
                .basicBuilder()
                .nonPreemptive()
                .credentials(username, password)
                .build();

        ClientConfig config = new ClientConfig();
        config.register(feature);
        client = ClientBuilder.newClient(config);
    }

    public WebTarget getWebTargetInstance(final String path){
        return client.target(formURI(path));
    }

    private URI formURI(final String path) {
        return UriBuilder.fromUri(server).path(path).build();
    }

}
