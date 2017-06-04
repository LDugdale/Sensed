package com.example.android.sensed.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.android.sensed.ui.fragment.entry_fragments.EntryOneFragment;
import com.example.android.sensed.ui.fragment.entry_fragments.EntryTwoFragment;

/**
 * @author Laurie Dugdale
 */

public class CreateEntryAdapter extends FragmentPagerAdapter {

    public CreateEntryAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                return EntryOneFragment.create();
            case 1:
                return EntryTwoFragment.create();

        }
        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }
}
