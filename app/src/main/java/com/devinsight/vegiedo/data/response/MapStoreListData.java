package com.devinsight.vegiedo.data.response;

import java.util.List;

public class MapStoreListData {

    private Long storeId;
    private String storeName;
    private String address;
    private String detailAddress;
    private Double distance;  // 이 데이터 타입은 'm' 단위로 제공되는 거리를 나타내기 위해 사용됩니다.
    private List<String> tags;
    private Double stars;  // 별점은 소수점이 포함될 수 있으므로 Double로 표현합니다.
    private Boolean like;
    private Double x;      // x 좌표
    private Double y;
}
