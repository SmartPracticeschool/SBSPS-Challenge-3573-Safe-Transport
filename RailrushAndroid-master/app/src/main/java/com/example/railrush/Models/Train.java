package com.example.railrush.Models;

public class Train {
    String start, dest, time, count, lastStation, trainNo;

    public Train(String start, String dest, String time, String count, String lastStation, String trainNo) {
        this.start = start;
        this.dest = dest;
        this.time = time;
        this.count = count;
        this.lastStation = lastStation;
        this.trainNo = trainNo;
    }

    public String getStart() {
        return start;
    }

    public String getDest() {
        return dest;
    }

    public String getTime() {
        return time;
    }

    public String getCount() {
        return count;
    }

    public String getLastStation() {
        return lastStation;
    }

    public String getTrainNo() {
        return trainNo;
    }
}
