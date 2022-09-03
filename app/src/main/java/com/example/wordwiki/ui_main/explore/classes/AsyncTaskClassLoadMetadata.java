package com.example.wordwiki.ui_main.explore.classes;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageButton;

import androidx.annotation.NonNull;

import com.example.wordwiki.ui_main.explore.models.ExistingLanguageHelper;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AsyncTaskClassLoadMetadata extends AsyncTask<Void, Integer, Void> {
    Context context;
    ArrayList<ExistingLanguageHelper> list;


    public AsyncTaskClassLoadMetadata(ArrayList<ExistingLanguageHelper> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        DatabaseReference referenceProfile = FirebaseDatabase.getInstance("https://wordwiki-af0d4-default-rtdb.europe-west1.firebasedatabase.app").getReference("Metadate");
        referenceProfile.child("ExistingLanguages").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot language : snapshot.getChildren()) {
                    // create an unique language list of possible dictionaries to choose from
                    String languageText = language.getKey();

                    long dictionaryCount = (long) language.getValue();
                    int dictionaryCountInt = (int) dictionaryCount;
                    list.add(new ExistingLanguageHelper(languageText, dictionaryCountInt));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        return null;
    }
}
