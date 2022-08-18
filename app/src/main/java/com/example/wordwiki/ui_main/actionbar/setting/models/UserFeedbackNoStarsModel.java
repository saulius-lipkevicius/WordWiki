package com.example.wordwiki.ui_main.actionbar.setting.models;

public class UserFeedbackNoStarsModel {
    String username;
    String input;
    String email;
    String date;

    public UserFeedbackNoStarsModel(String username, String input, String email, String date) {
        this.username = username;
        this.input = input;
        this.email = email;
        this.date = date;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getInput() {
        return input;
    }

    public void setInput(String input) {
        this.input = input;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
