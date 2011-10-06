package oauth.service;

import oauth.common.Calendar;

public class UserAccount {
    private Calendar calendar;

	public void setCalendar(Calendar calendar) {
		this.calendar = calendar;
	}

	public Calendar getCalendar() {
		return calendar;
	}
}
