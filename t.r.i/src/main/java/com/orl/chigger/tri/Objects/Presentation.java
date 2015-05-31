package com.orl.chigger.tri.Objects;

/**
 * Created by EN on 17.4.2015.
 */
public class Presentation {
    Long id;
    String name;
    String author;

    public Presentation(Long id, String name, String author) {
        this.id = id;
        this.name = name;
        this.author = author;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAuthor() {
        return author;
    }

    @Override
    public String toString() {
        return "Prezentace:"+name+" od "+author;
    }
}
