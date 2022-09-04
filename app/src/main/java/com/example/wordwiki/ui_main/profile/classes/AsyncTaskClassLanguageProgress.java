package com.example.wordwiki.ui_main.profile.classes;

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wordwiki.database.DatabaseHelper;
import com.example.wordwiki.ui_main.profile.adapters.ProgressAdapter;
import com.example.wordwiki.ui_main.profile.models.ProgressHelper;

import java.util.ArrayList;

public class AsyncTaskClassLanguageProgress extends AsyncTask<Void, Integer, Void> {
    RecyclerView recyclerView;
    ProgressAdapter adapter;
    ArrayList<ProgressHelper> list;
    LinearLayoutManager layoutManager;
    RelativeLayout emptyStateFiller;
    Context context;

    DatabaseHelper myDb;

    public AsyncTaskClassLanguageProgress(ArrayList<ProgressHelper> list, RecyclerView recyclerView, ProgressAdapter adapter, LinearLayoutManager layoutManager, RelativeLayout emptyStateFiller, Context context) {
        this.list = list;
        this.recyclerView = recyclerView;
        this.adapter = adapter;
        this.layoutManager = layoutManager;
        this.emptyStateFiller = emptyStateFiller;
        this.context = context;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        myDb = new DatabaseHelper(context);

        Cursor progressDataCursor = myDb.getLanguageProgress();

        if (progressDataCursor != null && progressDataCursor.moveToFirst()) {
            for (int i = 0; i < progressDataCursor.getCount(); i++) {
                String languageName = progressDataCursor.getString(0);
                String minDate = progressDataCursor.getString(1).substring(0, 10);
                String wordCount = progressDataCursor.getString(2);

                list.add(new ProgressHelper(languageName, wordCount + " words since " + minDate));

                progressDataCursor.moveToNext();  //move cursor for pointing to next fetched record
            }
        }


        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        return null;
    }

    @Override
    protected void onPostExecute(Void unused) {
        super.onPostExecute(unused);

        if (list.size() == 0) {
            recyclerView.setVisibility(View.GONE);
            emptyStateFiller.setVisibility(View.VISIBLE);

        } else {
            recyclerView.setVisibility(View.VISIBLE);
            emptyStateFiller.setVisibility(View.GONE);
        }
    }
}
