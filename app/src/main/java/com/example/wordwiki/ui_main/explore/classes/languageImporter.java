package com.example.wordwiki.ui_main.explore.classes;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.wordwiki.database.DatabaseHelper;
import com.example.wordwiki.ui_main.explore.models.LanguageModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class languageImporter {
    private static final String TAG = "ExcelExporter";

    public static void importCloud(String languageName, String sectionName, Context context) {
        DatabaseReference dbLanguage;
        DatabaseHelper myDb = new DatabaseHelper(context);

        ValueEventListener valueEventListener = new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                LanguageModel lng = snapshot.getValue(LanguageModel.class);
                myDb.insertCloudDictionary(languageName, sectionName, lng.getWords(), lng.getTranslations());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };

        dbLanguage = FirebaseDatabase.getInstance("https://wordwiki-af0d4-default-rtdb.europe-west1.firebasedatabase.app").getReference("Dictionaries");
        dbLanguage.addListenerForSingleValueEvent(valueEventListener);
    }
}

