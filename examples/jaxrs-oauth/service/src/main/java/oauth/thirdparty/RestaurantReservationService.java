package oauth.thirdparty;

import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;

import oauth.common.Calendar;
import oauth.common.CalendarEntry;
import oauth.manager.ThirdPartyAccessService;

@Path("reserve")
public class RestaurantReservationService {
	
	@Context
	private SecurityContext sc; 
	
    private ThirdPartyAccessService socialService;
    private RestaurantService restaurantService;
    
	public void setSocialService(ThirdPartyAccessService socialService) {
		this.socialService = socialService;
	}

	public void setRestaurantService(RestaurantService restaurantService) {
		this.restaurantService = restaurantService;
	}

	@POST
    public String reserveTableBetween(@FormParam("name") String name,
    		                          @FormParam("phone") String phone,
    		                          @FormParam("from") int from, 
    		                          @FormParam("to") int to) {
    	Calendar c = socialService.getUserCalendar(sc.getUserPrincipal().getName());
    	for (int i = from; i < to; i++) {
    		CalendarEntry entry = c.getEntry(i);
    		if (entry.getEventDescription() == null 
    				&& restaurantService.reserveAt(name, phone, i)) {
    			return restaurantService.getAddress();
    		}
    	}
    	return null;
    }
    
}
