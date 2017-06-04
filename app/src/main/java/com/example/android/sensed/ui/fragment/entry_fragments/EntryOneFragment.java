package com.example.android.sensed.ui.fragment.entry_fragments;

import android.animation.ArgbEvaluator;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.graphics.PorterDuff;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.android.sensed.R;
import com.example.android.sensed.ui.activity.CreateEntyActivity;
import com.example.android.sensed.entry.ManualEntry;
import com.example.android.sensed.ui.fragment.BaseFragment;

import java.util.Locale;

/**
 * Created by mnt_x on 29/05/2017.
 */

public class EntryOneFragment extends BaseFragment implements ViewPager.OnPageChangeListener {

    private ImageView mNextImage;
    private View mRoot;
    private ImageView mEntryPoint;

    // time controls
    private final static String DATE_TIME = "yyyy-MM-dd HH:mm:ss";
    private final static String DATE = "dd-MM-yyyy";
    private final static String TIME = "HH:mm";
    private SimpleDateFormat mDateTime;
    private String mFullDateTime;
    private Calendar c;
    private TextView mDisplayDate;
    private TextView mDisplayTime;
    private TimePickerDialog timePickerDialog;
    private DatePickerDialog datePickerDialog;

    //seekbar controls
    private SeekBar mSeerkBar;
    private TextView mSeekBarValue;
    private int mSeekbarInt;


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mEntryPoint = (ImageView) view.findViewById(R.id.iv_entry_point);
        mDisplayDate = (TextView) view.findViewById(R.id.tv_display_date);
        mDisplayTime = (TextView) view.findViewById(R.id.tv_display_time);
        mEntryPoint.getDrawable().setColorFilter(ContextCompat.getColor(getActivity(), R.color.red), PorterDuff.Mode.SRC_IN);
        nexButton(view);
        dateTimeListeners(view);
        seekBarLogic(view);
    }

    public static EntryOneFragment create(){

        return new EntryOneFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        mRoot = inflater.inflate(getLayoutResId(), container, false);
        inOnCreateView(mRoot, container, savedInstanceState);



        return mRoot;
    }

    public void nexButton(View view){
        mNextImage = (ImageView) view.findViewById(R.id.feo_next_image);
        // go to page 2 on click of end image
        mNextImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((CreateEntyActivity)getActivity()).setmManualEntry(createManualEntry());
                ((CreateEntyActivity)getActivity()).switchToFragmentOne();

            }
        });
    }

    public ManualEntry createManualEntry(){
        if(mFullDateTime == null){
            mDateTime = new SimpleDateFormat(DATE_TIME, Locale.UK);
            mFullDateTime = mDateTime.format(c.getTime());
        }

        return new ManualEntry(mFullDateTime, mSeekbarInt, 0 ,0 );
    }

    public void dateTimeListeners(View view){

        mDateTime = new SimpleDateFormat(DATE_TIME, Locale.UK);

        c = Calendar.getInstance();
        mDateTime.applyPattern(DATE);
        mDisplayDate.setText(mDateTime.format(c.getTime()));
        mDateTime.applyPattern(TIME);
        mDisplayTime.setText(mDateTime.format(c.getTime()));
        mDateTime.applyPattern(DATE_TIME);

        // listner for text field, launches android calendar
        mDisplayDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog.show();
            }
        });

        // listner for text field, launches android calendar
        mDisplayTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timePickerDialog.show();
            }
        });

        // date listner update when changed
        datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                c.set(year, monthOfYear, dayOfMonth);
                mDateTime.applyPattern(DATE);
                mDisplayDate.setText(mDateTime.format(c.getTime()));
                mDateTime.applyPattern(DATE_TIME);
                mFullDateTime = mDateTime.format(c.getTime());
            }

        },c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));

        // time listner update when changed
        timePickerDialog = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                c.set(Calendar.HOUR_OF_DAY, hourOfDay);
                c.set(Calendar.MINUTE, minute);
                mDisplayTime.setText(hourOfDay + ":" + minute);
                mFullDateTime = mDateTime.format(c.getTime());

            }

        }, c.get(c.HOUR_OF_DAY), c.get(c.MINUTE), true);//Yes 24 hour time


    }

    public void seekBarLogic(View view){

        mSeerkBar = (SeekBar) view.findViewById(R.id.sb_entry_seek_bar);
        mSeekBarValue = (TextView) view.findViewById(R.id.tv_seek_bar_value);

        mSeerkBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mSeekBarValue.setText(String.valueOf(progress));
                mSeekbarInt = progress;
                mEntryPoint.getDrawable().setColorFilter(createColor(progress), PorterDuff.Mode.SRC_IN);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    public int createColor(int happiness){

        int mGoodColor = ContextCompat.getColor(getContext(), R.color.green);
        int mMediumColor = ContextCompat.getColor(getContext(), R.color.yellow);
        int mBadColor =  ContextCompat.getColor(getContext(), R.color.red);
        ArgbEvaluator mArgbEvaluator = new ArgbEvaluator();

        if(happiness >= 50){
            float fl = (float) happiness/100;
            return (int) mArgbEvaluator.evaluate(fl, mMediumColor, mGoodColor);
        } else {
            float fl = (float) happiness/100;
            return (int) mArgbEvaluator.evaluate(fl, mBadColor, mMediumColor);
        }

    }


    @Override
    public int getLayoutResId() {
        return R.layout.fragment_entry_one;
    }

    @Override
    public void inOnCreateView(View root, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

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
