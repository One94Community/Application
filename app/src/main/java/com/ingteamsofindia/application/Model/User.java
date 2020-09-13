package com.ingteamsofindia.application.Model;

public class User {
    private String currentUser_Bio,currentUser_Country,currentUser_CurrentDate,currentUser_EmailID,currentUser_FullName,currentUser_ID,currentUser_ProfileImageUrl,currentUser_Username;

    public User() {
    }

    public User(String currentUser_Bio, String currentUser_Country, String currentUser_CurrentDate, String currentUser_EmailID, String currentUser_FullName, String currentUser_ID, String currentUser_ProfileImageUrl, String currentUser_Username) {
        this.currentUser_Bio = currentUser_Bio;
        this.currentUser_Country = currentUser_Country;
        this.currentUser_CurrentDate = currentUser_CurrentDate;
        this.currentUser_EmailID = currentUser_EmailID;
        this.currentUser_FullName = currentUser_FullName;
        this.currentUser_ID = currentUser_ID;
        this.currentUser_ProfileImageUrl = currentUser_ProfileImageUrl;
        this.currentUser_Username = currentUser_Username;
    }

    public String getCurrentUser_Bio() {
        return currentUser_Bio;
    }

    public void setCurrentUser_Bio(String currentUser_Bio) {
        this.currentUser_Bio = currentUser_Bio;
    }

    public String getCurrentUser_Country() {
        return currentUser_Country;
    }

    public void setCurrentUser_Country(String currentUser_Country) {
        this.currentUser_Country = currentUser_Country;
    }

    public String getCurrentUser_CurrentDate() {
        return currentUser_CurrentDate;
    }

    public void setCurrentUser_CurrentDate(String currentUser_CurrentDate) {
        this.currentUser_CurrentDate = currentUser_CurrentDate;
    }

    public String getCurrentUser_EmailID() {
        return currentUser_EmailID;
    }

    public void setCurrentUser_EmailID(String currentUser_EmailID) {
        this.currentUser_EmailID = currentUser_EmailID;
    }

    public String getCurrentUser_FullName() {
        return currentUser_FullName;
    }

    public void setCurrentUser_FullName(String currentUser_FullName) {
        this.currentUser_FullName = currentUser_FullName;
    }

    public String getCurrentUser_ID() {
        return currentUser_ID;
    }

    public void setCurrentUser_ID(String currentUser_ID) {
        this.currentUser_ID = currentUser_ID;
    }

    public String getCurrentUser_ProfileImageUrl() {
        return currentUser_ProfileImageUrl;
    }

    public void setCurrentUser_ProfileImageUrl(String currentUser_ProfileImageUrl) {
        this.currentUser_ProfileImageUrl = currentUser_ProfileImageUrl;
    }

    public String getCurrentUser_Username() {
        return currentUser_Username;
    }

    public void setCurrentUser_Username(String currentUser_Username) {
        this.currentUser_Username = currentUser_Username;
    }
}
