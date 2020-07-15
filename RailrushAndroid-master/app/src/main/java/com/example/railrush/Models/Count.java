package com.example.railrush.Models;

public class Count {
    double crowdCount;
    String lastStation;

    public String getLastStation() {
        return lastStation;
    }

    public void setLastStation(String lastStation) {
        this.lastStation = lastStation;
    }

    public double getCrowdCount() {
        return crowdCount;
    }

    public void setCrowdCount(double crowdCount) {
        this.crowdCount = crowdCount;
    }
}
