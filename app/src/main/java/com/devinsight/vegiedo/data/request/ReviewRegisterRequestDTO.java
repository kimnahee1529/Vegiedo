package com.devinsight.vegiedo.data.request;

import java.util.List;

public class ReviewRegisterRequestDTO {

    private String content;
    private Integer stars;
    private List<String> images;

    // 기본 생성자
    public ReviewRegisterRequestDTO() {}

    // 모든 필드를 매개변수로 갖는 생성자
    public ReviewRegisterRequestDTO(String content, Integer stars, List<String> images) {
        this.content = content;
        this.stars = stars;
        this.images = images;
    }

    // Getter, Setter
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getStars() {
        return stars;
    }

    public void setStars(Integer stars) {
        this.stars = stars;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    // toString(), hashCode(), equals() 메서드 추가 가능
}


