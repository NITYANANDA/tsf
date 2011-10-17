/**
 * Copyright (C) 2011 Talend Inc. - www.talend.com
 */
package oauth.thirdparty;

import java.net.URI;

import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.cxf.rs.security.oauth.client.OAuthClientUtils;
import org.apache.cxf.rs.security.oauth.client.OAuthClientUtils.Consumer;
import org.apache.cxf.rs.security.oauth.client.OAuthClientUtils.Token;

public class OAuthClientManager {

	private static final String DEFAULT_CLIENT_ID = "123456789";
	private static final String DEFAULT_CLIENT_SECRET = "987654321";
	
	private WebClient accessTokenService;
    private WebClient requestTokenService;
    private String authorizationServiceURI;
    private Consumer consumer = new Consumer(DEFAULT_CLIENT_ID, DEFAULT_CLIENT_SECRET);
    
	public OAuthClientManager() {
		
	}
	
	public URI getAuthorizationServiceURI(String token) {
	    return OAuthClientUtils.getAuthorizationURI(authorizationServiceURI, token);
	}
	
	public Token getRequestToken(URI callback) {
	    return OAuthClientUtils.getRequestToken(requestTokenService, consumer, callback, null);
	}
	
	public Token getAccessToken(Token requestToken, String verifier) {
	    return OAuthClientUtils.getAccessToken(accessTokenService, consumer, requestToken, verifier);
	}
	
	public String createAuthorizationHeader(Token token, String method, String requestURI) {
		return OAuthClientUtils.createAuthorizationHeader(consumer, token, method, requestURI);
	}
	
	public void setAccessTokenService(WebClient ats) {
		this.accessTokenService = ats;
	}
    
    public void setRequestTokenService(WebClient rts) {
		this.requestTokenService = rts;
	}
	
	public void setAuthorizationURI(String uri) {
		this.authorizationServiceURI = uri;
	}
	
}
