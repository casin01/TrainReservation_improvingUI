package com.example.reservet;

import java.io.Serializable;

public class UserInfo implements Serializable {
    String userID;
    String userPassword;
    String userName;
    String userPhone;

    public UserInfo(String userID, String userPassword, String userName, String userPhone) {
        this.userID = userID;
        this.userPassword = userPassword;
        this.userName = userName;
        this.userPhone = userPhone;
    }
}