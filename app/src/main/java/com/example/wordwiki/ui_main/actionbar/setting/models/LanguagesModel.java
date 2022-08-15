package com.example.wordwiki.ui_main.actionbar.setting.models;

public class LanguagesModel {
    String languageName;
    int flag;

    public LanguagesModel(String languageName, int flag) {
        this.languageName = languageName;
        this.flag = flag;
    }

    public String getLanguageName() {
        return languageName;
    }

    public void setLanguageName(String languageName) {
        this.languageName = languageName;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }
}
