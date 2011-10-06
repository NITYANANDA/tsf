package oauth.common;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Calendar {
    private CalendarEntry[] entries = new CalendarEntry[24];
    
    public Calendar() {
    	for (int i = 0; i < 24; i++) {
    		entries[i] = new CalendarEntry(i, null);
    	}
    }
    
    public void setEntry(CalendarEntry entry) {
    	validateHour(entry.getHour());
    	entries[entry.getHour()] = entry;
    }
    
    public CalendarEntry getEntry(int hour) {
    	validateHour(hour);
    	return entries[hour];
    }
    
    private static void validateHour(int hour) {
    	if (hour < 0 || hour > 23) {
    		throw new IllegalArgumentException("Wrong hour: " + hour);
    	}
    }
    
}
