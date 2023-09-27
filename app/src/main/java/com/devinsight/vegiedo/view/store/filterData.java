package com.devinsight.vegiedo.view.store;

import java.util.List;

public class filterData {

    int distance;
    float latitude;
    float longitude;
    List<String> tags;

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

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

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public filterData(int distance, float latitude, float longitude, List<String> tags) {
        this.distance = distance;
        this.latitude = latitude;
        this.longitude = longitude;
        this.tags = tags;
    }
}
