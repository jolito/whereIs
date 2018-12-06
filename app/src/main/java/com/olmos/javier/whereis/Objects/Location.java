package com.olmos.javier.whereis.Objects;

/**
 * Created by JOL on 08/12/2015.
 */
public class Location {
    private String title;
    private String description;
    private double latitude;
    private double longitude;

    public Location(String title, String description, double latitude, double longitude) {
        this.title = title;
        this.description = description;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public String toString() {
        return "" + this.title + ": " + this.description + " (" + this.latitude + ", " + this.longitude + ")";
    }
}
