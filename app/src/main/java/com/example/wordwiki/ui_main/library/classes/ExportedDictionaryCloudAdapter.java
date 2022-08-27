package com.example.wordwiki.ui_main.library.classes;

import com.example.wordwiki.ui_main.library.models.ExportedDictionaryHelper;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Class to push dictionary data to the google firebase and follow errors if there is any
 */
public class ExportedDictionaryCloudAdapter {
    private final DatabaseReference databaseReference;

    public ExportedDictionaryCloudAdapter(){
        FirebaseDatabase db = FirebaseDatabase.getInstance("https://wordwiki-af0d4-default-rtdb.europe-west1.firebasedatabase.app/");
        String dictionaryDestination = "Dictionaries";
        databaseReference = db.getReference(dictionaryDestination);
    }

    public Task<Void> add(ExportedDictionaryHelper lng){
        // TODO throw error statement here
        return databaseReference.child(lng.getLearningLanguage()).child(lng.getSectionLevel())
                .child(lng.getUsername() + "_" + lng.getSectionName()).setValue(lng);
    }
}
