package oauth.manager;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;

import org.apache.cxf.rs.security.oauth.data.Client;

@Path("registerProvider")
public class ThirdPartyRegistrationService {
	private static final String DEFAULT_CLIENT_ID = "123456789";
	private OAuthManager manager;
	
	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Path("/")
	public String register(@FormParam("appName") String appName, 
			               @FormParam("appURI") String appURI, 
			               @FormParam("password") String password) {
		String clientId = generateClientId();
		Client newClient = new Client(clientId, password, appName, appURI);
		manager.registerClient(newClient);
		return clientId;
	}

	public String generateClientId() {
		return DEFAULT_CLIENT_ID;
	}
	
	public void setManager(OAuthManager manager) {
		this.manager = manager;
	}
}
