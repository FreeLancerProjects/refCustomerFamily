package com.refCustomerFamily.activities_fragments.stores.google_place_modul.models;

import java.io.Serializable;

public class HourModel implements Serializable {
    private String day;
    private String time;

    public HourModel(String day, String time) {
        this.day = day;
        this.time = time;
    }

    public String getDay() {
        return day;
    }

    public String getTime() {
        return time;
    }
}
