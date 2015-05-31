package com.orl.chigger.tri.Objects;

import java.text.ParseException;
import java.util.Date;

/**
 * Created by EN on 17.4.2015.
 */
public class Personal {
    Long id;
    String sectionName;
    String presentationName;
    Date timeFrom;
    Date timeTo;


    public Personal(Long id, String sectionName, String presentationName, String timeTo, String timeFrom) throws ParseException {
        this.id = id;
        this.sectionName = sectionName;
        this.presentationName = presentationName;
       // SimpleDateFormat time_to = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
       // SimpleDateFormat time_from = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        //) this.timeTo = time_to.parse(timeTo);
       // this.timeFrom = time_from.parse(timeFrom);
    }

    @Override
    public String toString() {
        return sectionName;
    }
}
