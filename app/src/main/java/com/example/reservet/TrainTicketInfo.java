package com.example.reservet;

import java.io.Serializable;

public class TrainTicketInfo implements Serializable {
    String trainGradeName; // 차량 종류
    String depPlandTime;   // 출발 시간(날짜) 20220305054000
    String arrPlandTime;   // 도착 시간(날짜) 20220305064700
    String depPlaceName;  // 출발지
    String arrPlaceName; // 도착지
    String adultCharge; // 비용
    String trainno; //기차 번호

    public TrainTicketInfo(String trainGradeName, String depPlandTime, String arrPlandTime, String depPlaceName, String arrPlaceName, String adultCharge, String trainNo) {
        this.trainGradeName = trainGradeName;
        this.depPlandTime = depPlandTime;
        this.arrPlandTime = arrPlandTime;
        this.depPlaceName = depPlaceName;
        this.arrPlaceName = arrPlaceName;
        this.adultCharge = adultCharge;
        this.trainno= trainNo;
    }
}
