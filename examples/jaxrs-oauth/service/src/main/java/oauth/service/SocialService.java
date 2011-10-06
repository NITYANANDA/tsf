package oauth.service;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;

import oauth.common.Calendar;

@Path("social")
public class SocialService {

	@Context
	private SecurityContext context;
	
	private UserAccounts accounts = new UserAccounts();
	
	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Path("calendar")
	public void updateUserCalendar(int hour, String eventDescription) {
		String userName = context.getUserPrincipal().getName();
		UserAccount account = accounts.getAccount(userName);
		account.getCalendar().getEntry(hour).setEventDescription(eventDescription);
	}

	@GET
	@Path("calendar")
	public Calendar getUserCalendar(String userName) {
		return accounts.getAccount(userName).getCalendar();
	}
}
