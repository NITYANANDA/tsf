package oauth.service;

import java.util.HashMap;
import java.util.Map;

public class UserAccounts {

	private Map<String, UserAccount> accounts = new HashMap<String, UserAccount>();
	
	public void setAccount(String userName, UserAccount account) {
		accounts.put(userName, account);
	}
	
	public UserAccount getAccount(String name) {
		return accounts.get(name);
	}
	
}
