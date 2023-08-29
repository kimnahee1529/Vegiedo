package com.devinsight.vegiedo.view.search;

import android.widget.SeekBar;

public class seekbarData {

    private int progress;
    private int xPos;

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public int getxPos() {
        return xPos;
    }

    public void setxPos(int xPos) {
        this.xPos = xPos;
    }

    public seekbarData(int progress, int xPos) {
        this.progress = progress;
        this.xPos = xPos;
    }
}
