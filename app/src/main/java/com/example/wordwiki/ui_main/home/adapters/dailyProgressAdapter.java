package com.example.wordwiki.ui_main.home.adapters;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
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

import java.util.ArrayList;
import java.util.Calendar;


public class dailyProgressAdapter extends PagerAdapter {
    DatabaseHelper myDb;
    private Context context;
    private LayoutInflater layoutInflater;
    public int setPosition;

    public dailyProgressAdapter(Context context, int setPosition) {

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
        BarChart barChart = (BarChart) view.findViewById(R.id.barchart_in);

        ArrayList<BarEntry> barEntries = new ArrayList<BarEntry>();

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
            barEntries.add(new BarEntry(i, iterator.getInt(position)));
            dayNames2.add(dayNames.get((day + 1 + i)%7));
            iterator.moveToNext();
        }
        iterator.close();

        BarDataSet barDataSet = new BarDataSet(barEntries, "Contracts");
        barDataSet.setAxisDependency(YAxis.AxisDependency.LEFT);
        //        barDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        barDataSet.setColor(Color.parseColor("#BDBDC7"));
        barDataSet.setHighlightEnabled(true);
        barDataSet.setHighLightColor(Color.RED);
        //barDataSet.setLineWidth(3);
        //barDataSet.setDrawCircles(false);

        // naujas
        barChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(dayNames2));
        barDataSet.setValueFormatter(new DefaultValueFormatter(1));
        barDataSet.setValueTextSize(10);
        barDataSet.setDrawValues(true);
        barDataSet.setValueTextColor(Color.parseColor("#BDBDDD"));
        //barDataSet.setDrawFilled(true);
        BarData barData = new BarData(barDataSet);

        //barDataSet.setFillAlpha(40);
        //barDataSet.setFillColor(Color.parseColor("#BDBDC7"));
        //barDataSet.setMode(LineDataSet.Mode.HORIZONTAL_BEZIER);
        barChart.getDescription().setTextSize(12);
        barChart.setDrawMarkers(false);
        //barChart.getAxisLeft().addLimitLine(lowerLimitLine(2,"Minimum",2,12,5,5));
        //barChart.getAxisLeft().addLimitLine(upperLimitLine(5,"Target",2,12,5,5));
        //barChart.getAxisLeft().setAxisMinimum(0);
        barChart.getDescription().setText("");
        barChart.getLegend().setEnabled(false);
        barChart.getAxisRight().setEnabled(false);
        //remove top border
        barChart.getXAxis().setDrawAxisLine(false);
        barChart.getAxisLeft().setEnabled(false);
        //barChart.getAxisLeft().setLabelCount(0);
        //barChart.setDrawValueAboveBar(true);

        //remove left border
        barChart.getAxisLeft().setDrawAxisLine(false);

        //remove right border
        barChart.getAxisRight().setDrawAxisLine(false);
        barChart.getXAxis().setDrawGridLines(false);
        barChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        barChart.setDrawMarkers(false);
        barChart.setTouchEnabled(false);
        barChart.getAxisLeft().setDrawZeroLine(true);
        barChart.getAxisLeft().setGridColor(Color.parseColor("#f3f2f2"));


        barChart.setGridBackgroundColor(Color.parseColor("#A574AA"));
        XAxis xAxis = barChart.getXAxis();
        xAxis.setGranularity(1f);
        xAxis.setGranularityEnabled(true);
        barChart.getXAxis().setDrawGridLines(false);
        barChart.getXAxis().setDrawAxisLine(false);
        //barChart.getXAxis().setLabelCount(10);
        //barChart.getXAxis().setLabelCount(10);

        //if (position == 1) {
        //    barChart.animateY(3500);
        //} else {
        //    barChart.animateY(1500);
        //}
        xAxis.setDrawLabels(true);
        barChart.clearAnimation();
        //barChart.getXAxis().setGranularityEnabled(true);
        //barChart.getXAxis().setGranularity(5.0f);
        //barChart.getXAxis().setLabelCount(barDataSet.getEntryCount());

        barChart.setData(barData);
    }


}
