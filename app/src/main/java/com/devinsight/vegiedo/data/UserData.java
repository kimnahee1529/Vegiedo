package com.devinsight.vegiedo.data;

import java.util.ArrayList;
import java.util.List;

public class UserData {
    String userName;
    String userProfile;
    String kakaoToken;
    String googleToken;
    String post;
    String comment;
    String review;
    List<Tag> tag;
    ArrayList<StoreData> visitedStore;
    ArrayList<StoreData> likedStore;

}
