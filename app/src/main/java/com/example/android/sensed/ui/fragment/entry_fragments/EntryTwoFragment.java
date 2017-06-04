package com.example.android.sensed.ui.fragment.entry_fragments;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.android.sensed.R;
import com.example.android.sensed.data.SensedContract;
import com.example.android.sensed.ui.activity.CreateEntyActivity;
import com.example.android.sensed.ui.activity.MainActivity;
import com.example.android.sensed.entry.ManualEntry;
import com.example.android.sensed.ui.fragment.BaseFragment;

import static android.content.Context.LOCATION_SERVICE;

/**
 * @author Laurie Dugdale
 */

public class EntryTwoFragment extends BaseFragment {

    private ImageView mPreviousImage;
    private View mRoot;
    private ImageView mDoneButton;
    private LocationManager locationManager;
    private LocationListener listener;
    private String t;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        locationManager = (LocationManager) getActivity().getSystemService(LOCATION_SERVICE);


        listener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                System.out.println("here");
                System.out.println("\n " + location.getLongitude() + " " + location.getLatitude());
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

                Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(i);
            }
        };

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mPreviousImage = (ImageView) view.findViewById(R.id.feo_previous_image);
        acceptButton(view);
        // go to page 0 on click of end image
        mPreviousImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ((CreateEntyActivity) getActivity()).switchToFragmentZero();
            }
        });
    }

    public static EntryTwoFragment create() {

        return new EntryTwoFragment();
    }

    public void acceptButton(final View view) {
        mDoneButton = (ImageView) view.findViewById(R.id.iv_accept_button);
        mDoneButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

            addToContentProvider();

            // create intent to move back to MainActivity
            Class destinationActivity = MainActivity.class;
            Intent startMainActivityIntent = new Intent(v.getContext(), destinationActivity);
            v.getContext().startActivity(startMainActivityIntent);

//                ((CreateEntyActivity)getActivity()).finish();
            }
        });
    }


    public void addToContentProvider(){

        ManualEntry me = ((CreateEntyActivity) getActivity()).getmManualEntry();
        Location location = ((CreateEntyActivity) getActivity()).getSensedLocation().getLocation();

        double latitude = 0.0;
        double longitude = 0.0;

        if (location != null){
            latitude = location.getLatitude();
            longitude = location.getLongitude();
        }

        // Insert new task data via a ContentResolver
        // Create new empty ContentValues object
        ContentValues contentValues = new ContentValues();
        // Put the task description and selected mPriority into the ContentValues
        contentValues.put(SensedContract.SensedEntry.COLUMN_ENTRY_DATE_TIME, me.getDate());
        contentValues.put(SensedContract.SensedEntry.COLUMN_ENTRY_HAPPINESS, me.getHappiness());
        contentValues.put(SensedContract.SensedEntry.COLUMN_ENTRY_LONGITUDE, longitude);
        contentValues.put(SensedContract.SensedEntry.COLUMN_ENTRY_LATITUDE, latitude);


        // Insert the content values via a ContentResolver
        getActivity().getContentResolver().insert(SensedContract.SensedEntry.CONTENT_URI, contentValues);
    }

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_entry_two;
    }

    @Override
    public void inOnCreateView(View root, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

    }



}
