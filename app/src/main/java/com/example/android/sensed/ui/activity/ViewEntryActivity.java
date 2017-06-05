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

/**
 * Activity for displaying the content of an entry from the recycler view.
 *
 * @author Laurie Dugdale
 */
public class ViewEntryActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    // UI elements
    private TextView mDisplayDate;
    private TextView mDisplayHappiness;

    // the columns to return
    public static final String[] ENTRY_DETAIL = {
            SensedContract.SensedEntry.COLUMN_ENTRY_DATE_TIME,
            SensedContract.SensedEntry.COLUMN_ENTRY_HAPPINESS,
            SensedContract.SensedEntry.COLUMN_ENTRY_LATITUDE,
            SensedContract.SensedEntry.COLUMN_ENTRY_LONGITUDE
    };

    // easy references to the columns
    public static final int INDEX_ENTRY_DATE_TIME = 0;
    public static final int INDEX_ENTRY_HAPPINESS = 1;
    public static final int INDEX_ENTRY_LATITUDE = 2;
    public static final int INDEX_ENTRY_LONGITUDE = 3;

    // arbitrary number to identify the loader responsible for fetching the event details must be unique
    private static final int ID_DETAIL_LOADER = 353;

    // URI used to access details
    private Uri mUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry);

        // Bind UI elements to the class
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

            // case for getting the entries by their id
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

        // check if data is valid
        boolean cursorHasValidData = false;
        if (data != null && data.moveToFirst()) {
            // cursor has valid data continue with the UI logic
            cursorHasValidData = true;
        }

        // no data to display
        if (!cursorHasValidData) {
            return;
        }

        /**************
         * Entry Date *
         **************/
        // read in the entry date
        String entryDate = data.getString(INDEX_ENTRY_DATE_TIME);
        // set the date TextView
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
        // not used
    }
}
