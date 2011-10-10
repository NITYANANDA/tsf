package oauth.thirdparty;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import net.oauth.OAuth;
import net.oauth.OAuthAccessor;
import net.oauth.OAuthConsumer;
import net.oauth.OAuthMessage;
import oauth.common.Calendar;
import oauth.common.CalendarEntry;

import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.cxf.jaxrs.ext.form.Form;

@Path("reserve")
public class RestaurantReservationService {
	
	@Context
	private SecurityContext sc;
	@Context
	private UriInfo ui;
	
	private Map<String, Map<String, ReservationRequest>> requests = new
	    HashMap<String, Map<String, ReservationRequest>>();
	
	private WebClient socialService;
    private WebClient accessTokenService;
    private WebClient requestTokenService;
    private String authorizationServiceURI;
    private String consumerId;
    private String consumerSecret;
    
    private RestaurantService restaurantService;
    
    public void setSocialService(WebClient socialService) {
		this.socialService = socialService;
	}
    
    public void setAccessTokenService(WebClient ats) {
		this.accessTokenService = ats;
	}
    
    public void setRequestTokenService(WebClient rts) {
		this.requestTokenService = rts;
	}

	public void setRestaurantService(RestaurantService restaurantService) {
		this.restaurantService = restaurantService;
	}

	@GET
	@Path("complete")
    public String completeReservation(@QueryParam("oauth_token") String token,
    		                          @QueryParam("oauth_verifier") String verifier) {
		
		String userName = sc.getUserPrincipal().getName();
		Map<String, ReservationRequest> userRequests = requests.get(userName);
		if (userRequests == null) {
			throw new WebApplicationException(500);
		}
		ReservationRequest request = userRequests.get(token);
		if (request == null) {
			throw new WebApplicationException(500);
		}
		
		Token accessToken = getAccessToken(request.getRequestToken(), verifier);
		socialService.resetQuery().query("user", userName);
		
		String authHeader = createAuthorizationHeader(accessToken, "GET",
				socialService.getCurrentURI().toString());
		socialService.header("Authorization", authHeader);
		
		Calendar c = socialService.get(Calendar.class);
		
    	for (int i = request.getFromHour(); i < request.getToHour(); i++) {
    		CalendarEntry entry = c.getEntry(i);
    		if (entry.getEventDescription() == null 
    				&& restaurantService.reserveAt(request.getReserveName(), 
    						                       request.getContactPhone(), 
    						                       i)) {
    			return restaurantService.getAddress();
    		}
    	}
    	return "No reservation is possible";
    }
	
	@POST
	@Path("table")
    public Response reserveTableBetween(@FormParam("name") String name,
    		                            @FormParam("phone") String phone,
    		                            @FormParam("from") int from, 
    		                            @FormParam("to") int to) {
		
		Token requestToken = getRequestToken();
		
		String userName = sc.getUserPrincipal().getName();
		ReservationRequest request = new ReservationRequest();
		request.setReserveName(name);
		request.setContactPhone(phone);
		request.setFromHour(from);
		request.setToHour(to);
		request.setRequestToken(requestToken);
	
		synchronized (requests) {
			Map<String, ReservationRequest> userRequests = requests.get(userName);
			if (userRequests == null) {
				userRequests = new HashMap<String, ReservationRequest>(); 
				requests.put(userName, userRequests);
			}
			userRequests.put(requestToken.getToken(), request);
		}
		
		
    	// Create a request token request and redirect
		return Response.status(302).header("Location", 
				getAuthorizationServiceURI(requestToken.getToken())).build();
    }
    
	
	private URI getAuthorizationServiceURI(String token) {
	    return UriBuilder.fromUri(authorizationServiceURI).
		    queryParam("oauth_token", token).build();
	                                       
	}
	
	private Token getRequestToken() {
	    URI callback = ui.getAbsolutePathBuilder().path("complete").build();
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
	
	private Token getAccessToken(Token requestToken, String verifier) {
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
	
	private String createAuthorizationHeader(Token token, String method, String requestURI) {
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
	

	public void setRequestTokenURI(String uri) {
		this.authorizationServiceURI = uri;
	}
	
	public void setConsumerId(String consumerId) {
		this.consumerId = consumerId;
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
