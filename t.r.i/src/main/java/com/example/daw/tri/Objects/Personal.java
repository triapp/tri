package com.example.daw.tri.Objects;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by EN on 17.4.2015.
 */
public class Personal {
    Long id;
    String name;
    Date timeFrom;
    Date timeTo;


    public Personal(Long id, String name, String timeTo, String timeFrom) throws ParseException {
        this.id = id;
        this.name = name;
        SimpleDateFormat time_to = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        SimpleDateFormat time_from = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        this.timeTo = time_to.parse(timeTo);
        this.timeFrom = time_from.parse(timeFrom);

    }
}
