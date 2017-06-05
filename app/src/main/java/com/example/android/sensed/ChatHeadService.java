package com.example.android.sensed;

import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.SeekBar;

public class ChatHeadService extends Service {

    private WindowManager mWindowManager;
    private FrameLayout mFrameLayout;
    private ImageView mDone;
    private ImageView mCancel;
    private SeekBar mSeekBar;

    WindowManager.LayoutParams params;

    @Override
    public void onCreate() {
        super.onCreate();

        mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);

        mDone = new ImageView(this);
        mDone.setImageResource(R.drawable.ic_done_24dp);
        mDone.setMaxWidth(50);
        mDone.setMaxHeight(50);

        mFrameLayout = new FrameLayout(this);
        mFrameLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.white));


        params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);

        params.gravity = Gravity.TOP | Gravity.LEFT;
        params.x = 0;
        params.y = 100;

        mDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
                sendBroadcast(intent);
            }
        });

        mFrameLayout.addView(mDone);

        mWindowManager.addView(mFrameLayout, params);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        if (chatHead != null)
//            mWindowManager.removeView(chatHead);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }
}