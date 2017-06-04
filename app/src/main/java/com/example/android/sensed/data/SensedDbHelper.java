package com.example.android.sensed.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.android.sensed.data.SensedContract.*;
/**
 * @author Laurie Dugdale
 */

public class SensedDbHelper extends SQLiteOpenHelper {

    // The database name
    private static final String DATABASE_NAME = "sensed.db";

    // If you change the database schema, you must increment the database version
    private static final int DATABASE_VERSION = 1;

    // Constructor
    public SensedDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // Create a table to hold waitlist data
        final String SQL_CREATE_ENTRY_TABLE = "CREATE TABLE " + SensedEntry.TABLE_NAME + " (" +
                SensedEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                SensedEntry.COLUMN_ENTRY_DATE_TIME + " TEXT NOT NULL, " +
                SensedEntry.COLUMN_ENTRY_HAPPINESS + " INTEGER NOR NULL," +
                SensedEntry.COLUMN_ENTRY_LATITUDE + " DOUBLE NOT NULL, " +
                SensedEntry.COLUMN_ENTRY_LONGITUDE + " DOUBLE NOT NULL " +

                "); ";

        db.execSQL(SQL_CREATE_ENTRY_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // For now simply drop the table and create a new one. This means if you change the
        // DATABASE_VERSION the table will be dropped.
        // In a production app, this method might be modified to ALTER the table
        // instead of dropping it, so that existing data is not deleted.
        db.execSQL("DROP TABLE IF EXISTS " + SensedEntry.TABLE_NAME);
        onCreate(db);
    }
}
