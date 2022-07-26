package com.example.wordwiki.classes;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;

import com.blongho.country_data.World;
import com.example.wordwiki.database.DatabaseHelper;
import com.example.wordwiki.ui_intro.account.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AsyncTaskClassFetchProfileData extends AsyncTask<String, Integer, Integer> {
    DatabaseHelper myDb;
    String country_name;
    Context context;
    int flag;

    public AsyncTaskClassFetchProfileData(Context context) {
        this.context = context;
    }

    @Override
    protected Integer doInBackground(String... Uid) {
        try {
            myDb = new DatabaseHelper(context);

            SharedPreferences sharedPreferences = context.getSharedPreferences("user_profile", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();

            // here starts the algorithm
            DatabaseReference referenceProfile = FirebaseDatabase.getInstance("https://wordwiki-af0d4-default-rtdb.europe-west1.firebasedatabase.app").getReference("Users")
                    .child("Personal");
            referenceProfile.child(Uid[0]).child("ProfileInfo").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    User data = snapshot.getValue(User.class);
                    Log.i(TAG, "onDataChange: got Uid: " + Uid[0]);
                    if (data != null) {
                        Log.i(TAG, "onDataChange: setting an username to : " + data.getUsername());
                        editor.putString("username", "@" + data.getUsername());
                        editor.putString("userDescription", data.getDescription());

                        editor.putString("imagePath", data.getProfileImageURL());
                        editor.apply();
                        // add learning/known languages
                        data.getProficiency().putAll(data.getLearning());

                        // for languages
                        SharedPreferences spLanguage = context.getSharedPreferences("user_profile_language", Context.MODE_PRIVATE);
                        SharedPreferences.Editor spLanguageEditor = spLanguage.edit();

                        // for levels
                        SharedPreferences spLevel = context.getSharedPreferences("user_profile_language_level", Context.MODE_PRIVATE);
                        SharedPreferences.Editor spLevelEditor = spLevel.edit();

                        spLanguageEditor.clear();
                        spLevelEditor.clear();

                        for (String key : data.getProficiency().keySet()) {
                            country_name = myDb.getFlagISO(key);
                            flag = World.getFlagOf(country_name);

                            spLanguageEditor.putInt(key, flag);
                            spLevelEditor.putInt(key, data.getProficiency().get(key));

                        }
                        spLanguageEditor.apply();
                        spLevelEditor.apply();

                        Log.i(TAG, "onDataChange: input data: " + data.getLearning());
                        Log.i(TAG, "onDataChange: input data: " + data.getProfileImageURL());
                        Log.i(TAG, "onDataChange: input data: " + data.getUsername());
                        Log.i(TAG, "onDataChange: input data: " + data.getProficiency());
                        Log.i(TAG, "onDataChange: input data: " + data.getDescription());
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });


            myDb.close();

        } catch (Error e) {
            e.printStackTrace();
        }

        return 0;
    }

}

