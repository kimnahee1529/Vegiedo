package com.devinsight.vegiedo.utill;

import com.devinsight.vegiedo.R;

public enum VeganTag {
    FRUITTARIAN(R.id.tag_fruittarian, "#프루테리언",0),
    VEGAN(R.id.tag_vegan, "#비건",0),
    LACTO(R.id.tag_lacto,"#락토",0),
    OVO(R.id.tag_ovo,"#오보",0),
    LACTO_OVO(R.id.tag_lacto_ovo,"#락토 오보",0),
    PESCO(R.id.tag_pesca,"#페스코",0),
    POLLO(R.id.tag_pollo,"#폴로",0),
    KETO(R.id.tag_keto,"#키토",0),
    GLUTEN_FREE(R.id.tag_gluten,"#글루텐프리",0);


    private int tagNum;
    private String tagContent;
    private int tagId;

    VeganTag(int tagId, String tagContent, int tagNum){
        this.tagId = tagId;
        this.tagContent = tagContent;
        this.tagNum = tagNum;
    }

    public int getTagNum() {
        return tagNum;
    }

    public void setTagNum(int tagNum) {
        this.tagNum = tagNum;
    }

    public String getTagContent() {
        return tagContent;
    }

    public void setTagContent(String tagContent) {
        this.tagContent = tagContent;
    }

    public int getTagId() {
        return tagId;
    }

    public void setTagId(int tagId) {
        this.tagId = tagId;
    }



}
