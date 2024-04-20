package com.devicedetective.server;

import org.springframework.data.mongodb.core.mapping.Document;

@Document("test")
public class Location {
    private String latitude;

    public Location() {}
    private String longitude;

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) { this.latitude = latitude; }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }
}