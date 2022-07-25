package com.example.wordwiki.ui_main.home.models;

public class LanguageSelectionModel {
    private final int imageResource;
    private String languageName;
    private Boolean checkBox;

    public LanguageSelectionModel(String languageName, Boolean checkBox, int imageResource) {
        this.languageName = languageName;
        this.checkBox = checkBox;
        this.imageResource = imageResource;
    }

    public String getLanguageName() {
        return languageName;
    }

    public void setLanguageName(String languageName) {
        this.languageName = languageName;
    }

    public Boolean getCheckBox() {
        return checkBox;
    }

    public void setCheckBox(Boolean checkBox) {
        this.checkBox = checkBox;
    }

    public int getImageResource() {
        return imageResource;
    }
}

