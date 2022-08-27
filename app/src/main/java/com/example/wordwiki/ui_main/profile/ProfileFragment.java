package com.example.wordwiki.ui_main.profile;

import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;

import static com.facebook.FacebookSdk.getApplicationContext;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blongho.country_data.World;
import com.bumptech.glide.Glide;
import com.example.wordwiki.MainActivity;
import com.example.wordwiki.R;
import com.example.wordwiki.database.DatabaseHelper;
import com.example.wordwiki.databinding.FragmentProfileBinding;
import com.example.wordwiki.databinding.FragmentSettingBinding;
import com.example.wordwiki.ui_intro.account.User;
import com.example.wordwiki.ui_main.actionbar.setting.SettingFragment;
import com.example.wordwiki.ui_main.actionbar.setting.sub_settings.dialogs.HelpFragmentDialog;
import com.example.wordwiki.ui_main.profile.adapters.flagAdapter;
import com.example.wordwiki.ui_main.profile.adapters.progressAdapter;
import com.example.wordwiki.ui_main.profile.classes.AsyncTaskClassUploadProfile;
import com.example.wordwiki.ui_main.profile.models.flagHelper;
import com.example.wordwiki.ui_main.profile.models.progressHelper;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
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

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileFragment extends Fragment {
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;
    public CircleImageView profileImage;
    public Uri imageUri;
    RecyclerView flagRecycler;


    DatabaseHelper myDb;
    RecyclerView.Adapter adapter;
    String username;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    TextView profileName, profileDescription;

    // profile describing task
    ArrayList<flagHelper> flagLocations = new ArrayList<>();
    LinearLayoutManager layoutManagerHorizontal = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);


    // newly written recyclerView
    // variables
    String country_name;
    int flag;
    // recycler and adapter + layoutManager
    RecyclerView progressRecycler;
    LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);

    // list of the progress
    ArrayList<progressHelper> progressList = new ArrayList<>();

    FragmentProfileBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        World.init(getApplicationContext());
        myDb = new DatabaseHelper(getContext());

        // cloud storage
        profileImage = root.findViewById(R.id.profile_image);

        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();

        profileName = root.findViewById(R.id.settings_profile_name);
        profileDescription = root.findViewById(R.id.settings_profile_description);
        flagRecycler = root.findViewById(R.id.profile_flag_recycler);

        setUpFlagRecycler();


        //Toolbar tb = root.findViewById(R.id.toolbar);
        //((AppCompatActivity)getActivity()).setSupportActionBar(tb);
        setUpActionBarLinks(root);


        //setUserProfile();

        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseImage();
            }
        });


        progressRecycler = root.findViewById(R.id.profile_fragment_language_recycleview);
        progressRecycler();


        return root;
    }

    private void setUpFlagRecycler() {
        SharedPreferences sharedPreferences1 = getActivity().getSharedPreferences("user_profile", Context.MODE_PRIVATE);
        Boolean userDataFilled = sharedPreferences1.getBoolean("isFilled", false);

        Log.i(TAG, "setUpFlagRecycler: ");

        if (!userDataFilled) {
            ((MainActivity) getActivity()).checkUserProfileData();

            AsyncTaskClassUploadProfile profileInfoTask = new AsyncTaskClassUploadProfile(
                    profileImage, profileName, profileDescription, flagLocations, getContext());
            profileInfoTask.execute(mAuth.getUid());
        } else {
            SharedPreferences sharedPreferences = getActivity().getSharedPreferences("user_profile", Context.MODE_PRIVATE);
            String username = sharedPreferences.getString("username", null);

            String imagePath = sharedPreferences1.getString("imagePath", "");
            String userDescription = sharedPreferences1.getString("userDescription", "There is no description");
            Uri profileImageUri = Uri.parse(imagePath);

            Glide.with(getContext())
                    .load(profileImageUri)
                    .into(profileImage);

            profileName.setText(username);
            profileDescription.setText(userDescription);

            SharedPreferences spLanguage = getActivity().getSharedPreferences("user_profile_language", Context.MODE_PRIVATE);
            SharedPreferences spLevel = getActivity().getSharedPreferences("user_profile_language_level", Context.MODE_PRIVATE);
            for (String keyValue : spLanguage.getAll().keySet()) {
                flagLocations.add(new flagHelper(spLanguage.getInt(keyValue, 0), spLevel.getInt(keyValue, 0)));
            }
        }


        adapter = new flagAdapter(flagLocations);

        flagRecycler.setLayoutManager(layoutManagerHorizontal);
        flagRecycler.setAdapter(adapter);


    }

