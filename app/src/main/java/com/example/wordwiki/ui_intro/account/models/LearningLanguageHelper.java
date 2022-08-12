package com.example.wordwiki.ui_intro.account.models;

public class LearningLanguageHelper {
    private String languageName;
    private int flag;
    private boolean checked;

    public LearningLanguageHelper(String languageName, int flag, boolean checked) {
        this.languageName = languageName;
        this.flag = flag;
        this.checked = checked;
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

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }
}
