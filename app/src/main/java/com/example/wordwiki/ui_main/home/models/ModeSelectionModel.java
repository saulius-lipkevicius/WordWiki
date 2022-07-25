package com.example.wordwiki.ui_main.home.models;

public class ModeSelectionModel {
    private String modeName;
    private String modeSummaryName;
    private Boolean checkBox;


    public ModeSelectionModel(String modeName, String modeSummaryName, Boolean checkBox) {
        this.modeName = modeName;
        this.modeSummaryName = modeSummaryName;
        this.checkBox = checkBox;
    }


    public String getModeName() {
        return modeName;
    }

    public void setLanguageName(String languageName) {
        this.modeName = modeName;
    }

    public String getModeSummaryName() {
        return modeSummaryName;
    }

    public void setModeSummaryName(String sectionName) {
        this.modeSummaryName = modeSummaryName;
    }


    public Boolean getCheckBox() {
        return checkBox;
    }

    public void setCheckBox(Boolean checkBox) {
        this.checkBox = checkBox;
    }
}
