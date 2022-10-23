package com.example.wordwiki.ui_main.library.classes;

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.constraintlayout.utils.widget.ImageFilterView;
import androidx.recyclerview.widget.RecyclerView;

import com.blongho.country_data.World;
import com.example.wordwiki.R;
import com.example.wordwiki.database.DatabaseHelper;
import com.example.wordwiki.ui_main.library.adapters.SectionAdapter;
import com.example.wordwiki.ui_main.library.adapters.SubsectionAdapter;
import com.example.wordwiki.ui_main.library.models.SectionHelper;
import com.example.wordwiki.ui_main.library.models.SubsectionHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class AsyncTaskClassLoadLibrary extends AsyncTask<String, Void, Void> {
    TextView sectionName;
    ImageFilterView sectionFlag;
    RecyclerView subsectionRecyclerView;
    List<SubsectionHelper> items;
    SectionAdapter.ViewHolder  holder;
    Context context;
    SubsectionAdapter subsectionAdapter;

    public AsyncTaskClassLoadLibrary(TextView sectionName, ImageFilterView sectionFlag, RecyclerView subsectionRecyclerView, List<SubsectionHelper> items, SectionAdapter.ViewHolder holder, Context context) {
        this.sectionName = sectionName;
        this.sectionFlag = sectionFlag;
        this.subsectionRecyclerView = subsectionRecyclerView;
        this.items = items;
        this.holder = holder;
        this.context = context;
    }

    @Override
    protected Void doInBackground(String... strings) {


        sectionName.setText(strings[0]);
        sectionFlag.setImageResource(Integer.parseInt(strings[1]));

        subsectionAdapter = new SubsectionAdapter(
                items, holder, context
        );

        subsectionRecyclerView.setAdapter(subsectionAdapter);

        // TEST SHIMMER EFFECT
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        ///////////

        return null;
    }

    @Override
    protected void onPostExecute(Void unused) {
        super.onPostExecute(unused);

        subsectionAdapter.notifyDataSetChanged();
    }
}
