package com.example.daw.tri.Objects;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by EN on 16.4.2015.
 */
public class Day {
    Long id;
    Date day;

    public Day(Long id, String day) throws ParseException {
        this.id = id;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        this.day = dateFormat.parse(day);
    }

    public Long getId() {
        return id;
    }

    public String getDate(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        return dateFormat.format(day);
    }

    @Override
    public String toString(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        return "Show sections on "+dateFormat.format(day);
    }
}
