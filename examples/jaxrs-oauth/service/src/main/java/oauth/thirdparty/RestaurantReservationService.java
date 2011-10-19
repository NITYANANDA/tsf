/**
 * Copyright (C) 2011 Talend Inc. - www.talend.com
 */
package oauth.thirdparty;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriInfo;

import oauth.common.Calendar;
import oauth.common.CalendarEntry;
import oauth.common.ReservationConfirmation;

import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.cxf.jaxrs.ext.form.Form;
import org.apache.cxf.rs.security.oauth.client.OAuthClientUtils.Token;

@Path("reserve")
public class RestaurantReservationService {
	
	@Context
	private SecurityContext sc;
	@Context
	private UriInfo ui;
	
	private Map<String, Map<String, ReservationRequest>> requests = new
	    HashMap<String, Map<String, ReservationRequest>>();
	
	private WebClient socialService;
    private WebClient restaurantService;
    
    private OAuthClientManager manager;
    
    public void setOAuthClientManager(OAuthClientManager manager) {
    	this.manager = manager;
    }
    
    public void setSocialService(WebClient socialService) {
		this.socialService = socialService;
	}
    
    public void setRestaurantService(WebClient restaurantService) {
		this.restaurantService = restaurantService;
	}

	@GET
	@Path("complete")
	@Produces({"text/html", "application/xml;q=0.9" })
	public ReservationConfirmation completeReservation(@QueryParam("oauth_token") String token,
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
		socialService.resetQuery().query("user", userName);
		
		Token accessToken = manager.getAccessToken(request.getRequestToken(), verifier);
		String authHeader = manager.createAuthorizationHeader(accessToken, "GET",
				socialService.getCurrentURI().toString());
		socialService.header("Authorization", authHeader);
		
		Calendar c = socialService.get(Calendar.class);
		
    	CalendarEntry entry = c.getEntry(request.getHour());
		if (entry.getEventDescription() == null) { 
			String address = restaurantService.post(new Form().set("name", request.getReserveName()) 
					                     .set("phone", request.getContactPhone()) 
					                     .set("hour", request.getHour()),
					                      String.class);
			return new ReservationConfirmation(address, request.getHour());
		} else {
		    return null;
		}
    }
	
	@POST
	@Path("table")
    public Response reserveTableBetween(@FormParam("name") String name,
    		                            @FormParam("phone") String phone,
    		                            @FormParam("hour") int hour) {
		
		URI callback = ui.getBaseUriBuilder().path("reserve").path("complete").build();
		Token requestToken = manager.getRequestToken(callback);
		
		String userName = sc.getUserPrincipal().getName();
		ReservationRequest request = new ReservationRequest();
		request.setReserveName(name);
		request.setContactPhone(phone);
		request.setHour(hour);
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
		return Response.seeOther(manager.getAuthorizationServiceURI(requestToken.getToken())).build();
    }
    
	
}
