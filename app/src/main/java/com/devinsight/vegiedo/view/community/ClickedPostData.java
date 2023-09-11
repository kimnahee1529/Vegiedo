package com.devinsight.vegiedo.view.community;

import android.view.View;

import com.devinsight.vegiedo.data.response.PostListData;

public class ClickedPostData {

    PostListData postListData;
    int position;
    View view;

    public PostListData getPostListData() {
        return postListData;
    }

    public void setPostListData(PostListData postListData) {
        this.postListData = postListData;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public View getView() {
        return view;
    }

    public void setView(View view) {
        this.view = view;
    }

    public ClickedPostData(PostListData postListData, int position, View view) {
        this.postListData = postListData;
        this.position = position;
        this.view = view;
    }
}
