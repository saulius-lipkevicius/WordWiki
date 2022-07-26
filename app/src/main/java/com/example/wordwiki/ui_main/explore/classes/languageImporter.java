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

    private static String currentLanguage;
    private static String currentSection;

    public static void importCloud(ArrayList<String> checkedItemsList, Context context) {
        DatabaseReference dbLanguage;
        DatabaseHelper myDb = new DatabaseHelper(context);

        ValueEventListener valueEventListener = new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                Log.d(TAG, "onCreate: loc2: ");

                if (snapshot.exists()) {
                    for (DataSnapshot snapshot_iter : snapshot.getChildren()) {
                        LanguageModel lng = snapshot_iter.getValue(LanguageModel.class);

                        String checked_item = "{" + lng.getLearningLanguage() + "}_" + "{" + lng.getSectionName()  + "}";
                        Log.d(TAG, "onDataChange: " + checked_item + checkedItemsList.get(0));

                        if (checkedItemsList.contains(checked_item)){
                            myDb.insertCloudDictionary(lng.getLearningLanguage(), lng.getSectionName(), lng.getWords(), lng.getTranslations());
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        // go through all values to create a mapping between language and its sections for later exportation
        for (String currentX : checkedItemsList) {
            Pattern regex = Pattern.compile("\\{(.*?)\\}");
            Matcher regexMatcher = regex.matcher(currentX);

            int i = 0;
            while (regexMatcher.find()) {//Finds Matching Pattern in String
                String currentString = regexMatcher.group(1);
                if (i == 0) {
                    currentLanguage = currentString;
                } else {
                    currentSection = currentString;
                }

                i++;
            }

            dbLanguage = FirebaseDatabase.getInstance("https://wordwiki-af0d4-default-rtdb.europe-west1.firebasedatabase.app").getReference("Languages");
            dbLanguage.addListenerForSingleValueEvent(valueEventListener);
        }
    }


}
