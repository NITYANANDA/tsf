package oauth.manager;

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
		Client newClient = new Client(appName, appConnectURI, password);
		String clientId = manager.registerClient(newClient);
		return clientId;
	}

	public void setManager(OAuthManager manager) {
		this.manager = manager;
	}
}
