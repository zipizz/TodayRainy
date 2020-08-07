package com.example.apiexample;
import java.io.Serializable;

public class LocationInfo implements Serializable{

    private static final long serialVersionUID = -7788619177798333712L;

    private int longitudeHour;
    private int longitudeMin;
    private int latitudeHour;
    private int latitudeMin;
    private boolean isGlobalRegion;
    private int locationId;

    private String regionStep1;
    private String regionStep2;
    private String regionStep3;

    public LocationInfo() {
        this("");
    }

    public LocationInfo(String regionStep1) {
        this(regionStep1, "");
    }

    public LocationInfo(String regionStep1, String regionStep2) {
        this(regionStep1, regionStep2, "");
    }

    public LocationInfo(String regionStep1, String regionStep2, String regionStep3) {
        this(regionStep1, regionStep2, regionStep3, false);
    }

    public LocationInfo (String regionStep1, String regionStep2, String regionStep3, boolean isGlobalRegion) {
        this(regionStep1, regionStep2, regionStep3, isGlobalRegion, -1);
    }

    public LocationInfo (String regionStep1, String regionStep2, String regionStep3, boolean isGlobalRegion, int locationId) {
        this(regionStep1, regionStep2, regionStep3, 0, 0, 0, 0, isGlobalRegion, locationId);
    }

    public LocationInfo (String regionStep1, String regionStep2, String regionStep3, int longitudeHour, int longitudeMin, int latitudeHour, int latitudeMin, boolean isGlobalRegion, int locationId) {
        this.regionStep1 = regionStep1;
        this.regionStep2 = regionStep2;
        this.regionStep3 = regionStep3;
        this.longitudeHour = longitudeHour;
        this.longitudeMin = longitudeMin;
        this.latitudeHour = latitudeHour;
        this.latitudeMin = latitudeMin;
        this.isGlobalRegion = isGlobalRegion;
        this.locationId = locationId;
    }

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

    public boolean isGlobalRegion() { return isGlobalRegion; }

    public void setIsGlobalRegion(boolean isGlobalRegion) { this.isGlobalRegion = isGlobalRegion; }

    public int getLocationId() { return locationId; }

    public void setLocationId(int locationId) { this.locationId = locationId; }

    @Override
    public String toString() {
        String regionFullName = regionStep1 + " " + regionStep2 + " " + regionStep3;

        if (isGlobalRegion) {
            int regionFullNameLastIndex = regionFullName.length() - 1;
            while (regionFullName.charAt(regionFullNameLastIndex) == ' ') {
                regionFullNameLastIndex--;
            }
            regionFullName = regionFullName.substring(0, regionFullNameLastIndex + 1) + " 전체";
        }

        return regionFullName;
    }

}
