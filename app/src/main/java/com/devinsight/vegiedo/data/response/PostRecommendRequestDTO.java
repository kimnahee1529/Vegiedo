package com.devinsight.vegiedo.data.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PostRecommendRequestDTO {
    @Expose
    @SerializedName("likeReceiveCnt")private int likeReceiveCount;
    @Expose
    @SerializedName("like")private boolean like;

    public int getLikeReceiveCount() {
        return likeReceiveCount;
    }

    public void setLikeReceiveCount(int likeReceiveCount) {
        this.likeReceiveCount = likeReceiveCount;
    }

    public boolean isLike() {
        return like;
    }

    public void setLike(boolean like) {
        this.like = like;
    }

    public PostRecommendRequestDTO(int likeReceiveCount, boolean like) {
        this.likeReceiveCount = likeReceiveCount;
        this.like = like;
    }
}
