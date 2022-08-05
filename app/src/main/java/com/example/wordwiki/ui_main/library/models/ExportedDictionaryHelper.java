package com.example.wordwiki.ui_main.library.models;

import java.util.ArrayList;

public class ExportedDictionaryHelper {
    private String nativeLanguage;
    private  String learningLanguage;
    private Integer downloadCount;
    private String sectionLevel;
    private Integer starsGiven;
    private Integer peopleVoted;
    private Integer wordsCount;
    private String sectionName;
    private ArrayList<String> words;
    private ArrayList<String> translations;

    public ExportedDictionaryHelper(){};
    public ExportedDictionaryHelper(String nativeLanguage, String learningLanguage, Integer downloadCount
            , String sectionLevel, Integer starsGiven, Integer peopleVoted, Integer wordsCount
            , String sectionName, ArrayList<String> words, ArrayList<String> translations) {
        this.nativeLanguage = nativeLanguage;
        this.learningLanguage = learningLanguage;
        this.downloadCount = downloadCount;
        this.sectionLevel = sectionLevel;
        this.starsGiven = starsGiven;
        this.peopleVoted = peopleVoted;
        this.wordsCount = wordsCount;
        this.sectionName = sectionName;
        this.words = words;
        this.translations = translations;
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

    public Integer getStarsGiven() {
        return starsGiven;
    }

    public void setStarsGiven(Integer starsGiven) {
        this.starsGiven = starsGiven;
    }

    public Integer getPeopleVoted() {
        return peopleVoted;
    }

    public void setPeopleVoted(Integer peopleVoted) {
        this.peopleVoted = peopleVoted;
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
}
