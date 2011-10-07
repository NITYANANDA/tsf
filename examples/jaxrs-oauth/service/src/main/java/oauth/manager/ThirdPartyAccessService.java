package oauth.manager;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

import oauth.common.Calendar;
import oauth.service.UserAccounts;

@Path("accounts")
public class ThirdPartyAccessService {

    private UserAccounts accounts;
	
	public void setAccounts(UserAccounts accounts) {
		this.accounts = accounts;
	}
	
	@GET
	@Path("calendar")
	public Calendar getUserCalendar(@QueryParam("user") String userName) {
		return accounts.getAccount(userName).getCalendar();
	}
	
}
