package com.example.wordwiki.ui_main.home.models;

public class SectionSelectionModel {
    private final int imageResource;
    private String sectionName;
    private String languageName;
    private Boolean checkBox;

    public SectionSelectionModel(String sectionName, String languageName, Boolean checkBox, int imageResource) {
        this.sectionName = sectionName;
        this.languageName = languageName;
        this.checkBox = checkBox;
        this.imageResource = imageResource;
    }

    public String getSectionName() {
        return sectionName;
    }

    public void setSectionName(String sectionName) {
        this.sectionName = sectionName;
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

    public String getLanguageSection() { return languageName + sectionName; }

    public int getImageResource() {
        return imageResource;
    }
}
