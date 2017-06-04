package com.example.android.sensed.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.android.sensed.ui.fragment.main_fragments.ChatFragment;
import com.example.android.sensed.ui.fragment.main_fragments.MainFragment;
import com.example.android.sensed.ui.fragment.main_fragments.StatsFragment;

/**
 * Created by mnt_x on 28/05/2017.
 */

public class MainPagerAdapter extends FragmentPagerAdapter {

    public MainPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return ChatFragment.create();
            case 1:
                return MainFragment.create();
            case 2:
                return StatsFragment.create();

        }

        return null;
    }

    @Override
    public int getCount() {
        return 3;
    }
}
