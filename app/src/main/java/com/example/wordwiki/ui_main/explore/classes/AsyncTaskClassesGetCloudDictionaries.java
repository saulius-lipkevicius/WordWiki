package com.example.wordwiki.ui_main.explore.classes;

import android.os.AsyncTask;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AsyncTaskClassesGetCloudDictionaries extends AsyncTask<Void, Integer, Void> {
    DatabaseReference dbLanguage;
    ValueEventListener valueEventListener;


    public AsyncTaskClassesGetCloudDictionaries(DatabaseReference dbLanguage, ValueEventListener valueEventListener) {
        this.dbLanguage = dbLanguage;
        this.valueEventListener = valueEventListener;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        dbLanguage = FirebaseDatabase.getInstance("https://wordwiki-af0d4-default-rtdb.europe-west1.firebasedatabase.app").getReference("Dictionaries");
        dbLanguage.addListenerForSingleValueEvent(valueEventListener);
        return null;
    }

}
