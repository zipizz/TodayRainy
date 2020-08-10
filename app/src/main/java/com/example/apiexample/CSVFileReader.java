package com.example.apiexample;

import android.content.res.Resources;
import android.location.Geocoder;
import android.util.Log;

import com.opencsv.CSVReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class CSVFileReader {

    private InputStream mInputStream;
    private InputStreamReader mInputStreamReader;
    private CSVReader mReader;

    public CSVFileReader(Resources resources) {
        mInputStream = resources.openRawResource(R.raw.openapi_region);
        mInputStreamReader = new InputStreamReader(mInputStream);
        mReader = new CSVReader(mInputStreamReader);
    }

    public ArrayList<LocationInfo> getRegionDataCollectionsFromCSVFile() {
        String[] CSVColumnData = null;
        ArrayList<LocationInfo> regionDataCollections = new ArrayList<LocationInfo>();

        // [0] 구분, [1] 행정구역코드, [2] 1단계, [3] 2단계, [4] 3단계, [5] 격자 X, [6] 격자 Y,
        // [7] 경도(시), [8] 경도(분), [9] 경도(초), [10] 위도(시), [11] 위도(분), [12] 위도(초), [13] 경도(초/100), [14] 위도(초/100), [15] 위치업데이트
        try {
            String[] CSVFileHeaderLineString = mReader.readNext();

            while ((CSVColumnData = mReader.readNext()) != null) {
                String regionOneStep = CSVColumnData[2];
                String regionTwoStep = CSVColumnData[3];
                String regionThreeStep = CSVColumnData[4];
//                int longitudeHour = Integer.parseInt(CSVColumnData[7]);
//                int longitudeMin = Integer.parseInt(CSVColumnData[8]);
//                int latitudeHour = Integer.parseInt(CSVColumnData[10]);
//                int latitudeMin = Integer.parseInt(CSVColumnData[11]);

//                regionDataCollections.add(new LocationInfo(regionOneStep, regionTwoStep, regionThreeStep, longitudeHour, longitudeMin, latitudeHour, latitudeMin));
                regionDataCollections.add(new LocationInfo(regionOneStep, regionTwoStep, regionThreeStep));
            }
        } catch (IOException ioe) {
            Log.d("", ioe.getMessage());
        }
        return regionDataCollections;
    }

    public ArrayList<LocationInfo> getRegionRealDataCollectionsFromCSVFile() {
        String[] CSVColumnData = null;
        ArrayList<LocationInfo> regionDataCollections = new ArrayList<LocationInfo>();

        // [0] 구분, [1] 행정구역코드, [2] 1단계, [3] 2단계, [4] 3단계, [5] 격자 X, [6] 격자 Y,
        // [7] 경도(시), [8] 경도(분), [9] 경도(초), [10] 위도(시), [11] 위도(분), [12] 위도(초), [13] 경도(초/100), [14] 위도(초/100), [15] 위치업데이트
        try {
            String[] CSVFileHeaderLineString = mReader.readNext();

            while ((CSVColumnData = mReader.readNext()) != null) {
                String regionOneStep = CSVColumnData[2];
                String regionTwoStep = CSVColumnData[3];
                String regionThreeStep = CSVColumnData[4];
                double longitudeReal = Double.parseDouble(CSVColumnData[13]);
                double latitudeReal = Double.parseDouble(CSVColumnData[14]);
                regionDataCollections.add(new LocationInfo(regionOneStep, regionTwoStep, regionThreeStep, longitudeReal, latitudeReal));
            }
        } catch (IOException ioe) {
            Log.d("", ioe.getMessage());
        }
        return regionDataCollections;
    }

    public void close() {
        try {
            mInputStream.close();
            mInputStreamReader.close();
            mReader.close();
        } catch (IOException ioe) {
            Log.d("", ioe.getMessage());
        }
    }
}