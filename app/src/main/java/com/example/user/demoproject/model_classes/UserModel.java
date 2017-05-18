package com.example.user.demoproject.model_classes;

/**
 * Created by user on 15-05-2017.
 */

public class UserModel {
    String userName;
    String userEmail;
    int userId;

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public UserModel() {
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

}
