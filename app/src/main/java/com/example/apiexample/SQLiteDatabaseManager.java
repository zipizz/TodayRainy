package com.example.apiexample;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class SQLiteDatabaseManager {

    private static SQLiteDatabaseManager instance = null;

    private static Context mContext = null;
    private static SQLiteDatabase mRegionDataDatabase = null;
    private AtomicInteger mOpenCounter = new AtomicInteger();

    public SQLiteDatabaseManager(Context context) {
        mContext = context;
    }

    public static synchronized void initializeInstance(Context context) {
        if (instance == null) {
            instance = new SQLiteDatabaseManager(context);
        }
    }

    public static synchronized SQLiteDatabaseManager getInstance() {
        if (instance == null) {
            throw new IllegalStateException(SQLiteDatabaseManager.class.getSimpleName() + " is not initialized, call initializeInstance(..) method first.");
        }
        return instance;
    }

    public synchronized SQLiteDatabase openDatabase() {
        if(mOpenCounter.incrementAndGet() == 1) {
            if (mRegionDataDatabase != null && mRegionDataDatabase.isOpen()) {
                return mRegionDataDatabase;
            }
            mRegionDataDatabase = mContext.openOrCreateDatabase(Constant.REGION_DATA_DATABASE_NAME, Context.MODE_PRIVATE, null);
        }
        return mRegionDataDatabase;
    }

    public synchronized void closeDatabase() {
        if (mOpenCounter.decrementAndGet() == 0) {
            if(mRegionDataDatabase.isOpen()) {
                mRegionDataDatabase.close();
            }
        }
    }

    protected void createLocationCSVTable(ArrayList<LocationInfo> regionDataCollections) {

        mRegionDataDatabase = SQLiteDatabaseManager.getInstance().openDatabase();

        mRegionDataDatabase.execSQL("DROP TABLE IF EXISTS " + Constant.REGION_DATA_FROM_CSV_TABLE_NAME);

        mRegionDataDatabase.execSQL("CREATE TABLE IF NOT EXISTS " + Constant.REGION_DATA_FROM_CSV_TABLE_NAME + " (" +
                "regionStep1 varchar(20), " +
                "regionStep2 varchar(20), " +
                "regionStep3 varchar(20), " +
                "longitudeHour INTEGER, " +
                "longitudeMin INTEGER, " +
                "latitudeHour INTEGER, " +
                "latitudeMin INTEGER, " +
                "isGlobalRegion BOOLEAN, " +
                "locationId INTEGER" +
                ")"
        );

        mRegionDataDatabase.execSQL("DELETE FROM " + Constant.REGION_DATA_FROM_CSV_TABLE_NAME);

        mRegionDataDatabase.beginTransaction();
        for (LocationInfo regionData : regionDataCollections) {
            mRegionDataDatabase.execSQL("INSERT INTO " + Constant.REGION_DATA_FROM_CSV_TABLE_NAME + " (" +
                    "regionStep1, " +
                    "regionStep2, " +
                    "regionStep3, " +
                    "longitudeHour, " +
                    "longitudeMin, " +
                    "latitudeHour, " +
                    "latitudeMin, " +
                    "isGlobalRegion, " +
                    "locationId" +
                    ") VALUES (" +
                    "\"" + regionData.getRegionStep1() + "\", " +
                    "\"" + regionData.getRegionStep2() + "\", " +
                    "\"" + regionData.getRegionStep3() + "\", " +
                    regionData.getLongitudeHour() + ", " +
                    regionData.getLongitudeMin() + ", " +
                    regionData.getLatitudeHour() + ", " +
                    regionData.getLatitudeMin() + ", " +
                    (regionData.isGlobalRegion() ? 1 : 0) + ", " +
                    regionData.getLocationId() +
                    ")"
            );
        }
        mRegionDataDatabase.setTransactionSuccessful();
        mRegionDataDatabase.endTransaction();
        SQLiteDatabaseManager.getInstance().closeDatabase();
    }

    protected void createMyRegionIncludingCurrentLocationTable() {

        mRegionDataDatabase = SQLiteDatabaseManager.getInstance().openDatabase();
        mRegionDataDatabase.execSQL("DROP TABLE IF EXISTS " + Constant.REGION_DATA_MY_REGION_INCLUDING_CURRENT_LOCATION_TABLE_NAME);

        mRegionDataDatabase.execSQL("CREATE TABLE IF NOT EXISTS " + Constant.REGION_DATA_MY_REGION_INCLUDING_CURRENT_LOCATION_TABLE_NAME + " (" +
                "regionStep1 varchar(20), " +
                "regionStep2 varchar(20), " +
                "regionStep3 varchar(20), " +
                "longitudeHour INTEGER, " +
                "longitudeMin INTEGER, " +
                "latitudeHour INTEGER, " +
                "latitudeMin INTEGER, " +
                "isGlobalRegion BOOLEAN, " +
                "locationId INTEGER" +
                ")"
        );
        SQLiteDatabaseManager.getInstance().closeDatabase();
    }

    protected void createRemainLocationIdTable() {
        mRegionDataDatabase = SQLiteDatabaseManager.getInstance().openDatabase();
        mRegionDataDatabase.execSQL("DROP TABLE IF EXISTS " + Constant.REGION_DATA_REMAIN_LOCATION_ID_TABLE_NAME);

        mRegionDataDatabase.execSQL("CREATE TABLE IF NOT EXISTS " + Constant.REGION_DATA_REMAIN_LOCATION_ID_TABLE_NAME + " (" +
                "locationId INTEGER PRIMARY KEY" +
                ")"
        );
        for (int i = 1; i <= Constant.MAX_MY_ONLY_ADDED_LOCATION_COUNT; i++) {
            addLocationIdIntoRemainLocationIdTable(i);
        }
        SQLiteDatabaseManager.getInstance().closeDatabase();
    }

    protected void createForecastInformationTable() {
        mRegionDataDatabase = SQLiteDatabaseManager.getInstance().openDatabase();
        mRegionDataDatabase.execSQL("CREATE TABLE IF NOT EXISTS " + Constant.FORECAST_INFORMATION_TABLE_NAME + " (" +
                "precipitationFormDn varchar(20), " +
                "probabilityOfPrecipitation varchar(20), " +
                "precipitationDn varchar(20), " +
                "updatedDate varchar(20), " +
                "precipitationForm varchar(20), " +
                "humidity varchar(20), " +
                "precipitation varchar(20), " +
                "uid varchar(20), " +
                "locationId INTEGER, " +
                "FOREIGN KEY ( locationId ) " +
                "REFERENCES " + Constant.REGION_DATA_REMAIN_LOCATION_ID_TABLE_NAME +
                " ( locationId ) " +
                ")"
        );

        addEmptyForecastInformationList();
        SQLiteDatabaseManager.getInstance().closeDatabase();
    }

    private void addEmptyForecastInformationList() {
        mRegionDataDatabase = SQLiteDatabaseManager.getInstance().openDatabase();
        for (int i = 0; i < Constant.MAX_LOCATION_INCLUDING_CURRENT_LOCATION_COUNT; i++) {
            int locationId = i;
            if (i == 0) {
                locationId = Constant.CURRENT_LOCATION_ID;
            }
            mRegionDataDatabase.execSQL("INSERT INTO " + Constant.FORECAST_INFORMATION_TABLE_NAME + " (" +
                    "precipitationFormDn, " +
                    "probabilityOfPrecipitation, " +
                    "precipitationDn, " +
                    "updatedDate, " +
                    "precipitationForm, " +
                    "humidity, " +
                    "precipitation, " +
                    "uid, " +
                    "locationId" +
                    ") VALUES (" +
                    "\"" + "0" + "\", " +
                    "\"" + "" + "\", " +
                    "\"" + "" + "\", " +
                    "\"" + "" + "\", " +
                    "\"" + "0" + "\", " +
                    "\"" + "" + "\", " +
                    "\"" + "" + "\", " +
                    "\"" + PreferenceManager.getString(mContext, Constant.USER_ID) + "\", " +
                    locationId +
                    ")"
            );
        }
        SQLiteDatabaseManager.getInstance().closeDatabase();
    }

    protected void addForecastInformationTable(ForecastInformationTown forecastInformationTown) {
        mRegionDataDatabase = SQLiteDatabaseManager.getInstance().openDatabase();
        mRegionDataDatabase.execSQL("INSERT INTO " + Constant.FORECAST_INFORMATION_TABLE_NAME + " (" +
                "precipitationFormDn, " +
                "probabilityOfPrecipitation, " +
                "precipitationDn, " +
                "updatedDate, " +
                "precipitationForm, " +
                "humidity, " +
                "precipitation, " +
                "uid, " +
                "locationId" +
                ") VALUES (" +
                "\"" + forecastInformationTown.getPrecipitationFormDN() + "\", " +
                "\"" + forecastInformationTown.getProbabilityOfPrecipitation() + "\", " +
                "\"" + forecastInformationTown.getPrecipitationDn() + "\", " +
                "\"" + forecastInformationTown.getUpdatedDate() + "\", " +
                "\"" + forecastInformationTown.getPrecipitationForm() + "\", " +
                "\"" + forecastInformationTown.getHumidity() + "\", " +
                "\"" + forecastInformationTown.getPrecipitation() + "\", " +
                "\"" + forecastInformationTown.getUid() + "\", " +
                forecastInformationTown.getLocationId() +
                ")"
        );
        SQLiteDatabaseManager.getInstance().closeDatabase();
    }

    protected void updateForecastInformationListTable(ArrayList<ForecastInformationTown> forecastInformationTownsList) {
        mRegionDataDatabase = SQLiteDatabaseManager.getInstance().openDatabase();
        for (ForecastInformationTown forecastInformationTown : forecastInformationTownsList) {
            mRegionDataDatabase.execSQL("UPDATE " + Constant.FORECAST_INFORMATION_TABLE_NAME +
                    "SET " +
                    "precipitationFormDn = " + "\"" + forecastInformationTown.getPrecipitationFormDN() + "\", " +
                    "probabilityOfPrecipitation = " + "\"" + forecastInformationTown.getProbabilityOfPrecipitation() + "\", " +
                    "precipitationDn = " + "\"" + forecastInformationTown.getPrecipitationDn() + "\", " +
                    "updatedDate = " + "\"" + forecastInformationTown.getUpdatedDate() + "\", " +
                    "precipitationForm = " + "\"" + forecastInformationTown.getPrecipitationForm() + "\", " +
                    "humidity = " + "\"" + forecastInformationTown.getHumidity() + "\", " +
                    "precipitation = " + "\"" + forecastInformationTown.getPrecipitation() + "\", " +
                    "uid = " + "\"" + forecastInformationTown.getUid() + "\"" +
                    " WHERE " +
                    "locationId = " + forecastInformationTown.getLocationId() +
                    ")"
            );
        }
        SQLiteDatabaseManager.getInstance().closeDatabase();
    }

    protected void updateForecastInformationTable(ForecastInformationTown forecastInformationTown) {
        mRegionDataDatabase = SQLiteDatabaseManager.getInstance().openDatabase();
        mRegionDataDatabase.execSQL("UPDATE " + Constant.FORECAST_INFORMATION_TABLE_NAME +
                " SET " +
                "precipitationFormDn = " + "\"" + forecastInformationTown.getPrecipitationFormDN() + "\", " +
                "probabilityOfPrecipitation = " + "\"" + forecastInformationTown.getProbabilityOfPrecipitation() + "\", " +
                "precipitationDn = " + "\"" + forecastInformationTown.getPrecipitationDn() + "\", " +
                "updatedDate = " + "\"" + forecastInformationTown.getUpdatedDate() + "\", " +
                "precipitationForm = " + "\"" + forecastInformationTown.getPrecipitationForm() + "\", " +
                "humidity = " + "\"" + forecastInformationTown.getHumidity() + "\", " +
                "precipitation = " + "\"" + forecastInformationTown.getPrecipitation() + "\", " +
                "uid = " + "\"" + forecastInformationTown.getUid() + "\"" +
                " WHERE " +
                "locationId = " + forecastInformationTown.getLocationId()
        );
        SQLiteDatabaseManager.getInstance().closeDatabase();
    }

    protected ArrayList<ForecastInformationTown> getForcastInformationList() {
        mRegionDataDatabase = SQLiteDatabaseManager.getInstance().openDatabase();
        Cursor cursor = mRegionDataDatabase.rawQuery("SELECT * FROM " + Constant.FORECAST_INFORMATION_TABLE_NAME, null);

        ArrayList forecastInformationList = new ArrayList<>();

        if (cursor != null && cursor.moveToFirst()) {
            do {
                forecastInformationList.add(
                        new ForecastInformationTown(
                                cursor.getString(cursor.getColumnIndex("precipitationFormDn")),
                                cursor.getString(cursor.getColumnIndex("probabilityOfPrecipitation")),
                                cursor.getString(cursor.getColumnIndex("precipitationDn")),
                                cursor.getString(cursor.getColumnIndex("updatedDate")),
                                cursor.getString(cursor.getColumnIndex("precipitationForm")),
                                cursor.getString(cursor.getColumnIndex("humidity")),
                                cursor.getString(cursor.getColumnIndex("precipitation")),
                                cursor.getString(cursor.getColumnIndex("uid")),
                                cursor.getInt(cursor.getColumnIndex("locationId"))
                        )
                );
            }  while (cursor.moveToNext());
        }

        cursor.close();
        SQLiteDatabaseManager.getInstance().closeDatabase();
        return forecastInformationList;
    }

    protected ForecastInformationTown getForecastInformation(int locationId) {
        mRegionDataDatabase = SQLiteDatabaseManager.getInstance().openDatabase();
        Cursor cursor = mRegionDataDatabase.rawQuery("SELECT * FROM " + Constant.FORECAST_INFORMATION_TABLE_NAME
                + " WHERE locationId = " + locationId, null);

        ForecastInformationTown forecastInformationTown = null;
        if (cursor != null && cursor.moveToFirst()) {
            forecastInformationTown = new ForecastInformationTown(
                    cursor.getString(cursor.getColumnIndex("precipitationFormDn")),
                    cursor.getString(cursor.getColumnIndex("probabilityOfPrecipitation")),
                    cursor.getString(cursor.getColumnIndex("precipitationDn")),
                    cursor.getString(cursor.getColumnIndex("updatedDate")),
                    cursor.getString(cursor.getColumnIndex("precipitationForm")),
                    cursor.getString(cursor.getColumnIndex("humidity")),
                    cursor.getString(cursor.getColumnIndex("precipitation")),
                    cursor.getString(cursor.getColumnIndex("uid")),
                    cursor.getInt(cursor.getColumnIndex("locationId"))
            );
        }

        cursor.close();
        SQLiteDatabaseManager.getInstance().closeDatabase();
        return forecastInformationTown;
    }

    protected void addLocationIdIntoRemainLocationIdTable(int locationId) {
        mRegionDataDatabase = SQLiteDatabaseManager.getInstance().openDatabase();
        mRegionDataDatabase.execSQL("INSERT INTO " + Constant.REGION_DATA_REMAIN_LOCATION_ID_TABLE_NAME + " (" +
                "locationId" +
                ") VALUES (" +
                locationId +
                ")"
        );
        SQLiteDatabaseManager.getInstance().closeDatabase();
    }

    public boolean hasLocationInMyRegionDataIncludingCurrentLocationTable(LocationInfo locationInfo) {
        mRegionDataDatabase = SQLiteDatabaseManager.getInstance().openDatabase();
        Cursor cursor = mRegionDataDatabase.rawQuery("SELECT COUNT(*) FROM " + Constant.REGION_DATA_MY_REGION_INCLUDING_CURRENT_LOCATION_TABLE_NAME
                + " WHERE regionStep1 = " + "\"" + locationInfo.getRegionStep1() + "\""
                        + " AND regionStep2 = " + "\"" + locationInfo.getRegionStep2() + "\""
                        + " AND regionStep3 = " + "\"" + locationInfo.getRegionStep3() + "\"",
        null);

        boolean hasLocation = false;
        if (cursor != null && cursor.moveToFirst()) {
             hasLocation = cursor.getInt(0) > 0;
        } else {
            System.out.println("my app test unknown error at haslocation function");
        }
        cursor.close();
        SQLiteDatabaseManager.getInstance().closeDatabase();
        return hasLocation;
    }

    public void deleteLocationIdFromRemainLocationIdTable(int locationId) {
        mRegionDataDatabase = SQLiteDatabaseManager.getInstance().openDatabase();
        mRegionDataDatabase.execSQL("DELETE FROM " + Constant.REGION_DATA_REMAIN_LOCATION_ID_TABLE_NAME +
                " WHERE " +
                "locationId = " +
                locationId
        );
        SQLiteDatabaseManager.getInstance().closeDatabase();
    }

    public int getRemainLocationIdMin() {
        mRegionDataDatabase = SQLiteDatabaseManager.getInstance().openDatabase();
        Cursor cursor = mRegionDataDatabase.rawQuery("SELECT * FROM " + Constant.REGION_DATA_REMAIN_LOCATION_ID_TABLE_NAME + " ORDER BY locationId", null);

        int locationIdMinValue = Constant.LOCATION_ID_NOT_EXIST;

        if (cursor != null && cursor.moveToFirst()) {
            locationIdMinValue = cursor.getInt(cursor.getColumnIndex("locationId"));
        }
        cursor.close();
        SQLiteDatabaseManager.getInstance().closeDatabase();
        return locationIdMinValue;
    }

    public ArrayList<Integer> getRemainLocationIdList() {
        mRegionDataDatabase = SQLiteDatabaseManager.getInstance().openDatabase();
        Cursor cursor = mRegionDataDatabase.rawQuery("SELECT * FROM " + Constant.REGION_DATA_REMAIN_LOCATION_ID_TABLE_NAME + " ORDER BY locationId", null);

        ArrayList locationIdList = new ArrayList<>();

        if (cursor != null && cursor.moveToFirst()) {
            do {
                locationIdList.add(cursor.getInt(cursor.getColumnIndex("locationId")));
            }  while (cursor.moveToNext());
        }

        cursor.close();
        SQLiteDatabaseManager.getInstance().closeDatabase();
        return locationIdList;
    }

    public void showLocationDataListFromCSVTable() {
        mRegionDataDatabase = SQLiteDatabaseManager.getInstance().openDatabase();
        Cursor cursor = mRegionDataDatabase.rawQuery("SELECT * FROM " + Constant.REGION_DATA_FROM_CSV_TABLE_NAME, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                String a = cursor.getString(cursor.getColumnIndex("regionStep1"));
                String b = cursor.getString(cursor.getColumnIndex("regionStep2"));
                String c = cursor.getString(cursor.getColumnIndex("regionStep3"));
                String d = String.valueOf(cursor.getInt(cursor.getColumnIndex("longitudeHour")));
                String e = String.valueOf(cursor.getInt(cursor.getColumnIndex("longitudeMin")));
                String f = String.valueOf(cursor.getInt(cursor.getColumnIndex("latitudeHour")));
                String g = String.valueOf(cursor.getInt(cursor.getColumnIndex("latitudeMin")));
                String h = String.valueOf(cursor.getInt(cursor.getColumnIndex("isGlobalRegion")));
                String i = String.valueOf(cursor.getInt(cursor.getColumnIndex("locationId")) == 1);
                System.out.println(a + ", " + b + ", " + c + ", " + d + ", " + e + ", " + f + ", " + g + ", " + h + ", " + i);
            } while (cursor.moveToNext());
        }

        cursor.close();
        SQLiteDatabaseManager.getInstance().closeDatabase();
    }

    public ArrayList<LocationInfo> getMyRegionDataListFromCSVTable() {
        mRegionDataDatabase = SQLiteDatabaseManager.getInstance().openDatabase();
        Cursor cursor = mRegionDataDatabase.rawQuery("SELECT * FROM " + Constant.REGION_DATA_FROM_CSV_TABLE_NAME, null);
        ArrayList<LocationInfo> myRegionDataList = new ArrayList<LocationInfo>();

        if (cursor != null && cursor.moveToFirst()) {
            do {
                myRegionDataList.add(new LocationInfo(cursor.getString(cursor.getColumnIndex("regionStep1")),
                        cursor.getString(cursor.getColumnIndex("regionStep2")),
                        cursor.getString(cursor.getColumnIndex("regionStep3")),
                        cursor.getInt(cursor.getColumnIndex("isGlobalRegion")) == 1,
                        cursor.getInt(cursor.getColumnIndex("locationId"))
                        )
                );
            } while (cursor.moveToNext());
        }
        cursor.close();
        SQLiteDatabaseManager.getInstance().closeDatabase();
        return myRegionDataList;
    }

    public LocationInfo getMyRegionDataFromRegionIncludingCurrentLocationTable(int locationId) {
        mRegionDataDatabase = SQLiteDatabaseManager.getInstance().openDatabase();
        Cursor cursor = mRegionDataDatabase.rawQuery("SELECT * FROM " + Constant.REGION_DATA_MY_REGION_INCLUDING_CURRENT_LOCATION_TABLE_NAME +
                " WHERE locationId = " + locationId, null);

        LocationInfo locationInfo = null;

        if (cursor != null && cursor.moveToFirst()) {
            locationInfo = new LocationInfo(cursor.getString(cursor.getColumnIndex("regionStep1")),
                                cursor.getString(cursor.getColumnIndex("regionStep2")),
                                cursor.getString(cursor.getColumnIndex("regionStep3")),
                                cursor.getInt(cursor.getColumnIndex("isGlobalRegion")) == 1,
                                locationId);
        } else {
            System.out.println("my app test unknown error at getmyregiondata function");
        }
        cursor.close();
        SQLiteDatabaseManager.getInstance().closeDatabase();
        return locationInfo;
    }

    public ArrayList<LocationInfo> getMyRegionIncludingCurrentLocationList(boolean isOrderByLocationId) {
        mRegionDataDatabase = SQLiteDatabaseManager.getInstance().openDatabase();
        Cursor cursor = mRegionDataDatabase.rawQuery("SELECT * FROM " + Constant.REGION_DATA_MY_REGION_INCLUDING_CURRENT_LOCATION_TABLE_NAME +
                (isOrderByLocationId ? " ORDER BY locationId" : ""), null);
        ArrayList<LocationInfo> myRegionDataList = new ArrayList<LocationInfo>();

        if (cursor != null && cursor.moveToFirst()) {
            do {
                myRegionDataList.add(new LocationInfo(cursor.getString(cursor.getColumnIndex("regionStep1")),
                                cursor.getString(cursor.getColumnIndex("regionStep2")),
                                cursor.getString(cursor.getColumnIndex("regionStep3")),
                                cursor.getInt(cursor.getColumnIndex("isGlobalRegion")) == 1,
                                cursor.getInt(cursor.getColumnIndex("locationId"))
                        )
                );
            } while (cursor.moveToNext());
        }
        cursor.close();
        SQLiteDatabaseManager.getInstance().closeDatabase();
        return myRegionDataList;
    }

    public ArrayList<LocationInfo> getRegionListFromCSVTable(int regionStep, String regionStepOneName, String regionStepTwoName) {
        mRegionDataDatabase = SQLiteDatabaseManager.getInstance().openDatabase();

        String SQL = "";

        switch (regionStep) {
            case 1 :
                SQL = "SELECT DISTINCT(regionStep1) FROM " + Constant.REGION_DATA_FROM_CSV_TABLE_NAME;
                break;
            case 2 :
                SQL = "SELECT DISTINCT(regionStep2) FROM " + Constant.REGION_DATA_FROM_CSV_TABLE_NAME + " WHERE regionStep1 = '" + regionStepOneName + "'";
                break;
            case 3 :
                SQL = "SELECT DISTINCT(regionStep3) FROM " + Constant.REGION_DATA_FROM_CSV_TABLE_NAME + " WHERE regionStep1 = '" + regionStepOneName + "' AND regionStep2 = '" + regionStepTwoName + "'";
                break;
            default :
                break;
        }

        Cursor cursor = mRegionDataDatabase.rawQuery(SQL, null);
        ArrayList<LocationInfo> regionStringList = new ArrayList<LocationInfo>();

        if (cursor != null && cursor.moveToFirst()) {
            do {
                LocationInfo regionData = null;
                String currentStepRegionName = cursor.getString(0);
                switch (regionStep) {
                    case 1 :
                        regionData = new LocationInfo(currentStepRegionName);
                        break;
                    case 2 :
                        regionData = new LocationInfo(regionStepOneName, currentStepRegionName);
                        break;
                    case 3 :
                        regionData = new LocationInfo(regionStepOneName, regionStepTwoName, currentStepRegionName);
                        break;
                    default :
                        regionData = new LocationInfo();
                        break;
                }
                if (currentStepRegionName.isEmpty()) {
                    regionData.setIsGlobalRegion(true);
                }
                regionStringList.add(regionData);
            } while (cursor.moveToNext());
        }
        cursor.close();
        SQLiteDatabaseManager.getInstance().closeDatabase();
        return regionStringList;
    }

    public void close() {
        SQLiteDatabaseManager.getInstance().closeDatabase();
//        if (mRegionDataDatabase != null) {
//            mRegionDataDatabase.close();
//        }
    }

    private SQLiteDatabase getRegionDataDatabase() {
        mRegionDataDatabase = SQLiteDatabaseManager.getInstance().openDatabase();
        if (mRegionDataDatabase == null) {
            if (mContext != null) {
                mRegionDataDatabase = mContext.openOrCreateDatabase(Constant.REGION_DATA_DATABASE_NAME, Context.MODE_PRIVATE, null);
            } else {
                System.out.println("Use Constructor for mContext");
            }
        }
        SQLiteDatabaseManager.getInstance().closeDatabase();
        return mRegionDataDatabase;
    }

    public void addMyLocationDataIntoLocationIncludingCurrentLocationTable(LocationInfo locationInfo) {
        mRegionDataDatabase = SQLiteDatabaseManager.getInstance().openDatabase();
        mRegionDataDatabase.execSQL("INSERT INTO " + Constant.REGION_DATA_MY_REGION_INCLUDING_CURRENT_LOCATION_TABLE_NAME + " (" +
                "regionStep1, " +
                "regionStep2, " +
                "regionStep3, " +
                "longitudeHour, " +
                "longitudeMin, " +
                "latitudeHour, " +
                "latitudeMin, " +
                "isGlobalRegion, " +
                "locationId" +
                ") VALUES (" +
                "\"" + locationInfo.getRegionStep1() + "\", " +
                "\"" + locationInfo.getRegionStep2() + "\", " +
                "\"" + locationInfo.getRegionStep3() + "\", " +
                locationInfo.getLongitudeHour() + ", " +
                locationInfo.getLongitudeMin() + ", " +
                locationInfo.getLatitudeHour() + ", " +
                locationInfo.getLatitudeMin() + ", " +
                (locationInfo.isGlobalRegion() ? 1 : 0) + ", " +
                locationInfo.getLocationId() +
                ")"
        );
        SQLiteDatabaseManager.getInstance().closeDatabase();
    }

    public void deleteMyRegionDataFromRegionIncludingCurrentLocationTable(int locationId) {
        mRegionDataDatabase = SQLiteDatabaseManager.getInstance().openDatabase();
        mRegionDataDatabase.execSQL("DELETE FROM " + Constant.REGION_DATA_MY_REGION_INCLUDING_CURRENT_LOCATION_TABLE_NAME +
                " WHERE locationId = " + locationId
        );
        SQLiteDatabaseManager.getInstance().closeDatabase();
    }
}
