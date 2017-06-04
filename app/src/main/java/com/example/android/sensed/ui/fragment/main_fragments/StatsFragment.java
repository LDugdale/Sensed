package com.example.android.sensed.ui.fragment.main_fragments;

import android.database.Cursor;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.sensed.R;
import com.example.android.sensed.data.SensedContract;
import com.example.android.sensed.ui.fragment.BaseFragment;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.text.ParseException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

/**
 * @author Laurie Dugdale
 */

public class StatsFragment extends BaseFragment implements LoaderManager.LoaderCallbacks<Cursor> {

    // graphs
    private GraphView mOverviewGraph;
    private LineGraphSeries<DataPoint> mOverviewSeries;

    private final static DataPoint [] RESET_DATA = {new DataPoint(0.0, 0.0)};

    // calendar controls
    private ImageView mNextMonth;
    private ImageView mPreviousMonth;
    private TextView mCurrentYearMonth;
    private GregorianCalendar mCalendar;
    private SimpleDateFormat mSimpleDateFormat;

    private Cursor mCursor;

    private final static String DATE_TIME = "yyyy-MM-dd HH:mm:ss";


    private String mToDate;
    private String mFromDate;

    private LoaderManager.LoaderCallbacks<Cursor> mLoader = this;
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
    private static final int OVERVIEW_GRAPH = 100;


    public static StatsFragment create(){

        return new StatsFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mNextMonth = (ImageView) view.findViewById(R.id.date_right);
        mPreviousMonth = (ImageView) view.findViewById(R.id.date_left);
        mCurrentYearMonth = (TextView) view.findViewById(R.id.stats_date);

        calendar();
        createOverviewGraph(view);

    }

    public void createOverviewGraph(View view){

        getActivity().getSupportLoaderManager().initLoader(OVERVIEW_GRAPH, null, this);
        mOverviewGraph = (GraphView) view.findViewById(R.id.overview_graph);

    }

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_stats;
    }

    @Override
    public void inOnCreateView(View root, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id) {

            case OVERVIEW_GRAPH:
                // URI for all rows of entry data
                Uri entryQueryUri = SensedContract.SensedEntry.CONTENT_URI;
                String selection = SensedContract.SensedEntry.getSqlSelectbetweenDates(startDay(), endDay());

                return new CursorLoader(
                        getActivity(),
                        entryQueryUri,
                        ENTRY_DETAIL,
                        selection,
                        null,
                        SensedContract.SensedEntry.COLUMN_ENTRY_DATE_TIME);

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


        mCursor = data;

        /******************
         * Overview Graph *
         ******************/
        mOverviewSeries = new LineGraphSeries<>();
        mOverviewSeries.setColor(getResources().getColor(R.color.yellow));
        mOverviewSeries.setThickness(10);
        mOverviewSeries.resetData(generateOverviewData());
//        mOverviewSeries = new LineGraphSeries<>(generateOverviewData());
        mOverviewGraph.addSeries(mOverviewSeries);


    }


    /**
     * Generating overview graph data
     * @return
     */
    public DataPoint[] generateOverviewData(){


        int cursorSize = mCursor.getCount();
        double[][] happiness = new double[cursorSize][2];
        double[] date = new double[cursorSize];
        int count = 0;

        for (mCursor.moveToFirst(); !mCursor.isAfterLast(); mCursor.moveToNext()) {
            double currentHappiness = mCursor.getDouble(INDEX_ENTRY_HAPPINESS);
            double currentDate = parseDate(mCursor.getString(INDEX_ENTRY_DATE_TIME));
            if (currentDate != date[count]) {
                date[count] = currentDate;
                happiness[count][0] = currentHappiness;
            } else {
                happiness[count][0] += currentHappiness;
                happiness[count][1]++;
            }
            if (mCursor.moveToNext()) {
                if (currentDate != parseDate(mCursor.getString(INDEX_ENTRY_DATE_TIME)) && count < cursorSize) {
                    count++;
                }
                mCursor.moveToPrevious();
            }
        }
        int i = 0;
        DataPoint[] values = new DataPoint[count + 1];
        String [] dateLabels = new String [count + 1];
        for ( i = 0; i < date.length && date[i] != 0.0; i++) {
            int xAvg = (int) happiness[i][0] / (int) (happiness[i][1] + 1);
//            series.appendData(new DataPoint(date[i], xAvg), false, count + 1);
            values[i] = new DataPoint(date[i], xAvg);
            dateLabels[i] = String.valueOf(date[i]);
        }


        mOverviewGraph.getGridLabelRenderer().setGridColor(getResources().getColor(R.color.dark_grey));
        mOverviewGraph.getGridLabelRenderer().setNumHorizontalLabels(4);
        mOverviewGraph.getGridLabelRenderer().setGridStyle(GridLabelRenderer.GridStyle.HORIZONTAL);
        mOverviewGraph.getGridLabelRenderer().reloadStyles();
        mOverviewGraph.getViewport().setYAxisBoundsManual(true);
        mOverviewGraph.getViewport().setMinY(0.0);
        mOverviewGraph.getViewport().setMaxY(100.0);
        mOverviewGraph.getViewport().setXAxisBoundsManual(true);
        mOverviewGraph.getViewport().setMinX(date[0]);
        mOverviewGraph.getViewport().setMaxX(date[i - 1]);

//        StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(mOverviewGraph);
//        staticLabelsFormatter.setHorizontalLabels(dateLabels);
//        mOverviewGraph.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter);

        return values;

    }

    public double parseDate(String dateString){

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date startDate;

        double newDateString = 0.0;
        try {
            startDate = df.parse(dateString);
            df.applyPattern("dd");
            newDateString = Double.parseDouble(df.format(startDate));

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return newDateString;
    }

    public void calendar(){

        mCalendar = new GregorianCalendar();
        mSimpleDateFormat = new SimpleDateFormat("MMMM yyyy", Locale.UK);
        String time = mSimpleDateFormat.format(mCalendar.getTime()).toString();

        Date start = null;
        try {
            start = mSimpleDateFormat.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        mCalendar.setTime(start);
        mCurrentYearMonth.setText(mSimpleDateFormat.format(mCalendar.getTime()));
        mNextMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCalendar.add(Calendar.MONTH , 1);
                mCurrentYearMonth.setText(mSimpleDateFormat.format(mCalendar.getTime()));
                getActivity().getSupportLoaderManager().restartLoader(OVERVIEW_GRAPH, null, mLoader);
                mOverviewSeries.resetData(RESET_DATA);

            }
        });

        mPreviousMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCalendar.add(Calendar.MONTH , -1);
                mCurrentYearMonth.setText(mSimpleDateFormat.format(mCalendar.getTime()));
                getActivity().getSupportLoaderManager().restartLoader(OVERVIEW_GRAPH, null, mLoader);
                mOverviewSeries.resetData(RESET_DATA);
            }
        });
    }


    public String startDay(){

        Calendar c = Calendar.getInstance();
        c.setTime(mCalendar.getTime());
        c.set(Calendar.DAY_OF_MONTH, 1);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        return sdf.format(c.getTime());
    }

    public String endDay(){
        Calendar c = Calendar.getInstance();
        c.setTime(mCalendar.getTime());
        c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        return sdf.format(c.getTime());
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
