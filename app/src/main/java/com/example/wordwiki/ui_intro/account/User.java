package com.example.wordwiki.ui_intro.account;

import java.util.Map;

public class User {
    String username;
    String nationality;
    Map<String, String> learningLanguageMap;
    Map<String, String> masteredLanguageMap;

    public User(String username, String nationality, Map<String, String> learningLanguageMap, Map<String, String> masteredLanguageMap) {
        this.username = username;
        this.nationality = nationality;
        this.learningLanguageMap = learningLanguageMap;
        this.masteredLanguageMap = masteredLanguageMap;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public Map<String, String> getLearningLanguageMap() {
        return learningLanguageMap;
    }

    public void setLearningLanguageMap(Map<String, String> learningLanguageMap) {
        this.learningLanguageMap = learningLanguageMap;
    }

    public Map<String, String> getMasteredLanguageMap() {
        return masteredLanguageMap;
    }

    public void setMasteredLanguageMap(Map<String, String> masteredLanguageMap) {
        this.masteredLanguageMap = masteredLanguageMap;
    }
}
