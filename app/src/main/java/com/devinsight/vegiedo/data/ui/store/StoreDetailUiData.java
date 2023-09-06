package com.devinsight.vegiedo.data.ui.store;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class StoreDetailUiData {
   private Long storeId;
    private String storeName;
    private String storeImage;
    private String address;
    private Integer stars;
    private Integer reviewCount;
    private List<String> tags;
    private float latitude;
    private float longitude;
    private boolean like;
    private boolean stamp;
    private List<String> images;

}
