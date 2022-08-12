package com.example.wordwiki.ui_intro.account.fragments;

import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;
import static android.content.Context.MODE_PRIVATE;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.wordwiki.MainActivity;
import com.example.wordwiki.R;
import com.example.wordwiki.databinding.FragmentCreatePictureBinding;
import com.example.wordwiki.ui_intro.account.CreateProfileActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class CreatePictureFragment extends Fragment {
    FragmentCreatePictureBinding binding;

    private static final int SELECT_PICTURE = 1;
    ImageView profilePic;
    private StorageReference storageReference;
    public Uri imageUri;

    private FirebaseStorage firebaseStorage;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentCreatePictureBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();

        setButtons();

        profilePic = root.findViewById(R.id.profile_pic);

        Intent pickIntent = new Intent();
        pickIntent.setType("image/*");
        pickIntent.setAction(Intent.ACTION_GET_CONTENT);

        Intent takePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        String pickTitle = "Select or take a new Picture"; // Or get from strings.xml
        Intent chooserIntent = Intent.createChooser(pickIntent, pickTitle);
        chooserIntent.putExtra
                (
                        Intent.EXTRA_INITIAL_INTENTS,
                        new Intent[]{takePhotoIntent}
                );

        startActivityForResult(chooserIntent, SELECT_PICTURE);


        return root;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            Bitmap bitmap = null;
            Bitmap bmp = null;
            if (requestCode == SELECT_PICTURE) {
                if (data.getData() != null) {
                    imageUri = data.getData();
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(requireActivity().getContentResolver(), imageUri);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                } else {
                    Bundle extras = data.getExtras();
                    bitmap = (Bitmap) extras.get("data");
                    profilePic.setImageBitmap(bitmap);
                    imageUri = getImageUri(getContext(), bitmap);
                }
                profilePic.setImageBitmap(bitmap);


                uploadImage();
            }
        }
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }


    private void setButtons () {
            ImageButton backBtn = binding.getRoot().findViewById(R.id.back);
            backBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    NavController navController = Navigation.findNavController(view);
                    navController.navigate(R.id.action_navigation_create_user_picture_to_navigation_create_user_learning_languages);
                }
            });

            Button skipFragment = binding.getRoot().findViewById(R.id.fragment_username_skip_btn);
            skipFragment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // navigating to the mainActivity with navView
                }
            });

            Button nextFragment = binding.getRoot().findViewById(R.id.fragment_username_next_btn);
            nextFragment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //FirebaseDatabase.getInstance("https://wordwiki-af0d4-default-rtdb.europe-west1.firebasedatabase.app/").getReference().child("Users").child("saulius").child("picture").setValue(description);

                    NavController navController = Navigation.findNavController(view);
                    navController.navigate(R.id.action_navigation_create_user_picture_to_navigation_create_user_description);

                }
            });
        }

    private void uploadImage() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("general", MODE_PRIVATE);
        String username = sharedPreferences.getString("username", "");

        final ProgressDialog pd = new ProgressDialog(getContext());
        pd.setTitle("Uploading Image...");
        pd.show();

        LinearLayout rootlayout = (LinearLayout) getView().findViewById(R.id.linear_layout);
        //final String randomKey = UUID.randomUUID().toString();
        //FirebaseUser user = ((CreateProfileActivity) getActivity()).getCurrentUser();
        StorageReference profileImageRef = storageReference.child("user_profile/" + username + "/profile_image");

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