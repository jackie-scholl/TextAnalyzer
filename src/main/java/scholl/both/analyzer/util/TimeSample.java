package scholl.both.analyzer.util;

import java.util.Calendar;

public class TimeSample extends Sample<Calendar> {    
    public DoubleSample getDayOfWeek() {
        DoubleSample sample = new DoubleSample();
        
        for (Calendar c : this) {
            sample.add(getHours(c));
        }
        
        return sample;
    }
    
    public DoubleSample getMillisecondsSinceEpoch() {
        DoubleSample sample = new DoubleSample();
        
        for (Calendar c : this) {
            sample.add(c.getTimeInMillis());
        }
        
        return sample;
    }
    
    public DoubleSample getHourOfDay() {
        DoubleSample sample = new DoubleSample();
        
        for (Calendar c : this) {
            sample.add(getHours(c));
        }
        
        return sample;
    }
    
    public static double getMilliseconds(Calendar c) {
        return c.get(Calendar.MILLISECOND);
    }
    
    public static double getSeconds(Calendar c) {
        return c.get(Calendar.SECOND) + getMilliseconds(c)/1000;
    }
    
    public static double getMinutes(Calendar c) {
        return c.get(Calendar.MINUTE) + getSeconds(c)/60;
    }
    
    public static double getHours(Calendar c) {
        return c.get(Calendar.HOUR) + getMinutes(c)/60;
    }
    
    public static double getDaysOfWeek(Calendar c) {
        return c.get(Calendar.DAY_OF_WEEK) + getHours(c)/24;
    }
    
    public static double getDaysOfYear(Calendar c) {
        return c.get(Calendar.DAY_OF_YEAR) + getHours(c)/24;
    }
}
