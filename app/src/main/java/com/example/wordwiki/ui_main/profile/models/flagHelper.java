package com.example.wordwiki.ui_main.profile.models;

public class flagHelper {
    private int flag;
    private int level;

    public flagHelper(int flag, int level) {
        this.flag = flag;
        this.level = level;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }
}
