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


    // Store the context for easy access
    private Context mContext;
    private Cursor mCursor;
    /*
      * Below, we've defined an interface to handle clicks on items within this Adapter. In the
      * constructor of our ForecastAdapter, we receive an instance of a class that has implemented
      * said interface. We store that instance in this variable to call the onClick method whenever
      * an item is clicked in the list.
      */
    final private EntryAdapterOnClickHandler mClickHandler;

    /**
     * The interface that receives onClick messages.
     */
    public interface EntryAdapterOnClickHandler {
        void onClick(long date);
    }



    // Pass in the contact array into the constructor
    public EntryAdapter(Context mContext, Cursor mCursor, EntryAdapterOnClickHandler clickHandler) {
        this.mContext = mContext;
        this.mClickHandler = clickHandler;
        this.mCursor = mCursor;
    }


    // Usually involves inflating a layout from XML and returning the holder
    @Override
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

    // Involves populating data into the item through holder
    @Override
    public void onBindViewHolder(EntryAdapter.ViewHolder viewHolder, int position) {

        // Move the mCursor to the position of the item to be displayed
        if (!mCursor.moveToPosition(position)) {
            return; // bail if returned null
        }

        // Update the view holder with the information needed to display
        String date = mCursor.getString(mCursor.getColumnIndex(SensedContract.SensedEntry.COLUMN_ENTRY_DATE_TIME));
        int happiness = mCursor.getInt(mCursor.getColumnIndex(SensedContract.SensedEntry.COLUMN_ENTRY_HAPPINESS));
        long id = mCursor.getLong(mCursor.getColumnIndex(SensedContract.SensedEntry._ID));

        viewHolder.mEntryHappiness.setText(String.valueOf(happiness));


        viewHolder.mEntryDate.setText(parseDate(date));


        if (viewHolder.mEntryPoint != null) {
            viewHolder.mEntryPoint.getDrawable().setColorFilter(createColor(happiness), PorterDuff.Mode.SRC_IN);
        }

        if( viewHolder.mEntryPointOne != null ) {
            viewHolder.mEntryPointOne.getDrawable().setColorFilter(createColor(happiness), PorterDuff.Mode.SRC_IN);

        }

    }

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

    // Returns the total count of items in the list
    @Override
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

    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        public ImageView mEntryPoint;
        public ImageView mEntryPointOne;
        public TextView mEntryDate;
        public TextView mEntryHappiness;

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(View itemView) {

            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);
            mEntryPoint = (ImageView) itemView.findViewById(R.id.iv_entry_point);
            mEntryPointOne = (ImageView) itemView.findViewById(R.id.iv_entry_point_one);
            mEntryDate = (TextView) itemView.findViewById(R.id.ie_date);
            mEntryHappiness = (TextView) itemView.findViewById(R.id.ie_hp_value);

            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            mCursor.moveToPosition(adapterPosition);
            long id = mCursor.getLong(mCursor.getColumnIndex("_id"));
            mClickHandler.onClick(id);
        }
    }


}