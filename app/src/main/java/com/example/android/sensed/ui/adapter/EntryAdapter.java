package com.example.android.sensed.ui.adapter;

import android.animation.ArgbEvaluator;
import android.content.Context;
import android.database.Cursor;
import android.graphics.PorterDuff;
import android.icu.text.DateFormat;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.sensed.R;
import com.example.android.sensed.data.SensedContract;

import java.text.ParseException;
import java.util.Date;
import java.util.Locale;

/**
 * Created by mnt_x on 30/05/2017.
 */

public class EntryAdapter extends RecyclerView.Adapter<EntryAdapter.ViewHolder> {


    // Store the context and cursor for easy access
    private Context mContext;
    private Cursor mCursor;

    // This interface handles clicks on items within this Adapter. This is populated from the constructor
    // Call the instance in this variable to call the onClick method whenever and item is clicked in the list.
    final private EntryAdapterOnClickHandler mClickHandler;

    /**
     * The interface that receives onClick messages.
     */
    public interface EntryAdapterOnClickHandler {
        void onClick(long date);
    }

    /**
     * Entry adapter constructor
     *
     * @param mContext
     * @param mCursor
     * @param clickHandler
     */
    public EntryAdapter(Context mContext, Cursor mCursor, EntryAdapterOnClickHandler clickHandler) {
        this.mContext = mContext;
        this.mClickHandler = clickHandler;
        this.mCursor = mCursor;
    }

    @Override
    /**
     * Inflates a layout depending on its position and returns a ViewHolder
     */
    public EntryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View contactView = null;

        if (viewType == 1) {
            // inflate first item layout & return that viewHolder
            contactView = inflater.inflate(R.layout.item_entry_first, parent, false);
        } else if (viewType == getItemCount()){
            // inflate second item layout & return that viewHolder
            contactView = inflater.inflate(R.layout.item_entry_last, parent, false);
        } else {
            // inflate second item layout & return that viewHolder
            contactView = inflater.inflate(R.layout.item_entry, parent, false);
        }

        // Return a new holder instance
        return new ViewHolder(contactView);
    }

    @Override
    /**
     * Populates data into the layout through the viewholder
     */
    public void onBindViewHolder(EntryAdapter.ViewHolder viewHolder, int position) {

        // Move the mCursor to the position of the item to be displayed
        if (!mCursor.moveToPosition(position)) {
            return;
        }

        // get values from the cursor to update the view holder
        String date = mCursor.getString(mCursor.getColumnIndex(SensedContract.SensedEntry.COLUMN_ENTRY_DATE_TIME));
        int happiness = mCursor.getInt(mCursor.getColumnIndex(SensedContract.SensedEntry.COLUMN_ENTRY_HAPPINESS));
        long id = mCursor.getLong(mCursor.getColumnIndex(SensedContract.SensedEntry._ID));

        // set the values from the cursor to the UI elements
        viewHolder.mEntryHappiness.setText(String.valueOf(happiness));
        viewHolder.mEntryDate.setText(parseDate(date));


        // if not null set the colour of the EntryPoint UI element depending on the happiness level
        if (viewHolder.mEntryPoint != null) {
            viewHolder.mEntryPoint.getDrawable().setColorFilter(createColor(happiness), PorterDuff.Mode.SRC_IN);
        }

        // if not null set the colour of the EntryPoint UI element depending on the happiness level
        if( viewHolder.mEntryPointOne != null ) {
            viewHolder.mEntryPointOne.getDrawable().setColorFilter(createColor(happiness), PorterDuff.Mode.SRC_IN);

        }

    }

    /**
     * converts the date from the database to a more readable format
     *
     * @param dateString
     * @return
     */
    public String parseDate(String dateString){

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date startDate;
        String newDateString = "";
        try {
            startDate = df.parse(dateString);
            df.applyPattern("d MMMM yyyy");
            newDateString = df.format(startDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return newDateString;
    }

    /**
     * Gets the corresponding colour according to the value of the happiness level
     * @param happiness
     * @return
     */
    public int createColor(int happiness){

        int mGoodColor = ContextCompat.getColor(mContext, R.color.green);
        int mMediumColor = ContextCompat.getColor(mContext, R.color.yellow);
        int mBadColor =  ContextCompat.getColor(mContext, R.color.red);
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
    /**
     * return the total count of items in the list
     */
    public int getItemCount() {
        if (mCursor == null) {
            return 0;
        }
        return mCursor.getCount();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) return 1;
        else return 2;
    }

    /**
     * CLASS
     * Used to cache the views within the layout for quick access
     */
    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        // The UI elements
        public ImageView mEntryPoint;
        public ImageView mEntryPointOne;
        public TextView mEntryDate;
        public TextView mEntryHappiness;

        public ViewHolder(View itemView) {
            super(itemView);

            // Find the UI elements
            mEntryPoint = (ImageView) itemView.findViewById(R.id.iv_entry_point);
            mEntryPointOne = (ImageView) itemView.findViewById(R.id.iv_entry_point_one);
            mEntryDate = (TextView) itemView.findViewById(R.id.ie_date);
            mEntryHappiness = (TextView) itemView.findViewById(R.id.ie_hp_value);

            // set the listener as this class
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            // get the position of the current item
            int adapterPosition = getAdapterPosition();
            // move cursor to that position
            mCursor.moveToPosition(adapterPosition);
            // get the ID from the cursor
            long id = mCursor.getLong(mCursor.getColumnIndex("_id"));
            // call the onClick method for the mClickHandler variable
            mClickHandler.onClick(id);
        }
    }


}