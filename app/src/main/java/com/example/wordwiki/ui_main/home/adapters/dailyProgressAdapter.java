package com.example.wordwiki.ui_main.home.adapters;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;


import com.example.wordwiki.R;
import com.example.wordwiki.database.DatabaseHelper;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;

public class dailyProgressAdapter extends PagerAdapter {
    DatabaseHelper myDb;
    private Context context;
    private LayoutInflater layoutInflater;

    public dailyProgressAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return 3;
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

        createChart(view, position);

        ViewPager viewPager = (ViewPager) container;
        viewPager.addView(view, 0);
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
        for (int i = 0; i < iterator.getCount(); i++) {
            //Log.d("tag", "createChart: addd:" + i);

            barEntries.add(new Entry(i, iterator.getInt(position + 1), i));
            iterator.moveToNext();
        }

        iterator.close();

        LineDataSet barDataSet = new LineDataSet(barEntries, "Contracts");
        barDataSet.setAxisDependency(YAxis.AxisDependency.LEFT);
        //        barDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        barDataSet.setColor(Color.parseColor("#BDBDC7"));
        barDataSet.setHighlightEnabled(true);
        barDataSet.setHighLightColor(Color.RED);
        barDataSet.setLineWidth(3);
        barDataSet.setDrawCircles(false);
        barDataSet.setValueTextSize(10);
        barDataSet.setDrawValues(false);
        barDataSet.setValueTextColor(Color.parseColor("#BDBDDD"));
        barDataSet.setDrawFilled(true);
        LineData barData = new LineData(barDataSet);
        barDataSet.setFillAlpha(40);
        barDataSet.setFillColor(Color.parseColor("#BDBDC7"));
        barDataSet.setMode(LineDataSet.Mode.HORIZONTAL_BEZIER);
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
        barChart.getAxisLeft().setLabelCount(3);
        barChart.getXAxis().setLabelCount(4);

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
        //barChart.getXAxis().setLabelCount(10);
        //barChart.getXAxis().setLabelCount(10);

        //if (position == 1) {
        //    barChart.animateY(3500);
        //} else {
        //    barChart.animateY(1500);
        //}

        barChart.clearAnimation();
        //barChart.getXAxis().setGranularityEnabled(true);
        //barChart.getXAxis().setGranularity(5.0f);
        barChart.getXAxis().setLabelCount(4);
        //barChart.getXAxis().setLabelCount(barDataSet.getEntryCount());

        barChart.setData(barData);
    }
}
