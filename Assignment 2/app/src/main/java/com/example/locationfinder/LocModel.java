package com.example.locationfinder;

import java.io.Serializable;

public class LocModel implements Serializable {
    private int id;
    private String address;
    private Double latitude;
    private Double longitude;

    public LocModel(int id, String address, Double latitude, Double longitude){
        this.id = id;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
    }

//    get and set methods for the location model

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAddress() { return this.address; }

    public void setAddress(String address) { this.address = address; }

    public double getLatitude() { return this.latitude; }

    public void setLatitude(double latitude) { this.latitude = latitude; }

    public double getLongtitude() { return this.longitude; }

    public void setLongtitude(double longitude) { this.longitude = longitude; }

}
