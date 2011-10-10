package oauth.manager;

import java.util.UUID;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;

import org.apache.cxf.rs.security.oauth.data.Client;

@Path("registerProvider")
public class ThirdPartyRegistrationService {
	
	private OAuthManager manager;
	
	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Path("/")
	public String register(String appName, String appConnectURI, String password) {
		String clientId = UUID.randomUUID().toString();
		Client newClient = new Client(clientId, password, appName, appConnectURI);
		manager.registerClient(newClient);
		return clientId;
	}

	public void setManager(OAuthManager manager) {
		this.manager = manager;
	}
}
