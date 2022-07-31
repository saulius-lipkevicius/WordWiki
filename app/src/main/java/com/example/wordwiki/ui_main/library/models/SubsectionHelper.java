package com.example.wordwiki.ui_main.library.models;

public class SubsectionHelper {

    String sectionName;
    String subsectionName;
    String description;
    String creator;
    String subsectionLevel;
    Boolean ratedUp;
    Boolean ratedDown;
    Boolean expanded;

    public SubsectionHelper(String sectionName, String subsectionName, String description, String creator, String subsectionLevel, Boolean ratedUp, Boolean ratedDown, Boolean expanded) {
        this.sectionName = sectionName;
        this.subsectionName = subsectionName;
        this.description = description;
        this.creator = creator;
        this.subsectionLevel = subsectionLevel;
        this.ratedUp = ratedUp;
        this.ratedDown = ratedDown;
        this.expanded = expanded;
    }

    public String getSectionName() {
        return sectionName;
    }

    public void setSectionName(String sectionName) {
        this.sectionName = sectionName;
    }

    public String getSubsectionName() {
        return subsectionName;
    }

    public void setSubsectionName(String subsectionName) {
        this.subsectionName = subsectionName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getRatedUp() {
        return ratedUp;
    }

    public void setRatedUp(Boolean ratedUp) {
        this.ratedUp = ratedUp;
    }

    public Boolean getRatedDown() {
        return ratedDown;
    }

    public void setRatedDown(Boolean ratedDown) {
        this.ratedDown = ratedDown;
    }

    public Boolean getExpanded() {
        return expanded;
    }

    public void setExpanded(Boolean expanded) {
        this.expanded = expanded;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getSubsectionLevel() {
        return subsectionLevel;
    }

    public void setSubsectionLevel(String subsectionLevel) {
        this.subsectionLevel = subsectionLevel;
    }
}
