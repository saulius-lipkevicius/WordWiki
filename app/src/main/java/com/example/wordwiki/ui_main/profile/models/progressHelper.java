package com.example.wordwiki.ui_main.profile.models;

public class progressHelper {

    int image;
    String title;
    String wordCounter;
    String topicCounter;

    public progressHelper(int image, String title, String wordCounter) {
        this.image = image;
        this.title = title;
        this.wordCounter = wordCounter;
    }

    public int getImage() {
        return image;
    }

    public String getTitle() {
        return title;
    }

    public void setImage(int image) {
        this.image = image;
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
