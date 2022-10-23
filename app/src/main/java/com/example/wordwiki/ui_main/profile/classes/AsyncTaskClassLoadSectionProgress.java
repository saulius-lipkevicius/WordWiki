package com.example.wordwiki.ui_main.profile.classes;

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.LinearLayout;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wordwiki.database.DatabaseHelper;
import com.example.wordwiki.ui_main.profile.adapters.ProgressSectionAdapter;
import com.example.wordwiki.ui_main.profile.models.ProgressSectionHelper;

import java.util.ArrayList;

public class AsyncTaskClassLoadSectionProgress extends AsyncTask<String, Integer, Void> {
    RecyclerView sectionStatsRecycler;
    ArrayList<ProgressSectionHelper> sectionList;
    LinearLayoutManager layoutManager;
    Context context;

    DatabaseHelper myDb;

    public AsyncTaskClassLoadSectionProgress(RecyclerView sectionStatsRecycler, ArrayList<ProgressSectionHelper> sectionList, LinearLayoutManager layoutManager, Context context) {
        this.sectionStatsRecycler = sectionStatsRecycler;
        this.sectionList = sectionList;
        this.layoutManager = layoutManager;
        this.context = context;
    }

    @Override
    protected Void doInBackground(String ... strings) {
        myDb = new DatabaseHelper(context);

        Cursor sectionProgress = myDb.getSectionProgress(strings[0]);

        int size = sectionProgress.getCount();
        for (int i = 0; i < size; i++) {

            // Fetch your data values
            String sectionName = sectionProgress.getString(0);
            int learnedCount = sectionProgress.getInt(1);
            int allCount = sectionProgress.getInt(2);


            sectionList.add(new ProgressSectionHelper(sectionName, learnedCount, allCount));

            // Position the cursor
            sectionProgress.moveToNext();
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void unused) {
        super.onPostExecute(unused);

        ProgressSectionAdapter sectionAdapter = new ProgressSectionAdapter(sectionList, context);
        sectionStatsRecycler.setLayoutManager(layoutManager);
        sectionStatsRecycler.setAdapter(sectionAdapter);
    }

}
