package com.example.apiexample;

import java.io.Serializable;

public class LocationInfoForServer implements Serializable {

    private static final long serialVersionUID = -7788619177798333712L;

    private int longitudeHour;
    private int longitudeMin;
    private int latitudeHour;
    private int latitudeMin;

    private String regionStep1;
    private String regionStep2;
    private String regionStep3;

    public int getLongitudeHour() {
        return longitudeHour;
    }

    public void setLongitudeHour(int longitudeHour) {
        this.longitudeHour = longitudeHour;
    }

    public int getLongitudeMin() {
        return longitudeMin;
    }

    public void setLongitudeMin(int longitudeMin) {
        this.longitudeMin = longitudeMin;
    }

    public int getLatitudeHour() {
        return latitudeHour;
    }

    public void setLatitudeHour(int latitudeHour) {
        this.latitudeHour = latitudeHour;
    }

    public int getLatitudeMin() {
        return latitudeMin;
    }

    public void setLatitudeMin(int latitudeMin) {
        this.latitudeMin = latitudeMin;
    }

    public String getRegionStep1() {
        return regionStep1;
    }

    public void setRegionStep1(String regionStep1) {
        this.regionStep1 = regionStep1;
    }

    public String getRegionStep2() {
        return regionStep2;
    }

    public void setRegionStep2(String regionStep2) {
        this.regionStep2 = regionStep2;
    }

    public String getRegionStep3() {
        return regionStep3;
    }

    public void setRegionStep3(String regionStep3) {
        this.regionStep3 = regionStep3;
    }
}
