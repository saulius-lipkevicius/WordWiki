package com.example.wordwiki.ui_main.actionbar.setting;

public class SettingListModel {
    private String settingsName;
    private final int settingIcon;

    public SettingListModel(int settingIcon, String settingsName) {
        this.settingIcon = settingIcon;
        this.settingsName = settingsName;
    }

    public void setSettingsName(String settingsName) {
        this.settingsName = settingsName;
    }

    public int getSettingIcon() {
        return settingIcon;
    }

    public String getSettingsName() {
        return settingsName;
    }
}


