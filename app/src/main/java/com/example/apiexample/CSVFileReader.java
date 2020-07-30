package com.example.apiexample;

import android.content.res.Resources;
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

    public ArrayList<RegionData> getRegionDataCollectionsFromCSVFile() {
        String[] CSVColumnData = null;
        ArrayList<RegionData> regionDataCollections = new ArrayList<RegionData>();

        // [0] 구분, [1] 행정구역코드, [2] 1단계, [3] 2단계, [4] 3단계, [5] 격자 X, [6] 격자 Y,
        // [7] 경도(시), [8] 경도(분), [9] 경도(초), [10] 위도(시), [11] 위도(분), [12] 위도(초), [13] 경도(초/100), [14] 위도(초/100), [15] 위치업데이트
        try {
            String[] CSVFileHeaderLineString = mReader.readNext();

            while ((CSVColumnData = mReader.readNext()) != null) {
                String regionOneStep = CSVColumnData[2];
                String regionTwoStep = CSVColumnData[3];
                String regionThreeStep = CSVColumnData[4];
                String nx = CSVColumnData[5];
                String ny = CSVColumnData[6];
                regionDataCollections.add(new RegionData(regionOneStep, regionTwoStep, regionThreeStep, nx, ny));
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