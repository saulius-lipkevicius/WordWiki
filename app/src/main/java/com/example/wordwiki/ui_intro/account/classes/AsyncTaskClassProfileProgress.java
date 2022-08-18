package com.example.wordwiki.ui_intro.account.classes;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.blongho.country_data.World;
import com.example.wordwiki.database.DatabaseHelper;
import com.example.wordwiki.ui_main.profile.models.progressHelper;

import java.io.IOException;

public class AsyncTaskClassProfileProgress extends AsyncTask<progressHelper, Integer, Integer> {
    ImageView flag;
    TextView languageName;
    TextView progressText;
    Context context;
    DatabaseHelper myDb;

    public AsyncTaskClassProfileProgress(ImageView flag, TextView languageName, TextView progressText, Context context) {
        this.flag = flag;
        this.languageName = languageName;
        this.progressText = progressText;
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }


    @Override
    protected Integer doInBackground(progressHelper... params) {
        try {

            String countryISO = myDb.getFlagISO(params[0].getTitle());
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
