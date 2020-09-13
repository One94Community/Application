package com.ingteamsofindia.application.Model;

public class Post {
    private String
            currentUser_CurrentDate,
            currentUser_FullName,
            currentUser_ID_Post,
            currentUser_ID_User,
            currentUser_PostDescription,
            currentUser_PostImage,
            currentUser_Username;

    public Post() {
    }

    public Post(String currentUser_CurrentDate, String currentUser_FullName, String currentUser_ID_Post, String currentUser_ID_User, String currentUser_PostDescription, String currentUser_PostImage, String currentUser_Username) {
        this.currentUser_CurrentDate = currentUser_CurrentDate;
        this.currentUser_FullName = currentUser_FullName;
        this.currentUser_ID_Post = currentUser_ID_Post;
        this.currentUser_ID_User = currentUser_ID_User;
        this.currentUser_PostDescription = currentUser_PostDescription;
        this.currentUser_PostImage = currentUser_PostImage;
        this.currentUser_Username = currentUser_Username;
    }

    public String getCurrentUser_CurrentDate() {
        return currentUser_CurrentDate;
    }

    public void setCurrentUser_CurrentDate(String currentUser_CurrentDate) {
        this.currentUser_CurrentDate = currentUser_CurrentDate;
    }

    public String getCurrentUser_FullName() {
        return currentUser_FullName;
    }

    public void setCurrentUser_FullName(String currentUser_FullName) {
        this.currentUser_FullName = currentUser_FullName;
    }

    public String getCurrentUser_ID_Post() {
        return currentUser_ID_Post;
    }

    public void setCurrentUser_ID_Post(String currentUser_ID_Post) {
        this.currentUser_ID_Post = currentUser_ID_Post;
    }

    public String getCurrentUser_ID_User() {
        return currentUser_ID_User;
    }

    public void setCurrentUser_ID_User(String currentUser_ID_User) {
        this.currentUser_ID_User = currentUser_ID_User;
    }

    public String getCurrentUser_PostDescription() {
        return currentUser_PostDescription;
    }

    public void setCurrentUser_PostDescription(String currentUser_PostDescription) {
        this.currentUser_PostDescription = currentUser_PostDescription;
    }

    public String getCurrentUser_PostImage() {
        return currentUser_PostImage;
    }

    public void setCurrentUser_PostImage(String currentUser_PostImage) {
        this.currentUser_PostImage = currentUser_PostImage;
    }

    public String getCurrentUser_Username() {
        return currentUser_Username;
    }

    public void setCurrentUser_Username(String currentUser_Username) {
        this.currentUser_Username = currentUser_Username;
    }
}
