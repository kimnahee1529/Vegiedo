package com.devinsight.vegiedo.view.search;

import java.util.List;

public class FilterData {

    Integer distance;
    List<String> tags;

    public FilterData() {
    }

    public Integer getDistance() {
        return distance;
    }

    public void setDistance(Integer distance) {
        this.distance = distance;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public FilterData(Integer distance, List<String> tags) {
        this.distance = distance;
        this.tags = tags;
    }
}
