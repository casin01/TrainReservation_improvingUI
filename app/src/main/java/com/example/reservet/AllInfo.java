package com.example.reservet;

import java.io.Serializable;

public class AllInfo implements Serializable {
    String userID;
    String userPassword;
    String userName;
    String userPhone;
    String depStn;
    String depStnID;
    String arrStn;
    String arrStnID;
    String trainName;
    String trainNo;
    String depTime;
    String arrTime;
    int peopleNum;
    int totalcharge;
    String imp_uid;
    String merchant_uid;
    String bookdate;

    public AllInfo(String userID, String userPassword, String userName, String userPhone, String depStn, String depStnID,
                   String arrStn, String arrStnID, String trainName, String trainNo, String depTime, String arrTime,
                   int peopleNum, int totalcharge, String imp_uid, String merchant_uid, String bookdate) {
        this.userID = userID;
        this.userPassword = userPassword;
        this.userName = userName;
        this.userPhone = userPhone;
        this.depStn = depStn;
        this.depStnID = depStnID;
        this.arrStn= arrStn;
        this.arrStnID = arrStnID;
        this.trainName = trainName;
        this.trainNo = trainNo;
        this.depTime = depTime;
        this.arrTime = arrTime;
        this.peopleNum = peopleNum;
        this.totalcharge = totalcharge;
        this.imp_uid = imp_uid;
        this.merchant_uid = merchant_uid;
        this.bookdate = bookdate;
    }

}
