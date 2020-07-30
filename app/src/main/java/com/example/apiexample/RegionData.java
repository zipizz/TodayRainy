package com.example.apiexample;

public class RegionData {

    private String regionOneStep;
    private String regionTwoStep;
    private String regionThreeStep;
    private String nx;
    private String ny;

    public RegionData(String regionOneStep, String regionTwoStep, String regionThreeStep, String nx, String ny) {
        this.regionOneStep = regionOneStep;
        this.regionTwoStep = regionTwoStep;
        this.regionThreeStep = regionThreeStep;
        this.nx = nx;
        this.ny = ny;
    }

    public String getRegionOneStep() { return this.regionOneStep; }

    public String getRegionTwoStep() {
        return this.regionTwoStep;
    }

    public String getRegionThreeStep() { return this.regionThreeStep; }

    public String getNx() {
        return this.nx;
    }

    public String getNy() {
        return this.ny;
    }
}
