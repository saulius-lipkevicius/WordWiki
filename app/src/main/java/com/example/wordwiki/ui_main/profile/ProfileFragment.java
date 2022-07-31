package com.example.wordwiki.ui_main.profile;

import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;

import static com.facebook.FacebookSdk.getApplicationContext;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blongho.country_data.World;
import com.example.wordwiki.MainActivity;
import com.example.wordwiki.R;
import com.example.wordwiki.database.DatabaseHelper;
import com.example.wordwiki.ui_main.actionbar.notification.NotificationFragment;
import com.example.wordwiki.ui_main.actionbar.setting.SettingFragment;
import com.example.wordwiki.ui_main.profile.adapters.progressAdapter;
import com.example.wordwiki.ui_main.profile.models.progressHelper;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.List;

public class ProfileFragment extends Fragment {
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;
    public ImageButton profileImage;
    public Uri imageUri;
    RecyclerView progressRecycler;
    ArrayList<progressHelper> progressLocations;
    DatabaseHelper myDb;
    RecyclerView.Adapter adapter;
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

        //Toolbar tb = root.findViewById(R.id.toolbar);
        //((AppCompatActivity)getActivity()).setSupportActionBar(tb);
        setUpActionBarLinks(root);

        // cloud storage
        profileImage = root.findViewById(R.id.profile_image);

        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();

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

    private void progressRecycler() {

        //All Gradients
        GradientDrawable gradient2 = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, new int[]{0xf7f5f5f5, 0xf7f5f5f5});
        GradientDrawable gradient1 = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, new int[]{0xf7f5f5f5, 0xf7f5f5f5});


        progressRecycler.setHasFixedSize(true);
        progressRecycler.setLayoutManager(new GridLayoutManager(getContext(), 2));

        progressLocations = new ArrayList<>();

        Cursor progress_languages = myDb.getAllLanguages();

        if (progress_languages == null)
            return; // can't do anything with a null cursor.
        try {
            String language_name = myDb.getFlagLanguage(progress_languages.getString(0));
            String country_name = myDb.getFlagISO(progress_languages.getString(0));
            int flag = World.getFlagOf(country_name);

            // TODO find how many words are newly revised in that language
            String wordsPerLanguage =Integer.toString(myDb.countWordsPerLanguage(language_name));
            progressLocations.add(new progressHelper(flag, language_name, wordsPerLanguage));
            progressLocations.add(new progressHelper(flag, "very long languagenameesase", wordsPerLanguage));
            progressLocations.add(new progressHelper(flag, language_name, wordsPerLanguage));
            progressLocations.add(new progressHelper(flag, language_name, wordsPerLanguage));
            progressLocations.add(new progressHelper(flag, language_name, wordsPerLanguage));


            while (progress_languages.moveToNext()) {
                language_name = myDb.getFlagLanguage(progress_languages.getString(0));
                country_name = myDb.getFlagISO(progress_languages.getString(0));
                flag = World.getFlagOf(country_name);

                wordsPerLanguage =Integer.toString(myDb.countWordsPerLanguage(language_name));
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

        if (requestCode==1 && resultCode==RESULT_OK && data!=null && data.getData()!=null){
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
        FirebaseUser user = ((MainActivity)getActivity()).getCurrentUser();
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