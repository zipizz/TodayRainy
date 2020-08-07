package com.example.apiexample;

import androidx.annotation.NonNull;

public class ForecastInformation {

    public String precipitationForm;
    public String humidity;
    public String precipitation;
    public String uid;
    public int locationId;

    public ForecastInformation(String PTY, String REH, String RN1, String UID, int locationId) {
        this.precipitationForm = PTY;
        this.humidity = REH;
        this.precipitation = RN1;
        this.uid = UID;
        this.locationId = locationId;
    }

    public String getPrecipitationForm() { return precipitationForm; }
    public int getLocationId() { return locationId; }

    @Override
    public String toString() {
        return "PTY : " + precipitationForm + ", humidity : " + humidity + ", precipitation : " + precipitation + ", uid : " + uid;
    }
}
