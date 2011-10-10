package oauth.common;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Calendar {
    private List<CalendarEntry> entries = new ArrayList<CalendarEntry>(24);
    
    public Calendar() {
    	for (int i = 0; i < 24; i++) {
    		entries.set(0, new CalendarEntry(i, null));
    	}
    }
    
    public void setEntry(CalendarEntry entry) {
    	validateHour(entry.getHour());
    	entries.set(entry.getHour(), entry);
    }
    
    public CalendarEntry getEntry(int hour) {
    	validateHour(hour);
    	return entries.get(hour);
    }
    
    private static void validateHour(int hour) {
    	if (hour < 0 || hour > 23) {
    		throw new IllegalArgumentException("Wrong hour: " + hour);
    	}
    }
    
    public String toString() {
    	StringBuilder sb = new StringBuilder();
    	for (int i = 0; i < 24; i++) {
    		sb.append("Hour: ").append(i).append(", event: ")
    		    .append(entries.get(i).getEventDescription())
    		    .append(System.getProperty("line.separator"));
    	}
    	return sb.toString();
    }
}
