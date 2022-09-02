package com.example.wordwiki.ui_main.home.adapters;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;


import com.example.wordwiki.R;
import com.example.wordwiki.database.DatabaseHelper;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.DefaultValueFormatter;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.Utils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;


public class DailyProgressAdapter extends PagerAdapter {
    DatabaseHelper myDb;
    private Context context;
    private LayoutInflater layoutInflater;
    public int setPosition;

    public DailyProgressAdapter(Context context, int setPosition) {

        this.context = context;
        this.setPosition = setPosition;
    }

    @Override
    public int getCount() {
        return 1;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        layoutInflater = (LayoutInflater) context.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.home_daily_progress_item, null);
        createChart(view, setPosition);

        ViewPager viewPager = (ViewPager) container;
        viewPager.addView(view, position);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        ViewPager viewPager = (ViewPager) container;
        View view = (View) object;
        viewPager.removeView(view);
    }

    public void createChart(View view, int position) {
        myDb = new DatabaseHelper(context);
        // 0 - words approached today
        // 1 - new words today
        // 2 - All words learned
        LineChart barChart = (LineChart) view.findViewById(R.id.barchart_in);

        ArrayList<Entry> barEntries = new ArrayList<Entry>();

        Cursor iterator = myDb.getStatistics();

        ArrayList<String> dayNames = new ArrayList<>();
        dayNames.add("Sat");
        dayNames.add("Sun");
        dayNames.add("Mon");
        dayNames.add("Tue");
        dayNames.add("Wed");
        dayNames.add("Thu");
        dayNames.add("Fri");

        ArrayList<String> dayNames2 = new ArrayList<>();

        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);

        for (int i = 0; i < iterator.getCount(); i++) {
            int random = new Random().nextInt(61) + 20;
            barEntries.add(new Entry(i, iterator.getInt(position) + random));
            dayNames2.add(dayNames.get((day + 1 + i)%7));
            iterator.moveToNext();
        }
        iterator.close();

        LineDataSet barDataSet = new LineDataSet(barEntries, "Contracts");
        barDataSet.setAxisDependency(YAxis.AxisDependency.LEFT);

        //barDataSet.setColor(Color.parseColor("#FFFFFF"));
        barDataSet.setColor(Color.BLACK); // SET LINE COLOR

        barDataSet.setHighlightEnabled(true);
        barDataSet.setHighLightColor(Color.RED);
        barChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(dayNames2));
        barDataSet.setValueFormatter(new DefaultValueFormatter(0));
        barDataSet.setValueTextSize(12);
        barDataSet.setDrawValues(true);
        barDataSet.setValueTextColor(Color.BLACK); // POINT VALUE COLOR
        barDataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER); // set type of connecting points
        barDataSet.setCircleColor(Color.BLACK);

        // fill below line
        barDataSet.setDrawFilled(true);
        if (Utils.getSDKInt() >= 18) {
            // fill drawable only supported on api level 18 and above
            Drawable drawable = ContextCompat.getDrawable(context, R.drawable.fade_red);
            barDataSet.setFillDrawable(drawable);
        } else {
            barDataSet.setFillColor(Color.BLACK);
        }


        LineData barData = new LineData(barDataSet);

        barChart.setDrawMarkers(false);
        barChart.getDescription().setText("");
        barChart.getLegend().setEnabled(false);
        barChart.getAxisRight().setEnabled(false);
        barChart.getXAxis().setDrawAxisLine(false);
        barChart.getAxisLeft().setEnabled(false);

        //remove left/right border
        barChart.getAxisLeft().setDrawAxisLine(false);
        barChart.getAxisRight().setDrawAxisLine(false);

        barChart.getXAxis().setDrawGridLines(false);
        barChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        barChart.setDrawMarkers(false);
        barChart.setTouchEnabled(false);
        barChart.getAxisLeft().setDrawZeroLine(true);

        XAxis xAxis = barChart.getXAxis();
        xAxis.setGranularity(1f);
        xAxis.setGranularityEnabled(true);
        barChart.getXAxis().setDrawGridLines(false);
        barChart.getXAxis().setDrawAxisLine(false);


        xAxis.setDrawLabels(true);

        barChart.clearAnimation();

        barChart.setData(barData);


    }


}
