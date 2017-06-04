package com.example.android.sensed.ui.activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;

import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import android.widget.ImageView;

import com.example.android.sensed.R;
import com.example.android.sensed.services.SensedLocation;
import com.example.android.sensed.ui.adapter.CreateEntryAdapter;
import com.example.android.sensed.entry.ManualEntry;
import com.example.android.sensed.ui.fragment.entry_fragments.EntryOneFragment;

public class CreateEntyActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener {

    private ManualEntry mManualEntry;
    private ViewPager viewPager;
    private ImageView mNextImage;

    private SensedLocation sensedLocation;
    private boolean isBound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_enty);

        viewPager = (ViewPager) findViewById(R.id.ace_view_pager);
        CreateEntryAdapter adapter = new CreateEntryAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(0);

        mNextImage = (ImageView) findViewById(R.id.feo_next_image);

        EntryOneFragment eo = new EntryOneFragment();



        // create intent and bind location service
        Intent i = new Intent(this, SensedLocation.class);
        bindService(i, locationServiceConnection, Context.BIND_AUTO_CREATE);

    }

    public SensedLocation getSensedLocation() {
        return sensedLocation;
    }

//    @Override
//    protected void onStop() {
//        super.onStop();
//        unbindService(locationServiceConnection);
//    }

    public void switchToFragmentOne(){
        if(viewPager.getCurrentItem() != 1){
            viewPager.setCurrentItem(1);
        }
    }

    public void switchToFragmentZero(){
        if(viewPager.getCurrentItem() != 0){
            viewPager.setCurrentItem(0);
        }
    }

    public void setmManualEntry(ManualEntry mManualEntry){
        this.mManualEntry = mManualEntry;
    }

    public ManualEntry getmManualEntry(){
        return this.mManualEntry;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
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
