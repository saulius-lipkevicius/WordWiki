package com.example.wordwiki.ui_main.profile;

import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;

import static com.facebook.FacebookSdk.getApplicationContext;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blongho.country_data.World;
import com.example.wordwiki.MainActivity;
import com.example.wordwiki.R;
import com.example.wordwiki.database.DatabaseHelper;
import com.example.wordwiki.ui_intro.account.User;
import com.example.wordwiki.ui_main.profile.adapters.flagAdapter;
import com.example.wordwiki.ui_main.profile.adapters.progressAdapter;
import com.example.wordwiki.ui_main.profile.models.flagHelper;
import com.example.wordwiki.ui_main.profile.models.progressHelper;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileFragment extends Fragment {
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;
    public CircleImageView profileImage;
    public Uri imageUri;
    RecyclerView progressRecycler, flagRecycler;
    ArrayList<progressHelper> progressLocations;
    ArrayList<flagHelper> flagLocations;
    DatabaseHelper myDb;
    RecyclerView.Adapter adapter;
    String username;

    TextView profileName, profileDescription;

    String country_name;
    int flag;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_profile, container, false);

        World.init(getApplicationContext());
        myDb = new DatabaseHelper(getContext());
        flagLocations = new ArrayList<>();

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("general", Context.MODE_PRIVATE);
        username = sharedPreferences.getString("username", "");

        //Toolbar tb = root.findViewById(R.id.toolbar);
        //((AppCompatActivity)getActivity()).setSupportActionBar(tb);
        setUpActionBarLinks(root);

        // cloud storage
        profileImage = root.findViewById(R.id.profile_image);

        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();


        profileName = root.findViewById(R.id.settings_profile_name);
        profileDescription = root.findViewById(R.id.settings_profile_description);


        setUserProfile();

        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseImage();
            }
        });


        progressRecycler = root.findViewById(R.id.profile_fragment_language_recycleview);
        progressRecycler();

        flagRecycler = root.findViewById(R.id.profile_flag_recycler);
        setUpFlagRecycler();

        return root;
    }

    private void setUpFlagRecycler() {
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false);
        //flagRecycler.setHasFixedSize(true);
        flagRecycler.setLayoutManager(layoutManager);

        Collections.sort(flagLocations, new Comparator<flagHelper>() {
            @Override
            public int compare(flagHelper lhs, flagHelper rhs) {
                return rhs.getLevel() - lhs.getLevel();
            }
        });


        adapter = new flagAdapter(flagLocations);
        flagRecycler.setAdapter(adapter);
    }


    private void setUserProfile() {
        DatabaseReference referenceProfile = FirebaseDatabase.getInstance("https://wordwiki-af0d4-default-rtdb.europe-west1.firebasedatabase.app").getReference("Users");
        referenceProfile.child(username).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User sth = snapshot.getValue(User.class);

                if (sth != null) {
                    Log.i(TAG, "onDataChange: " + sth.getUsername());
                    profileName.setText("@" + sth.getUsername());
                    profileDescription.setText(sth.getDescription());
                    String path = sth.getProfile();
                    Uri pathImage = Uri.parse(path);
                    //profileImage.setImageURI();

                    Picasso.get().load(pathImage).into(profileImage);

                    // add learning/known languages
                    sth.getProficiency().putAll(sth.getLearning());

                    for (String key : sth.getProficiency().keySet()) {
                        Log.i(TAG, "onDataChange: hi: " + key);
                        // get flags for sep. languages
                        country_name = myDb.getFlagISO(key);
                        flag = World.getFlagOf(country_name);

                        flagLocations.add(new flagHelper(flag, sth.getProficiency().get(key)));
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private void progressRecycler() {
        progressRecycler.setHasFixedSize(true);
        progressRecycler.setLayoutManager(new GridLayoutManager(getContext(), 2));

        progressLocations = new ArrayList<>();

        Cursor progress_languages = myDb.getAllLanguages();

        if (progress_languages == null)
            return; // can't do anything with a null cursor.
        try {
            String language_name;
            String country_name;
            int flag;
            String wordsPerLanguage;


            while (progress_languages.moveToNext()) {
                language_name = myDb.getFlagLanguage(progress_languages.getString(0));
                country_name = myDb.getFlagISO(progress_languages.getString(0));
                flag = World.getFlagOf(country_name);

                wordsPerLanguage = Integer.toString(myDb.countTotalWordsPerLanguage(language_name));
                // wordsPerLanguage = Integer.toString(myDb.countWordsPerLanguage(language_name));
                progressLocations.add(new progressHelper(flag, language_name, wordsPerLanguage));
            }
        } finally {
            progress_languages.close();
        }

        adapter = new progressAdapter(progressLocations, this::onProgressListClick);
        progressRecycler.setAdapter(adapter);
    }

    public void onProgressListClick(int clickedItemIndex) {
        Log.i(TAG, "onProgressListClick: " + progressLocations.get(clickedItemIndex).getTitle());

        /*

        switch (clickedItemIndex) {
            case 0: //first item in Recycler view
                mIntent = new Intent(MainActivity.this, com.example.kazkas.samsung.class);
                startActivity(mIntent);
                break;
            case 1: //second item in Recycler view
                mIntent = new Intent(MainActivity.this, com.example.kazkas.apple.class);
                startActivity(mIntent);
                break;
        }

         */
    }

    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);

        startActivityForResult(intent, 1);
    }

    private void setUpActionBarLinks(View root) {
        // moves of the actionbar in the mainActivity
        ImageButton toSetting = root.findViewById(R.id.main_actionbar_settings);
        toSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavController navController = Navigation.findNavController(view);
                navController.navigate(R.id.action_navigation_profile_to_navigation_setting);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            profileImage.setImageURI(imageUri);

            uploadImage();
        }
    }

    private void uploadImage() {

        final ProgressDialog pd = new ProgressDialog(getContext());
        pd.setTitle("Uploading Image...");
        pd.show();

        LinearLayout rootlayout = (LinearLayout) getView().findViewById(R.id.linear_layout);
        //final String randomKey = UUID.randomUUID().toString();
        FirebaseUser user = ((MainActivity) getActivity()).getCurrentUser();
        StorageReference profileImageRef = storageReference.child("user_profile/" + user + "/profile_image");

        profileImageRef.putFile(imageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Snackbar.make(rootlayout, "Image Uploaded.", Snackbar.LENGTH_LONG).show();
                        pd.dismiss();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), "Upload Failed", Toast.LENGTH_SHORT).show();
                        pd.dismiss();
                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                        double progressPercent = (100.00 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                        pd.setMessage("Progress: " + (int) progressPercent + "%");
                    }
                });
    }
}