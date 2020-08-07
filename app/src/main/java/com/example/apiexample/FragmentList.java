package com.example.apiexample;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

public class FragmentList extends Fragment {

    private View inflateView = null;
    private int regionStep = 0;
    private String regionStepOne = "";
    private String regionStepTwo = "";
    private String regionStepThree = "";
    private SelectLocationActivity parentActivity = null;

    public FragmentList(int regionStep, SelectLocationActivity parentActivity) {
        this(regionStep, "", parentActivity);
    }

    public FragmentList(int regionStep, String regionStepOne, SelectLocationActivity parentActivity) {
        this(regionStep, regionStepOne, "", parentActivity);
    }

    public FragmentList(int regionStep, String regionStepOne, String regionStepTwo, SelectLocationActivity parentActivity) {
        this(regionStep, regionStepOne, regionStepTwo, "", parentActivity);
    }

    public FragmentList(int regionStep, String regionStepOne, String regionStepTwo, String regionStepThree, SelectLocationActivity parentActivity) {
        this.regionStep = regionStep;
        this.regionStepOne = regionStepOne;
        this.regionStepTwo = regionStepTwo;
        this.regionStepThree = regionStepThree;
        this.parentActivity = parentActivity;
    }

    public String getRegionName(int currentRegionStep) {
        switch (currentRegionStep) {
            case 1 :
                return this.regionStepOne;
            case 2 :
                return this.regionStepTwo;
            case 3 :
                return this.regionStepThree;
            default :
                return "";
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        inflateView = inflater.inflate(R.layout.fragment_listview, container, false);

        ListView listView = inflateView.findViewById(R.id.list_view);

        ArrayList list = SQLiteDatabaseManager.getInstance().getRegionList(regionStep, regionStepOne, regionStepTwo);

        CustomListViewAdapter customAdapter = new CustomListViewAdapter(parentActivity, list);// ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, list);

        listView.setAdapter(customAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                FragmentList nextFragmentList = null;

                LocationInfo selectedLocationName = (LocationInfo) parent.getItemAtPosition(position);
//                RegionData selectedRegionName = (RegionData) parent.getItemAtPosition(position);

                System.out.println("My app test step : " + regionStep + ", one : " + regionStepOne + ", two : " + regionStepTwo + ", three : " + regionStepThree);
                switch (regionStep) {
                    case 1 :
                        nextFragmentList = new FragmentList(2, selectedLocationName.getRegionStep1(), parentActivity);
                        parentActivity.replaceFragment(nextFragmentList);
                        break;
                    case 2 :
                        if (selectedLocationName.getRegionStep2().isEmpty()) {
                            requestAddLocation(regionStepOne, "", "", true);
//                            requestCreate(regionStepOne, "", "");
                            break;
                        }
                        nextFragmentList = new FragmentList(3, regionStepOne, selectedLocationName.getRegionStep2(), parentActivity);
                        parentActivity.replaceFragment(nextFragmentList);
                        break;
                    case 3 :
                        if (selectedLocationName.getRegionStep3().isEmpty()) {
                            requestAddLocation(regionStepOne, regionStepTwo, "", true);
//                            requestCreate(regionStepOne, regionStepTwo, "");
                            break;
                        }
                        requestAddLocation(regionStepOne, regionStepTwo, selectedLocationName.getRegionStep3(), false);
//                        requestCreate(regionStepOne, regionStepTwo, selectedRegionName.getRegionThreeStep());
                        break;
                    default :
                        break;
                }
            }
        });

        return inflateView;
    }

//    private void requestCreate (String regionStepOne, String regionStepTwo, String regionStepThree) {
//        mainActivity.setSelectedRegionInformation(regionStepOne, regionStepTwo, regionStepThree);
//        mainActivity.postCreateRequest(regionStepOne, regionStepTwo, regionStepThree);
//    }
//
    private void requestAddLocation (String regionStepOne, String regionStepTwo, String regionStepThree, boolean isGlobalRegion) {
        System.out.println("My app test addlocation here function in");
        parentActivity.setSelectedLocationInfo(new LocationInfo(regionStepOne, regionStepTwo, regionStepThree, isGlobalRegion));
        parentActivity.postRequestAddLocation();
    }

    public int getRegionStep() {
        return regionStep;
    }


}
