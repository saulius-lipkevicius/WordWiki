package com.example.wordwiki.ui_main.explore.classes;

import android.os.AsyncTask;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AsyncTaskClassesGetCloudDictionaries extends AsyncTask<String, Integer, Void> {
    DatabaseReference dbLanguage;
    ValueEventListener valueEventListener;


    public AsyncTaskClassesGetCloudDictionaries(DatabaseReference dbLanguage, ValueEventListener valueEventListener) {
        this.dbLanguage = dbLanguage;
        this.valueEventListener = valueEventListener;
    }

    @Override
    protected Void doInBackground(String... param) {
        dbLanguage = FirebaseDatabase.getInstance("https://wordwiki-af0d4-default-rtdb.europe-west1.firebasedatabase.app").getReference("Dictionaries")
            .child(param[0]).child(param[1]);
        dbLanguage.addListenerForSingleValueEvent(valueEventListener);
        return null;
    }

}
