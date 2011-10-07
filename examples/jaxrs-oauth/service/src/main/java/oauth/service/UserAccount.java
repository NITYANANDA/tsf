package oauth.service;

import oauth.common.Calendar;

public class UserAccount {
    private String name;
    private String password;
    private Calendar calendar;

    public UserAccount(String name, String password) {
    	this.name = name;
    	this.password = password;
    }
    
    public String getName() {
    	return name;
    }
    
    public String getPassword() {
    	return password;
    }
    
	public void setCalendar(Calendar calendar) {
		this.calendar = calendar;
	}

	public Calendar getCalendar() {
		return calendar;
	}
}
