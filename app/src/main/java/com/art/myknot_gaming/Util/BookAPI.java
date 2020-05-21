package com.art.myknot_gaming.Util;

import android.app.Application;

public class BookAPI extends Application {
    private String username;
    private String userId;
    private String userEmail;
    private static BookAPI instance;

    public static BookAPI getInstance(){
        if(instance == null)
            instance = new BookAPI();
        return instance;
    }

    public BookAPI(){}

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }


}