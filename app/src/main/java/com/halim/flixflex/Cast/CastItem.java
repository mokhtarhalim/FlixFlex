package com.halim.flixflex.Cast;

import java.io.Serializable;

//POJO class of Cast
public class CastItem implements Serializable {

    private final int id;
    private final String name;
    private final String profilePath;

    public CastItem(int id, String name, String profilePath) {
        this.id = id;
        this.name = name;
        this.profilePath = profilePath;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getProfilePath() {
        return profilePath;
    }

}
