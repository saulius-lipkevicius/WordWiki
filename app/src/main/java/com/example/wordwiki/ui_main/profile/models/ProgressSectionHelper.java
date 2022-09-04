package com.example.wordwiki.ui_main.profile.models;

public class ProgressSectionHelper {
    private String sectionName;
    private int learnedCount;
    private int allCount;

    public ProgressSectionHelper(String sectionName, int learnedCount, int allCount) {
        this.sectionName = sectionName;
        this.learnedCount = learnedCount;
        this.allCount = allCount;
    }

    public String getSectionName() {
        return sectionName;
    }

    public void setSectionName(String sectionName) {
        this.sectionName = sectionName;
    }

    public int getLearnedCount() {
        return learnedCount;
    }

    public void setLearnedCount(int learnedCount) {
        this.learnedCount = learnedCount;
    }

    public int getAllCount() {
        return allCount;
    }

    public void setAllCount(int allCount) {
        this.allCount = allCount;
    }
}
