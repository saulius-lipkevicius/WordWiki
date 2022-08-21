package com.example.wordwiki.ui_main.profile.models;

public class progressHelper {

    String title;
    String wordCounter;
    String topicCounter;

    public progressHelper(String title, String wordCounter) {
        this.title = title;
        this.wordCounter = wordCounter;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getWordCounter() {
        return wordCounter;
    }

    public void setWordCounter(String wordCounter) {
        this.wordCounter = wordCounter;
    }
}
