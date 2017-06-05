package com.example.android.sensed.ui.activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.example.android.sensed.R;
import com.example.android.sensed.ReminderUtilities;
import com.example.android.sensed.services.SensedLocation;
import com.example.android.sensed.ui.adapter.MainPagerAdapter;
import com.example.android.sensed.ui.view.SensedTabsView;
import com.example.android.sensed.ui.view.SensedTitleView;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

public class MainActivity extends AppCompatActivity {

    private SensedLocation sensedLocation;
    private boolean isBound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //find view pager and Adapter for managing fragments
        ViewPager viewPager = (ViewPager) findViewById(R.id.am_view_pager);
        MainPagerAdapter adapter = new MainPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);

        // find navigation tabs and start listeners
        SensedTabsView sensedTabsView = (SensedTabsView) findViewById(R.id.am_sensed_tabs);
        sensedTabsView.setUpWithViewPager(viewPager, this);

        // find navigation tabs and start listeners
        SensedTitleView sensedTitleView = (SensedTitleView) findViewById(R.id.am_sensed_title);
        sensedTitleView.setUpWithViewPager(viewPager, this);

        // set initial fragment
        viewPager.setCurrentItem(1);

        // create intent and bind location service
        Intent i = new Intent(this, SensedLocation.class);
        bindService(i, locationServiceConnection, Context.BIND_AUTO_CREATE);

        ReminderUtilities.scheduleChargingReminder(this);


    }

    /**
     * getter for sensedLocation
     *
     * @return
     */
    public SensedLocation getSensedLocation() {
        return sensedLocation;
    }

    /**
     * Bind Location service
     */
    private ServiceConnection locationServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            SensedLocation.LocationBinder binder = (SensedLocation.LocationBinder) service;
            sensedLocation = binder.getService();
            isBound = true;

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            isBound = false;
        }
    };

}
