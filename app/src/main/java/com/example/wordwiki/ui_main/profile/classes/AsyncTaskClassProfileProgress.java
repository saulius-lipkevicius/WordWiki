package com.example.wordwiki.ui_main.profile.classes;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import com.blongho.country_data.World;
import com.example.wordwiki.database.DatabaseHelper;
import com.example.wordwiki.ui_main.profile.models.progressHelper;


public class AsyncTaskClassProfileProgress extends AsyncTask<progressHelper, Integer, Integer> {
    ImageView flag;
    Context context;
    DatabaseHelper myDb;

    public AsyncTaskClassProfileProgress(ImageView flag, Context context) {
        this.flag = flag;
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }


    @Override
    protected Integer doInBackground(progressHelper... helpers) {
        Log.i(TAG, "doInBackground: " + helpers[0].getTitle() + " : " + helpers[0].getWordCounter());
        try {
            myDb = new DatabaseHelper(context);
            String countryISO = myDb.getFlagISO(helpers[0].getTitle());
            Integer flagInt = World.getFlagOf(countryISO);
            return flagInt;
        } catch (Error e) {
            e.printStackTrace();

        }

        return 0;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(Integer flagInt) {
        super.onPostExecute(flagInt);

        flag.setImageResource(flagInt);

    }


}
