package com.example.wordwiki.ui_main.profile.classes;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.blongho.country_data.World;
import com.example.wordwiki.database.DatabaseHelper;
import com.example.wordwiki.ui_intro.account.User;
import com.example.wordwiki.ui_main.profile.models.FlagHelper;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import de.hdodenhof.circleimageview.CircleImageView;


public class AsyncTaskClassUploadProfile extends AsyncTask<String, Integer, Integer> {
    CircleImageView profilePic;
    TextView usernameView;
    TextView userDescription;
    ArrayList<FlagHelper> flagLocations;
    Context context;
    DatabaseHelper myDb;
    String country_name;
    int flag;

    public AsyncTaskClassUploadProfile(CircleImageView profilePic, TextView usernameView, TextView userDescription, ArrayList<FlagHelper> flagLocations, Context context) {
        this.profilePic = profilePic;
        this.usernameView = usernameView;
        this.userDescription = userDescription;
        this.flagLocations = flagLocations;
        this.context = context;
    }

    @Override
    protected Integer doInBackground(String... Uid) {
        try {
            myDb = new DatabaseHelper(context);

            // here starts the algorithm
            DatabaseReference referenceProfile = FirebaseDatabase.getInstance("https://wordwiki-af0d4-default-rtdb.europe-west1.firebasedatabase.app").getReference("Users")
                    .child("Personal").child(Uid[0]);
            referenceProfile.child("ProfileInfo").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    User data = snapshot.getValue(User.class);

                    if (data != null) {
                        usernameView.setText("@" + data.getUsername());
                        userDescription.setText(data.getDescription());

                        String path = data.getProfileImageURL();
                        Uri pathImage = Uri.parse(path);
                        //profileImage.setImageURI();

                        Picasso.get().load(pathImage).into(profilePic);

                        // add learning/known languages
                        data.getProficiency().putAll(data.getLearning());

                        for (String key : data.getProficiency().keySet()) {
                            country_name = myDb.getFlagISO(key);
                            flag = World.getFlagOf(country_name);

                            flagLocations.add(new FlagHelper(flag, data.getProficiency().get(key)));
                        }
                    }

                    Collections.sort(flagLocations, new Comparator<FlagHelper>() {
                        @Override
                        public int compare(FlagHelper lhs, FlagHelper rhs) {
                            return rhs.getLevel() - lhs.getLevel();
                        }
                    });

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            myDb.close();
            return flagLocations.size();

        } catch (Error e) {
            e.printStackTrace();
        }

        return 0;
    }

    @Override
    protected void onPostExecute(Integer integer) {
        super.onPostExecute(integer);
        Log.i(TAG, "onPostExecute: length: " + integer);
        //profileName.setText("@" + sth.getUsername());
        // profileDescription.setText(sth.getDescription());
    }
}
