package com.example.reservet;

import java.io.Serializable;

public class StationInfo implements Serializable {
    String st_station;
    String st_stationCode;

    public StationInfo(String st_station, String st_stationCode) {
        this.st_station = st_station;
        this.st_stationCode = st_stationCode;
    }
}
