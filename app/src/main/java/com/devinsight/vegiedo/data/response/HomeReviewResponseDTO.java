package com.devinsight.vegiedo.data.response;

import com.devinsight.vegiedo.data.ui.home.HomeReviewUiData;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class HomeReviewResponseDTO {


    @Expose
    @SerializedName("reviews")private List<HomeReviewUiData> reviews;

    public HomeReviewResponseDTO(List<HomeReviewUiData> reviews) {
        this.reviews = reviews;
    }

    public List<HomeReviewUiData> getReviews() {
        return reviews;
    }

    public void setReviews(List<HomeReviewUiData> reviews) {
        this.reviews = reviews;
    }
}
