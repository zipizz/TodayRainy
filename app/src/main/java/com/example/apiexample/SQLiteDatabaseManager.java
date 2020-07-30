package com.example.apiexample;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

public class SQLiteDatabaseManager {
    private Context mContext;
    private SQLiteDatabase mRegionDataDatabase = null;

    public SQLiteDatabaseManager(Context context) {
        mContext = context;
    }

    protected void createRegionDataDB(ArrayList<RegionData> regionDataCollections) {
        mRegionDataDatabase = mContext.openOrCreateDatabase(Constant.REGION_DATA_DATABASE_NAME, Context.MODE_PRIVATE, null);
        mRegionDataDatabase.execSQL("CREATE TABLE IF NOT EXISTS " + Constant.REGION_DATA_TABLE_NAME + " (" +
                "regionOneStep varchar(20), " +
                "regionTwoStep varchar(20), " +
                "regionThreeStep varchar(20), " +
                "nx varchar(5), " +
                "ny varchar(5)" +
                ");"
        );
        mRegionDataDatabase.execSQL("DELETE FROM " + Constant.REGION_DATA_TABLE_NAME);

        mRegionDataDatabase.beginTransaction();
        for (RegionData regionData : regionDataCollections) {
            mRegionDataDatabase.execSQL("INSERT INTO " + Constant.REGION_DATA_TABLE_NAME + " (" +
                    "regionOneStep, " +
                    "regionTwoStep, " +
                    "regionThreeStep, " +
                    "nx, " +
                    "ny" +
                    ") VALUES (" +
                    "\"" + regionData.getRegionOneStep() + "\", " +
                    "\"" + regionData.getRegionTwoStep() + "\", " +
                    "\"" + regionData.getRegionThreeStep() + "\", " +
                    "\"" + regionData.getNx() + "\", " +
                    "\"" + regionData.getNy() + "\"" +
                    ");"
            );
        }
        mRegionDataDatabase.setTransactionSuccessful();
        mRegionDataDatabase.endTransaction();
        mRegionDataDatabase.close();
    }

    public void showRegionDataDB() {
        mRegionDataDatabase = mContext.openOrCreateDatabase(Constant.REGION_DATA_DATABASE_NAME, Context.MODE_PRIVATE, null);
        Cursor cursor = mRegionDataDatabase.rawQuery("SELECT * FROM " + Constant.REGION_DATA_TABLE_NAME, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                String a = cursor.getString(cursor.getColumnIndex("regionOneStep"));
                String b = cursor.getString(cursor.getColumnIndex("regionTwoStep"));
                String c = cursor.getString(cursor.getColumnIndex("regionThreeStep"));
                String d = cursor.getString(cursor.getColumnIndex("nx"));
                String e = cursor.getString(cursor.getColumnIndex("ny"));
                System.out.println(a + ", " + b + ", " + c + ", " + d + ", " + e);
            } while (cursor.moveToNext());
        }

        cursor.close();
        mRegionDataDatabase.close();
    }

    public void close() {
        if (mRegionDataDatabase != null) {
            mRegionDataDatabase.close();
        }
    }


}
