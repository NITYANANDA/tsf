package oauth.thirdparty;

import java.util.concurrent.atomic.AtomicBoolean;

import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

@Path("reception")
public class RestaurantService {

	private AtomicBoolean availableHours[] = new AtomicBoolean[24];
	
	@POST
	public boolean reserveAt(@FormParam("name") String name,
                             @FormParam("phone") String phone, 
                             @FormParam("hour") int hour) {
		return availableHours[hour].compareAndSet(false, true);
	}
	
	public String getAddress() {
		return "";
	}
	
}
