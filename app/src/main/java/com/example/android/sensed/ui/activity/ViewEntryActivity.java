package com.example.android.sensed.ui.activity;

import android.database.Cursor;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.android.sensed.R;
import com.example.android.sensed.data.SensedContract;

import java.text.ParseException;
import java.util.Date;

public class ViewEntryActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private TextView mDisplayDate;
    private TextView mDisplayHappiness;


    /*
     * The columns of data that we are interested in displaying within our DetailActivity's
     * weather display.
     */
    public static final String[] ENTRY_DETAIL = {
            SensedContract.SensedEntry.COLUMN_ENTRY_DATE_TIME,
            SensedContract.SensedEntry.COLUMN_ENTRY_HAPPINESS
    };

    /*
     * We store the indices of the values in the array of Strings above to more quickly be able
     * to access the data from our query. If the order of the Strings above changes, these
     * indices must be adjusted to match the order of the Strings.
     */
    public static final int INDEX_ENTRY_DATE_TIME = 0;
    public static final int INDEX_ENTRY_HAPPINESS = 1;


    /*
     * This ID will be used to identify the Loader responsible for loading the weather details
     * for a particular day. In some cases, one Activity can deal with many Loaders. However, in
     * our case, there is only one. We will still use this ID to initialize the loader and create
     * the loader for best practice. Please note that 353 was chosen arbitrarily. You can use
     * whatever number you like, so long as it is unique and consistent.
     */
    private static final int ID_DETAIL_LOADER = 353;

    /* The URI that is used to access the chosen day's weather details */
    private Uri mUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry);
        mDisplayDate = (TextView) findViewById(R.id.ae_display_date);
        mDisplayHappiness = (TextView) findViewById(R.id.ae_display_happiness);


        mUri = getIntent().getData();
        if (mUri == null) throw new NullPointerException("URI for DetailActivity cannot be null");
        /* This connects our Activity into the loader lifecycle. */
        getSupportLoaderManager().initLoader(ID_DETAIL_LOADER, null, this);

    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id) {

            case ID_DETAIL_LOADER:

                return new CursorLoader(this,
                        mUri,
                        ENTRY_DETAIL,
                        null,
                        null,
                        null);

            default:
                throw new RuntimeException("Loader Not Implemented: " + id);
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        /*
         * Before we bind the data to the UI that will display that data, we need to check the
         * cursor to make sure we have the results that we are expecting. In order to do that, we
         * check to make sure the cursor is not null and then we call moveToFirst on the cursor.
         * Although it may not seem obvious at first, moveToFirst will return true if it contains
         * a valid first row of data.
         *
         * If we have valid data, we want to continue on to bind that data to the UI. If we don't
         * have any data to bind, we just return from this method.
         */
        boolean cursorHasValidData = false;
        if (data != null && data.moveToFirst()) {
            /* We have valid data, continue on to bind the data to the UI */
            cursorHasValidData = true;
        }

        if (!cursorHasValidData) {
            /* No data to display, simply return and do nothing */
            return;
        }

        /**************
         * Entry Date *
         **************/
        /* Read weather condition ID from the cursor (ID provided by Open Weather Map) */
        String entryDate = data.getString(INDEX_ENTRY_DATE_TIME);

        /* Set the resource ID on the icon to display the art */
        mDisplayDate.setText(parseDate(entryDate));



        /*******************
         * Happiness Level *
         *******************/

        int entryHappiness = data.getInt(INDEX_ENTRY_HAPPINESS);
        mDisplayHappiness.setText(String.valueOf(entryHappiness));


    }

    public String parseDate(String dateString){

        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        Date startDate;
        String newDateString = "";
        try {
            startDate = df.parse(dateString);
            newDateString = df.format(startDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return newDateString;
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
