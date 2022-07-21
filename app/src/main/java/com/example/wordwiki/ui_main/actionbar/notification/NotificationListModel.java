package com.example.wordwiki.ui_main.actionbar.notification;

public class NotificationListModel {
    private String notificationDescription;

    public NotificationListModel(String notificationDescription) {
        this.notificationDescription = notificationDescription;
    }

    public String getFriendName() {
        return notificationDescription;
    }
}


