/**
 * Copyright (C) 2011 Talend Inc. - www.talend.com
 */
package oauth.manager;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;

import oauth.common.ConsumerRegistration;

import org.apache.cxf.rs.security.oauth.data.Client;

@Path("registerProvider")
public class ThirdPartyRegistrationService {
	private static final String DEFAULT_CLIENT_ID = "123456789";
	private static final String DEFAULT_CLIENT_SECRET = "987654321";
	private OAuthManager manager;
	
	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Path("/")
	public ConsumerRegistration register(@FormParam("appName") String appName, 
			                             @FormParam("appURI") String appURI) {
		String clientId = generateClientId(appName, appURI);
		String clientSecret = generateClientSecret();
		Client newClient = new Client(clientId, clientSecret, appName, appURI);
		manager.registerClient(newClient);
		return new ConsumerRegistration(clientId, clientSecret);
	}

	public String generateClientId(String appName, String appURI) {
	    // if appURI is not allowed to contain paths, example, it can only be
	    // www.mycompany.com, then appURI can be used as a consumer key
		return DEFAULT_CLIENT_ID;
	}
	
	public String generateClientSecret() {
        return DEFAULT_CLIENT_SECRET;
    }
	
	public void setDataProvider(OAuthManager manager) {
		this.manager = manager;
	}
}
