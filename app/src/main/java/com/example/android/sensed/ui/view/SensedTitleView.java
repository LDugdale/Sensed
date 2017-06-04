package com.example.android.sensed.ui.view;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.example.android.sensed.R;
import com.example.android.sensed.ui.activity.CreateEntyActivity;

/**
 * Created by mnt_x on 03/06/2017.
 */

public class SensedTitleView extends FrameLayout implements ViewPager.OnPageChangeListener {

    public SensedTitleView(@NonNull Context context) {
        this(context, null);
    }

    public SensedTitleView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SensedTitleView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    /**
     * Initialise the ImageViews
     */
    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_sensed_title, this, true);


    }

    /**
     * listeners for the tabbed icons (ImageView)
     * @param viewPager
     */
    public void setUpWithViewPager(final ViewPager viewPager, final Context context) {
        viewPager.addOnPageChangeListener(this);

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
}
