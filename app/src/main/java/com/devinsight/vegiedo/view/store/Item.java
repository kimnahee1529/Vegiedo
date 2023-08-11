package com.devinsight.vegiedo.view.store;

public class Item {
    public enum ItemType {
        TYPE_ONE,
        TYPE_TWO,
        STORE_DETAIL_PAGE
    }

    private ItemType itemType;
    private String title;
    private String description;
    private String content;

    private int imageResource1;
    private int imageResource2;
    private int imageResource3;
    private int imageResource4;
    private int imageResource5;
    private int imageResource6;
    private int imageResource7;
    private int imageResource8;
    private int imageResource9;
    private int imageResource10;


    public Item(ItemType itemType, String title, int imageResource6, int imageResource7, int imageResource8, int imageResource9, int imageResource10, String content, int imageResource1, int imageResource2, int imageResource3, int imageResource4, int imageResource5) {
        this.itemType = itemType;
        this.title = title;
        this.content = content;
        this.imageResource1 = imageResource1;
        this.imageResource2 = imageResource2;
        this.imageResource3 = imageResource3;
        this.imageResource4 = imageResource4;
        this.imageResource5 = imageResource5;
        this.imageResource6 = imageResource6;
        this.imageResource7 = imageResource7;
        this.imageResource8 = imageResource8;
        this.imageResource9 = imageResource9;
        this.imageResource10 = imageResource10;
    }

    // Getters
    public ItemType getItemType() {
        return itemType;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getContent() {
        return content;
    }

    public int getImageResource1() {
        return imageResource1;
    }

    public int getImageResource2() {
        return imageResource2;
    }

    public int getImageResource3() {
        return imageResource3;
    }

    public int getImageResource4() {
        return imageResource4;
    }

    public int getImageResource5() {
        return imageResource5;
    }

    public int getImageResource6() {
        return imageResource6;
    }

    public int getImageResource7() {
        return imageResource7;
    }

    public int getImageResource8() {
        return imageResource8;
    }

    public int getImageResource9() {
        return imageResource9;
    }

    public int getImageResource10() {
        return imageResource10;
    }

}