/*
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

 */


    private void progressRecycler() {
        /*
        Cursor progress_languages = myDb.getAllLanguages();

        if (progress_languages == null)
            return; // can't do anything with a null cursor.
        try {
            String language_name = myDb.getFlagLanguage(progress_languages.getString(0));
            String wordsPerLanguage;
            if (myDb.countTotalWordsPerLanguage(language_name) > 0) {
                String country_name = myDb.getFlagISO(progress_languages.getString(0));
                int flag = World.getFlagOf(country_name);
                wordsPerLanguage = Integer.toString(myDb.countTotalWordsPerLanguage(language_name));
                progressList.add(new progressHelper(flag, language_name, wordsPerLanguage + " words in " + "N" + " days"));
            }

            while (progress_languages.moveToNext()) {
                language_name = myDb.getFlagLanguage(progress_languages.getString(0));
                if (myDb.countTotalWordsPerLanguage(language_name) > 0) {
                    country_name = myDb.getFlagISO(progress_languages.getString(0));
                    flag = World.getFlagOf(country_name);
                    wordsPerLanguage = Integer.toString(myDb.countTotalWordsPerLanguage(language_name));
                    progressList.add(new progressHelper(flag, language_name, wordsPerLanguage + " words in " + "N" + " days"));
                }
            }
        } finally {
            progress_languages.close();
        }

         */
        progressList.add(new progressHelper("English", " words in " + "N" + " days"));
        progressList.add(new progressHelper("German", " words in " + "N" + " days"));
        progressList.add(new progressHelper("Estonian", " words in " + "N" + " days"));
        progressList.add(new progressHelper("French", " words in " + "N" + " days"));
        progressList.add(new progressHelper("Latvian", " words in " + "N" + " days"));
        progressList.add(new progressHelper("Lithuanian", " words in " + "N" + " days"));
        adapter = new progressAdapter(progressList, this::onProgressListClick, getContext());
        progressRecycler.setLayoutManager(layoutManager);
        progressRecycler.setAdapter(adapter);
    }


    public void onProgressListClick(int clickedItemIndex) {
        Log.i(TAG, "onProgressListClick: " + progressList.get(clickedItemIndex).getTitle());
        FragmentManager fm = ProfileFragment.this.getParentFragmentManager();
        DialogFragment dialogFragment = FullScreenDialog.newInstance();


        Bundle bundle = new Bundle();
        bundle.putString("title", progressList.get(clickedItemIndex).getTitle());

        country_name = myDb.getFlagISO(progressList.get(clickedItemIndex).getTitle());
        flag = World.getFlagOf(country_name);

        bundle.putInt("flag", flag);
        dialogFragment.setArguments(bundle);
        changeStatusBarColor(false); // false means transparent - white statusbar


        // create a request to later on send some results back

        dialogFragment.setTargetFragment(this, 5);
        dialogFragment.show(fm, "TAG");



        // TODO implement the callback
        /*
        ((FullScreenDialog) dialogFragment).setCallback(new FullScreenDialog.Callback() {
            @Override
            public void onActionClick(String name) {
                // here comes the callback
            }
        });

         */


        //TODO pop up dialogFragment here
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

        if (requestCode == 5) {
            Log.i(TAG, "onActivityResult: testing request code 5");
            Boolean editTextString = data.getBooleanExtra("isDismissed", false);
            Boolean createSnack = data.getBooleanExtra("isSnack", false);
            if (editTextString) {
                changeStatusBarColor(true);
            }

            Log.i(TAG, "onActivityResult: testing request code 5; second if");
            if (createSnack) {
                Snackbar snackbar = Snackbar.make(binding.getRoot().getRootView(), "It is successfully sent. Thanks for the input.", Snackbar.LENGTH_SHORT);
                BottomNavigationView bottomNavigationView = getActivity().findViewById(R.id.nav_view);
                snackbar.setAnchorView(bottomNavigationView);
                snackbar.show();
            }
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

    public void changeStatusBarColor(Boolean isDismissed) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getActivity().getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            if (isDismissed) {
                window.setStatusBarColor(Color.BLACK);
            } else {
                window.setStatusBarColor(Color.TRANSPARENT);
            }

        }
    }

}