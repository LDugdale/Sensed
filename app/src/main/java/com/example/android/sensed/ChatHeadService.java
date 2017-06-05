package com.example.android.sensed;

import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

public class ChatHeadService extends Service {

    private WindowManager mWindowManager;
    private FrameLayout mFrameLayout;
    private ImageView mDone;
    private ImageView mCancel;
    private SeekBar mSeekBar;
    private TextView mHappiness;
    private ImageView mHappinessCircle;

    WindowManager.LayoutParams params;

    @Override
    public void onCreate() {
        super.onCreate();
        createLayout();

    }

    public void createLayout(){
        mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);

        // SeekBar styles
        mSeekBar = new SeekBar(this);
        FrameLayout.LayoutParams seekParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        seekParams.setMargins(40, 0, 40, 0);
        seekParams.gravity = Gravity.CENTER;
        mSeekBar.setLayoutParams(seekParams);
        mSeekBar.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        // Happiness circle styles
        mHappinessCircle = new ImageView(this);
        FrameLayout.LayoutParams happinessCircleParams = new FrameLayout.LayoutParams(50, 50);
        happinessCircleParams.setMargins(0, 0, 0, 0);
        happinessCircleParams.gravity = Gravity.CENTER | Gravity.TOP;
        mHappinessCircle.setLayoutParams(happinessCircleParams);
        mHappinessCircle.setImageResource(R.drawable.small_circle);
        mHappinessCircle.setMaxWidth(50);
        mHappinessCircle.setMaxHeight(50);

        // Happiness value styles
        mHappiness = new TextView(this);
        FrameLayout.LayoutParams valueParams = new FrameLayout.LayoutParams(50, 50);
        valueParams.setMargins(0, 0, 0, 0);
        valueParams.gravity = Gravity.CENTER | Gravity.TOP;
        mHappiness.setLayoutParams(valueParams);
        mHappiness.setMaxWidth(50);
        mHappiness.setMaxHeight(50);

        // Done button styles
        mDone = new ImageView(this);
        FrameLayout.LayoutParams doneParams = new FrameLayout.LayoutParams(50, 50);
        doneParams.setMargins(50, 0, 0, 0);
        doneParams.gravity = Gravity.END | Gravity.BOTTOM;
        mDone.setLayoutParams(doneParams);
        mDone.setImageResource(R.drawable.ic_done_24dp);

        // Cancel button styles
        mCancel = new ImageView(this);
        FrameLayout.LayoutParams cancelParams = new FrameLayout.LayoutParams(50, 50);
        cancelParams.setMargins(0, 0, 50, 0);
        cancelParams.gravity = Gravity.START | Gravity.BOTTOM;
        mCancel.setLayoutParams(cancelParams);
        mCancel.setImageResource(R.drawable.ic_done_24dp);

        // FrameLayout styles
        mFrameLayout = new FrameLayout(this);
        mFrameLayout.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 200));
        mFrameLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.white));

        View view = new View(this);
        mFrameLayout.addView(view);

        params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT,
                200,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);

        params.gravity = Gravity.TOP | Gravity.LEFT;
        params.x = 0;
        params.y = 0;

        mDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopService(new Intent(ChatHeadService.this, ChatHeadService.class));

            }
        });

        // add elements to FrameLayout
        mFrameLayout.addView(mSeekBar);
        mFrameLayout.addView(mHappinessCircle);
        mFrameLayout.addView(mHappiness);
        mFrameLayout.addView(mCancel);
        mFrameLayout.addView(mDone);

        // add FrameLayout to window manager
        mWindowManager.addView(mFrameLayout, params);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mDone != null)
            mWindowManager.removeView(mFrameLayout);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }
}