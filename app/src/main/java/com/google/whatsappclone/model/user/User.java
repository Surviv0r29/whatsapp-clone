package com.google.whatsappclone.model.user;

public class User {
    private String userId;
    private String userName;
    private String Bio;
    private String userPhone;
    private String imageProfile;
    private String status;

    public User() {
    }

    public User(String userId, String userName, String userPhone, String imageProfile, String status,String Bio) {
        this.userId = userId;
        this.userName = userName;
        this.userPhone = userPhone;
        this.imageProfile = imageProfile;
        this.status = status;
        this.Bio = Bio;
    }


    public String getBio() {
        return Bio;
    }

    public void setBio(String bio) {
        Bio = bio;
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

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getImageProfile() {
        return imageProfile;
    }

    public void setImageProfile(String imageProfile) {
        this.imageProfile = imageProfile;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
