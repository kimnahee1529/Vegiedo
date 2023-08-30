package com.devinsight.vegiedo.view.search;

public class Boundary {
    private float minLat;
    private float minLon;
    private float maxLat;
    private float maxLon;

    public float getMinLat() {
        return minLat;
    }

    public void setMinLat(float minLat) {
        this.minLat = minLat;
    }

    public float getMinLon() {
        return minLon;
    }

    public void setMinLon(float minLon) {
        this.minLon = minLon;
    }

    public float getMaxLat() {
        return maxLat;
    }

    public void setMaxLat(float maxLat) {
        this.maxLat = maxLat;
    }

    public float getMaxLon() {
        return maxLon;
    }

    public void setMaxLon(float maxLon) {
        this.maxLon = maxLon;
    }

    public Boundary(float minLat, float minLon, float maxLat, float maxLon) {
        this.minLat = minLat;
        this.minLon = minLon;
        this.maxLat = maxLat;
        this.maxLon = maxLon;
    }
}

