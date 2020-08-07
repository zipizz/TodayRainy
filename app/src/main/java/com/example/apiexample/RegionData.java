//package com.example.apiexample;
//
//public class RegionData {
//
////    private String regionOneStep;
////    private String regionTwoStep;
////    private String regionThreeStep;
////    private String nx;
////    private String ny;
////    private boolean isGlobalRegion;
////
////    public RegionData() {
////        this("");
////    }
////
////    public RegionData(String regionOneStep) {
////        this(regionOneStep, "");
////    }
////
////    public RegionData(String regionOneStep, String regionTwoStep) {
////        this(regionOneStep, regionTwoStep, "");
////    }
////
////    public RegionData(String regionOneStep, String regionTwoStep, String regionThreeStep) {
////        this(regionOneStep, regionTwoStep, regionThreeStep, "", "");
////    }
////
////    public RegionData(String regionOneStep, String regionTwoStep, String regionThreeStep, boolean isGlobalRegion) {
////        this(regionOneStep, regionTwoStep, regionThreeStep, "", "", isGlobalRegion);
////    }
////
////    public RegionData(String regionOneStep, String regionTwoStep, String regionThreeStep, String nx, String ny) {
////        this(regionOneStep, regionTwoStep, regionThreeStep, nx, ny, false);
////    }
////
////    public RegionData(String regionOneStep, String regionTwoStep, String regionThreeStep, String nx, String ny, boolean isGlobalRegion) {
////        this.regionOneStep = regionOneStep;
////        this.regionTwoStep = regionTwoStep;
////        this.regionThreeStep = regionThreeStep;
////        this.nx = nx;
////        this.ny = ny;
////        this.isGlobalRegion = isGlobalRegion;
////    }
////
////    public String getRegionOneStep() { return this.regionOneStep; }
////
////    public String getRegionTwoStep() {
////        return this.regionTwoStep;
////    }
////
////    public String getRegionThreeStep() { return this.regionThreeStep; }
////
////    public String getNx() {
////        return this.nx;
////    }
////
////    public String getNy() {
////        return this.ny;
////    }
////
////    public void setIsGlobalRegion(boolean isGlobalRegion) { this.isGlobalRegion = isGlobalRegion; }
////
////    public boolean isGlobalRegion() { return this.isGlobalRegion; }
////
////    @Override
////    public String toString() {
////        String regionFullName = regionOneStep + " " + regionTwoStep + " " + regionThreeStep;
////
////        if (isGlobalRegion) {
////            int regionFullNameLastIndex = regionFullName.length() - 1;
////            while (regionFullName.charAt(regionFullNameLastIndex) == ' ') {
////                regionFullNameLastIndex--;
////            }
////            regionFullName = regionFullName.substring(0, regionFullNameLastIndex + 1) + " 전체";
////        }
////
////        return regionFullName;
////    }
//}
