package com.devinsight.vegiedo.view.search;

public class LocationData {
    float latitude;
    float longitude;

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    public LocationData(float latitude, float longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
