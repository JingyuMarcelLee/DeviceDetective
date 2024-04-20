package com.devicedetective.server;

import org.springframework.data.mongodb.core.mapping.Document;

@Document("test")
public class Location {
    private double latitiude;
    private double longitude;

    public Location() {}

    public double getLatitiude() {
        return latitiude;
    }

    public void setLatitiude(double latitiude) {
        this.latitiude = latitiude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}