package com.orl.chigger.tri.Objects;

/**
 * Created by EN on 16.4.2015.
 */
public class Section {
    Long id;
    Long id_hall;
    Long id_day;
    String name;

    public Section(Long id,Long id_hall, Long id_day, String name) {
        this.id = id;
        this.id_hall = id_hall;
        this.id_day = id_day;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    @Override
    public String toString() {
        return "Tema:"+name;
    }
}
