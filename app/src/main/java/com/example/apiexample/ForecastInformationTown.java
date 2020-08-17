package com.example.apiexample;

import androidx.annotation.NonNull;

public class ForecastInformationTown {

    public String precipitationFormDn;
    public String probabilityOfPrecipitation;
    public String precipitationDn;
    public String updatedDate;
    public String precipitationForm;
    public String humidity;
    public String precipitation;
    public String uid;
    public int locationId;

    public ForecastInformationTown(String PTY_DN, String POP, String R06, String updatedDate, String PTY, String REH, String RN1, String UID, int locationId) {
        this.precipitationFormDn = PTY_DN;
        this.probabilityOfPrecipitation = POP;
        this.precipitationDn = R06;
        this.updatedDate = updatedDate;
        this.precipitationForm = PTY;
        this.humidity = REH;
        this.precipitation = RN1;
        this.uid = UID;
        this.locationId = locationId;
    }

    public String getPrecipitationForm() { return precipitationForm; }
    public String getHumidity() { return humidity; }
    public String getPrecipitation() { return precipitation; }
    public String getPrecipitationFormDN() { return precipitationFormDn; }
    public String getProbabilityOfPrecipitation() { return probabilityOfPrecipitation; }
    public String getPrecipitationDn() { return precipitationDn; }
    public String getUpdatedDate() { return updatedDate; }
    public String getUid() { return uid; }
    public int getLocationId() { return locationId; }

    @Override
    public String toString() {
        return "PTY_DN : " + precipitationFormDn + ", PTY : " + precipitationForm + ", humidity : " + humidity + ", precipitation : " + precipitation + ", uid : " + uid + ", locationId : " + locationId
                + ", updatedDate : " + updatedDate;
    }

    public boolean isRainy() {
        return precipitationFormDn.charAt(0) - '0' > 0;
    }

    public String getFormattedUpdatedDate() {
        if (updatedDate.length() > 5) {
            return updatedDate.substring(0, updatedDate.length() - 5);
        }
        return updatedDate;
    }
}
