package com.example.android.sensed.ui.fragment.main_fragments;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.sensed.R;
import com.example.android.sensed.ui.activity.ViewEntryActivity;
import com.example.android.sensed.ui.adapter.EntryAdapter;
import com.example.android.sensed.data.SensedContract;
import com.example.android.sensed.data.SensedDbHelper;
import com.example.android.sensed.ui.fragment.BaseFragment;

/**
 * Created by mnt_x on 28/05/2017.
 */

public class MainFragment extends BaseFragment  implements LoaderManager.LoaderCallbacks<Cursor>, EntryAdapter.EntryAdapterOnClickHandler {

    private EntryAdapter mAdapter;
    private SQLiteDatabase database;
    private RecyclerView mRecyclerView;

    public static MainFragment create(){

        return new MainFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        // Replace 'android.R.id.list' with the 'id' of your RecyclerView
        mRecyclerView = (RecyclerView) view.findViewById(R.id.rv_main_frag);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this.getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new EntryAdapter(getActivity(), getAllEntries(), this);
        mRecyclerView.setAdapter(mAdapter);
        return view;
    }

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_main;
    }

    @Override
    public void inOnCreateView(View root, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

    }

    /**
     * Query the mDb and get all guests from the waitlist table
     *
     * @return Cursor containing the list of guests
     */
    public Cursor getAllEntries() {
        SensedDbHelper dbHelper = new SensedDbHelper(getContext());
        database = dbHelper.getWritableDatabase();
        return database.query(
                SensedContract.SensedEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                "_id DESC"
        );
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    @Override
    /**
     * On click method when an item in the recyclerview is clicked this launches the ViewEntryActivity class
     * passes the Uri of the clicked entry
     */
    public void onClick(long id) {

        Intent entryDetailIntent = new Intent(getContext(), ViewEntryActivity.class);
        Uri uriForDateClicked = SensedContract.SensedEntry.buildEntryUriWithId(id);
        entryDetailIntent.setData(uriForDateClicked);

        startActivity(entryDetailIntent);
    }
}
