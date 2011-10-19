/**
 * Copyright (C) 2011 Talend Inc. - www.talend.com
 */
package oauth.common;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ReservationConfirmation {
	
	private String address;
    private int hour;
	
    public ReservationConfirmation() {
        
    }
    
    public ReservationConfirmation(String address, int hour) {
        this.address = address;
        this.hour = hour;
    }
    
	public void setHour(int hour) {
		this.hour = hour;
	}
	public int getHour() {
		return hour;
	}
    public void setAddress(String address) {
        this.address = address;
    }
    public String getAddress() {
        return address;
    }
}
