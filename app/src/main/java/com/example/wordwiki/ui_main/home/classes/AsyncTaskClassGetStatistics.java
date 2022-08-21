package com.example.wordwiki.ui_main.home.classes;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Adapter;

import androidx.viewpager.widget.ViewPager;

import com.example.wordwiki.ui_main.home.adapters.dailyProgressAdapter;

public class AsyncTaskClassGetStatistics extends AsyncTask<Integer, Integer, Void> {
    ViewPager viewPager;
    dailyProgressAdapter adapter;
    Context context;
    String TAG = "main background";
    public AsyncTaskClassGetStatistics(ViewPager viewPager, dailyProgressAdapter adapter, Context context) {
        this.viewPager = viewPager;
        this.adapter = adapter;
        this.context = context;
    }

    @Override
    protected Void doInBackground(Integer... param) {
        try {
            Log.i(TAG, "doInBackground: tests");
            adapter = new dailyProgressAdapter(context, param[0]);
            viewPager.setCurrentItem(param[0]);
            viewPager.setAdapter(adapter);
        } catch (Error e) {
            e.printStackTrace();
        }

        return null;
    }
}
