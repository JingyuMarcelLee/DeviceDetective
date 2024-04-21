package com.devicedetective.server;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("test")
public class Location {

    @Id
    private Object id;
    private String clientId;
    private String latitude;
    private String longitude;

    public Location() {}

    public Object getId() { return id;}

    public void setId(Object id) { this.id = id;}

    public String getClientId() { return clientId; }

    public void setClientId(String clientId) { this.clientId = clientId; }

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