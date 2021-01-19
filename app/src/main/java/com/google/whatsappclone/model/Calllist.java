package com.google.whatsappclone.model;

public class Calllist {
    private String userID;
    private String userName;
    private String date;
    private String urlProfile;
    private String Calltype;

    public Calllist() {
    }

    public Calllist(String userID, String userName, String date, String urlProfile, String calltype) {
        this.userID = userID;
        this.userName = userName;
        this.date = date;
        this.urlProfile = urlProfile;
        this.Calltype = calltype;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
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

    public String getUrlProfile() {
        return urlProfile;
    }

    public void setUrlProfile(String urlProfile) {
        this.urlProfile = urlProfile;
    }

    public String getCalltype() {
        return Calltype;
    }

    public void setCalltype(String calltype) {
        Calltype = calltype;
    }
}
