package oauth.thirdparty;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ReservationRequest {
    private String reserveName;
    private String contactPhone;
    private int fromHour;
    private int toHour;
	public void setReserveName(String reserveName) {
		this.reserveName = reserveName;
	}
	public String getReserveName() {
		return reserveName;
	}
	public void setContactPhone(String contactPhone) {
		this.contactPhone = contactPhone;
	}
	public String getContactPhone() {
		return contactPhone;
	}
	public void setFromHour(int fromHour) {
		this.fromHour = fromHour;
	}
	public int getFromHour() {
		return fromHour;
	}
	public void setToHour(int toHour) {
		this.toHour = toHour;
	}
	public int getToHour() {
		return toHour;
	}
}
