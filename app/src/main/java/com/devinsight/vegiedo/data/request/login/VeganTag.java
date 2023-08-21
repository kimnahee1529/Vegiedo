package com.devinsight.vegiedo.data.request.login;

public enum VeganTag {
    TAG_FRUITTARIAN(1, "#프루테리언"),
    TAG_VEGAN(2, "#비건"),
    TAG_LACTO(3,"#락토"),
    TAG_OVO(4,"#오보"),
    TAG_LACTO_OVO(5,"#락토 오보"),
    TAG_PESCA(6,"#페스코"),
    TAG_POLLO(7,"#폴로"),
    TAG_KETO(8,"#키토"),
    TAG_GLUTEN(9,"#글루텐프리");


    private int tagNum;
    private String tagContent;

    VeganTag(int tagNum, String tagContent){
        this.tagNum = tagNum;
        this.tagContent = tagContent;
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



}
