package com.example.wordwiki.ui_main.explore.models;

public class ExistingLanguageHelper {
    private String languageName;
    private int dictionaryCounter;

    public ExistingLanguageHelper(String languageName, int dictionaryCounter) {
        this.languageName = languageName;
        this.dictionaryCounter = dictionaryCounter;
    }

    public String getLanguageName() {
        return languageName;
    }

    public void setLanguageName(String languageName) {
        this.languageName = languageName;
    }

    public int getDictionaryCounter() {
        return dictionaryCounter;
    }

    public void setDictionaryCounter(int dictionaryCounter) {
        this.dictionaryCounter = dictionaryCounter;
    }
}
