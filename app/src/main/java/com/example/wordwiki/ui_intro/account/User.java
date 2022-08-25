package com.example.wordwiki.ui_intro.account;

import java.util.Map;

public class User {
    String username;
    String description;
    Map<String, Integer> learning;
    Map<String, Integer> proficiency;
    String profileImageURL;

    public User(String username, String nationality, Map<String, Integer> learning, Map<String, Integer> proficiency, String profileImageURL) {
        this.username = username;
        this.description = nationality;
        this.learning = learning;
        this.proficiency = proficiency;
        this.profileImageURL = profileImageURL;
    }
    public User() {

    }

    public String getProfileImageURL() {
        return profileImageURL;
    }

    public void setProfileImageURL(String profileImageURL) {
        this.profileImageURL = profileImageURL;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Map<String, Integer> getLearning() {
        return learning;
    }

    public void setLearning(Map<String, Integer> learning) {
        this.learning = learning;
    }

    public Map<String, Integer> getProficiency() {
        return proficiency;
    }

    public void setProficiency(Map<String, Integer> proficiency) {
        this.proficiency = proficiency;
    }
}
