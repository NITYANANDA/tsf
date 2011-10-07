package oauth.thirdparty;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

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

import oauth.common.Calendar;
import oauth.common.CalendarEntry;
import oauth.manager.ThirdPartyAccessService;

import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.cxf.jaxrs.ext.form.Form;
import org.apache.cxf.rs.security.oauth.services.AccessTokenService;

@Path("reserve")
public class RestaurantReservationService {
	
	@Context
	private SecurityContext sc;
	@Context
	private UriInfo ui;
	
	private AtomicInteger requestCounter = new AtomicInteger();
	
	private Map<String, Map<Integer, ReservationRequest>> requests = new
	    HashMap<String, Map<Integer, ReservationRequest>>();
	
	// thread-safe
    private ThirdPartyAccessService socialService;
    // thread-safe
    private AccessTokenService accessTokenService;
    
    
    private RestaurantService restaurantService;
    
    private String requestTokenURI;
    private String consumerId;
    
	public void setSocialService(ThirdPartyAccessService socialService) {
		this.socialService = socialService;
	}

	public void setRestaurantService(RestaurantService restaurantService) {
		this.restaurantService = restaurantService;
	}

	@GET
	@Path("complete")
    public String completeReservation(@QueryParam("oauth_token") String token,
    		                          @QueryParam("oauth_secret") String secret,
    		                          @QueryParam("state") Integer requestId) {
		
		String userName = sc.getUserPrincipal().getName();
		Map<Integer, ReservationRequest> userRequests = requests.get(userName);
		if (userRequests == null) {
			throw new WebApplicationException(500);
		}
		ReservationRequest request = userRequests.get(requestId);
		if (request == null) {
			throw new WebApplicationException(500);
		}
		
		Form form = getAccessToken(token, secret);
		String header = createAuthorizationHeader(form.getData().getFirst("oauth_token"),
				                                  form.getData().getFirst("oauth_secret"));
		WebClient.client(accessTokenService).header("Authorization", header);
		
    	Calendar c = socialService.getUserCalendar(userName);
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
		String userName = sc.getUserPrincipal().getName();
		ReservationRequest request = new ReservationRequest();
		request.setReserveName(name);
		request.setContactPhone(phone);
		request.setFromHour(from);
		request.setToHour(to);
	
		Integer requestId = requestCounter.addAndGet(1);
		
		synchronized (requests) {
			Map<Integer, ReservationRequest> userRequests = requests.get(userName);
			if (userRequests == null) {
				userRequests = new HashMap<Integer, ReservationRequest>(); 
				requests.put(userName, userRequests);
			}
			userRequests.put(requestId, request);
		}
		
    	// Create a request token request and redirect
		return Response.status(302).header("Location", 
				getRequestTokenServiceURI(requestId)).build();
    }
    
	private Form getAccessToken(String requestToken, String requestTokenSecret) {
		// create Authorization header
		String header = createAuthorizationHeader(requestToken, requestTokenSecret);
		WebClient.client(accessTokenService).header("Authorization", header);
	    Response response = accessTokenService.getAccessToken();
	    // should probably return Form
	    return new Form();
	}
	
	private String createAuthorizationHeader(String token, String secret) {
		return null;                                 
	}
	
	private URI getRequestTokenServiceURI(Integer requestId) {
	    URI callback = ui.getAbsolutePathBuilder().path("complete").build();
		return UriBuilder.fromUri(requestTokenURI).
		    queryParam("oauth_consumer_key", consumerId).
		    queryParam("oauth_callback", callback.toString()).
		    queryParam("state", requestId.toString()).build();
	                                       
	}

	public void setRequestTokenURI(String requestTokenURI) {
		this.requestTokenURI = requestTokenURI;
	}
	
	public void setConsumerId(String consumerId) {
		this.consumerId = consumerId;
	}

	
}
