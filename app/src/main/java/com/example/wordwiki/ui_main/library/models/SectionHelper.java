package com.example.wordwiki.ui_main.library.models;

import java.util.List;

public class SectionHelper {

    private String sectionName;
    private int sectionFlag;
    private List<SubsectionHelper> sectionItems;

    public SectionHelper(String sectionName, int sectionFlag, List<SubsectionHelper> sectionItems) {
        this.sectionName = sectionName;
        this.sectionFlag = sectionFlag;
        this.sectionItems = sectionItems;
    }

    public String getSectionName() {
        return sectionName;
    }

    public void setSectionName(String sectionName) {
        this.sectionName = sectionName;
    }

    public int getSectionFlag() {
        return sectionFlag;
    }

    public void setSectionFlag(int sectionFlag) {
        this.sectionFlag = sectionFlag;
    }

    public List<SubsectionHelper> getSectionItems() {
        return sectionItems;
    }

    public void setSectionItems(List<SubsectionHelper> sectionItems) {
        this.sectionItems = sectionItems;
    }

    @Override
    public String toString() {
        return "SectionHelper{" +
                "sectionName='" + sectionName + '\'' +
                ", sectionFlag=" + sectionFlag +
                ", sectionItems=" + sectionItems +
                '}';
    }
}
