package oauth.service;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;

@Path("registerUser")
public class UserRegistrationService {

	private UserAccounts accounts;
	
	public UserRegistrationService() {
	}
	
	public void setAccounts(UserAccounts accounts) {
		this.accounts = accounts;
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Path("/")
	public void register(String name, String password) {
		if (accounts.getAccount(name) != null) {
			throw new WebApplicationException(400);
		}
		accounts.setAccount(name, new UserAccount(name, password));
	}
}