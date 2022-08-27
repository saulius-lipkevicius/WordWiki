package com.example.wordwiki.ui_main.explore.models;

import java.util.ArrayList;

public class ImportModel {
    private String nativeLanguage;
    private String learningLanguage;
    private Integer downloadCount;
    private String sectionLevel;
    private Integer likesSum;
    private Integer wordsCount;
    private String sectionName;
    private ArrayList<String> words;
    private ArrayList<String> translations;
    private Boolean checkBox;

    public ImportModel(String nativeLanguage, String learningLanguage, Integer downloadCount
            , String sectionLevel, Integer likesSum, Integer wordsCount
            , String sectionName, ArrayList<String> words, ArrayList<String> translations, Boolean checkBox) {
        this.nativeLanguage = nativeLanguage;
        this.learningLanguage = learningLanguage;
        this.downloadCount = downloadCount;
        this.sectionLevel = sectionLevel;
        this.likesSum = likesSum;
        this.wordsCount = wordsCount;
        this.sectionName = sectionName;
        this.words = words;
        this.translations = translations;
        this.checkBox = checkBox;
    }

    public ArrayList<String> getWords() {
        return words;
    }

    public void setWords(ArrayList<String> words) {
        this.words = words;
    }

    public ArrayList<String> getTranslations() {
        return translations;
    }

    public void setTranslations(ArrayList<String> translations) {
        this.translations = translations;
    }

    public Integer getDownloadCount() {
        return downloadCount;
    }

    public void setDownloadCount(Integer downloadCount) {
        this.downloadCount = downloadCount;
    }

    public String getSectionLevel() {
        return sectionLevel;
    }

    public void setSectionLevel(String sectionLevel) {
        this.sectionLevel = sectionLevel;
    }

    public Integer getWordsCount() {
        return wordsCount;
    }

    public void setWordsCount(Integer wordsCount) {
        this.wordsCount = wordsCount;
    }

    public String getSectionName() {
        return sectionName;
    }

    public void setSectionName(String sectionName) {
        this.sectionName = sectionName;
    }

    public Integer getLikesSum() {
        return likesSum;
    }

    public void setLikesSum(Integer likesSum) {
        this.likesSum = likesSum;
    }

    public String getNativeLanguage() {
        return nativeLanguage;
    }

    public void setNativeLanguage(String nativeLanguage) {
        this.nativeLanguage = nativeLanguage;
    }

    public String getLearningLanguage() {
        return learningLanguage;
    }

    public void setLearningLanguage(String learningLanguage) {
        this.learningLanguage = learningLanguage;
    }

    public Boolean getCheckBox() {
        return checkBox;
    }

    public void setCheckBox(Boolean checkBox) {
        this.checkBox = checkBox;
    }

    public String getLanguageSection(){
        return learningLanguage + sectionName;
    }
}
