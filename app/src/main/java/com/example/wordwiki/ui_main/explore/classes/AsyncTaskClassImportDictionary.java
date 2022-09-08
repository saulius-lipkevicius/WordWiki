package com.example.wordwiki.ui_main.explore.classes;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.example.wordwiki.R;
import com.example.wordwiki.database.DatabaseHelper;

public class AsyncTaskClassImportDictionary extends AsyncTask<String, Integer, Void> {
    ImageButton downloadImage;
    Context context;
    String TAG = "AsyncTaskDownloadedDictionary";
    DatabaseHelper myDb;

    public AsyncTaskClassImportDictionary(ImageButton downloadImage, Context context) {
        this.downloadImage = downloadImage;
        this.context = context;
    }


    /**
     *
     * @param strings:
     *               [0] - username
     *               [1] - language name
     *               [2] - section name
     */
    @Override
    protected Void doInBackground(String... strings) {
        // import language
        languageImporter.importCloud(strings[0] ,strings[1], strings[2], strings[3], context);

        // send the data confirmation to the firebase
        //

        // save the same data in the myDb to speed up app overall
        myDb = new DatabaseHelper(context);
        Log.i(TAG, "doInBackground: import dictionary: " + strings[0]  + strings[1] +  strings[2] +  strings[3] +  strings[4]);
        myDb.isDictionaryImported(strings[0] ,strings[1], strings[2], "1", strings[3], strings[4]);

        return null;
    }

    @Override
    protected void onPostExecute(Void unused) {
        super.onPostExecute(unused);

        downloadImage.setImageResource(R.drawable.ic_checked);
        downloadImage.setClickable(false);
    }
}
