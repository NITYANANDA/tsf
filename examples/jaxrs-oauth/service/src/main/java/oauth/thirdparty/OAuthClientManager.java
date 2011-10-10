package oauth.thirdparty;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.UriBuilder;

import net.oauth.OAuth;
import net.oauth.OAuthAccessor;
import net.oauth.OAuthConsumer;
import net.oauth.OAuthMessage;

import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.cxf.jaxrs.ext.form.Form;

public class OAuthClientManager {

	private static final String DEFAULT_CLIENT_ID = "123456789";
	private static final String DEFAULT_CLIENT_SECRET = "987654321";
	
	private WebClient accessTokenService;
    private WebClient requestTokenService;
    private String authorizationServiceURI;
    private String consumerId = DEFAULT_CLIENT_ID;
    private String consumerSecret = DEFAULT_CLIENT_SECRET;
	
	public OAuthClientManager() {
		
	}
	
	public URI getAuthorizationServiceURI(String token) {
	    return UriBuilder.fromUri(authorizationServiceURI).
		    queryParam("oauth_token", token).build();
	                                       
	}
	
	public Token getRequestToken(URI callback) {
	    Map<String, String> parameters = new HashMap<String, String>();
	    parameters.put(OAuth.OAUTH_CALLBACK, callback.toString());
	    parameters.put(OAuth.OAUTH_SIGNATURE_METHOD, "HMAC-SHA1");
	    parameters.put(OAuth.OAUTH_NONCE, UUID.randomUUID().toString());
	    parameters.put(OAuth.OAUTH_TIMESTAMP, String.valueOf(System.currentTimeMillis() / 1000));
	    parameters.put(OAuth.OAUTH_CONSUMER_KEY, consumerId);
	    
	    OAuthConsumer consumer = new OAuthConsumer(null, consumerId, consumerSecret, null);
        OAuthAccessor accessor = new OAuthAccessor(consumer);
        return getToken(requestTokenService, accessor, parameters);
        
	}
	
	public Token getAccessToken(Token requestToken, String verifier) {
	    Map<String, String> parameters = new HashMap<String, String>();
	    parameters.put(OAuth.OAUTH_CONSUMER_KEY, consumerId);
	    parameters.put(OAuth.OAUTH_TOKEN, requestToken.getToken());
	    parameters.put(OAuth.OAUTH_VERIFIER, verifier);
	    parameters.put(OAuth.OAUTH_SIGNATURE_METHOD, "HMAC-SHA1");
	    
	    OAuthConsumer consumer = new OAuthConsumer(null, consumerId, consumerSecret, null);
        OAuthAccessor accessor = new OAuthAccessor(consumer);
        accessor.requestToken = requestToken.getToken();
        accessor.tokenSecret = requestToken.getSecret();
        return getToken(accessTokenService, accessor, parameters);
    }
	
	public String createAuthorizationHeader(Token token, String method, String requestURI) {
		Map<String, String> parameters = new HashMap<String, String>();
	    parameters.put(OAuth.OAUTH_CONSUMER_KEY, consumerId);
	    parameters.put(OAuth.OAUTH_TOKEN, token.getToken());
	    parameters.put(OAuth.OAUTH_SIGNATURE_METHOD, "HMAC-SHA1");
	    parameters.put(OAuth.OAUTH_NONCE, UUID.randomUUID().toString());
	    parameters.put(OAuth.OAUTH_TIMESTAMP, String.valueOf(System.currentTimeMillis() / 1000));
	    
	    OAuthConsumer consumer = new OAuthConsumer(null, consumerId, consumerSecret, null);
        OAuthAccessor accessor = new OAuthAccessor(consumer);
        accessor.accessToken = token.getToken();
        accessor.tokenSecret = token.getSecret();
        
        try {
	        OAuthMessage msg = accessor.newRequestMessage(method, requestURI, parameters.entrySet());
	        return msg.getAuthorizationHeader(null);
        } catch (Exception ex) {
        	throw new WebApplicationException(500);
        }
	}
	
	private static Token getToken(WebClient tokenService, OAuthAccessor accessor,
			Map<String, String> parameters) {
		try {
	        OAuthMessage msg = accessor
		            .newRequestMessage("POST", tokenService.getBaseURI().toString(), 
		            		parameters.entrySet());
	        String header = msg.getAuthorizationHeader(null);
	        tokenService.header("Authorization", header);
	        Form form = tokenService.post(null, Form.class);
	        return new Token(form.getData().getFirst("oauth_token"),
	        		         form.getData().getFirst("oauth_token_secret"));
        } catch (Exception ex) {
        	throw new WebApplicationException(500);
        }
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
	
	public void setConsumerId(String consumerId) {
		this.consumerId = consumerId;
	}
	
	public void setConsumerSecret(String consumerSecret) {
		this.consumerSecret = consumerSecret;
	}
	
    public static class Token {
		
		private String token;
		private String secret;
		
		public Token(String token, String secret) {
			this.token = token;
			this.secret = secret;
		}
		public String getToken() {
			return token;
		}

		public String getSecret() {
			return secret;
		}
		
		
	}
}
