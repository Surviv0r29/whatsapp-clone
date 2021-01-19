package com.google.whatsappclone.model;

public class ChatList {
    private String userId;
    private String userName;
    private String date;
    private String description;
    private String urlProfile;

    public ChatList() {
    }

    public ChatList(String userId, String userName, String date, String description, String urlProfile) {
        this.userId = userId;
        this.userName = userName;
        this.date = date;
        this.description = description;
        this.urlProfile = urlProfile;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrlProfile() {
        return urlProfile;
    }

    public void setUrlProfile(String urlProfile) {
        this.urlProfile = urlProfile;
    }
}
