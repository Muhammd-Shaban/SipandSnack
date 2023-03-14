package com.cust.sipnsnack.Customers;

public class MyLocation {
    private double longitude;
    private double latitude;

    public MyLocation(double longitude, double latitude) {
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public MyLocation() { }



    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }
}
