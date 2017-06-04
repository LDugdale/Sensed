package com.example.android.sensed.entry;

import android.icu.text.SimpleDateFormat;

/**
 * @author Laurie Dugdale
 */

public abstract class Entry {

    private String dateTime;
    private int happiness; // must be --- 0 =< happiness <= 100
    private double latitude;
    private double longitude;

    public Entry(String dateTime, int happiness, double latitude, double longitude) {
        this.dateTime = dateTime;
        this.happiness = happiness;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getDate() {
        return dateTime;
    }

    public void setDate(String date) {
        this.dateTime = date;
    }

    public int getHappiness() {
        return happiness;
    }

    public void setHappiness(int happiness) {
        this.happiness = happiness;
    }
}
