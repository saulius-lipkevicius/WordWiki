package com.example.wordwiki.ui_intro.account.fragments;

import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;
import static android.content.Context.MODE_PRIVATE;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;

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

import com.example.wordwiki.R;
import com.example.wordwiki.databinding.FragmentCreatePictureBinding;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class CreatePictureFragment extends Fragment {
    FragmentCreatePictureBinding binding;
    private static final int SELECT_PICTURE = 1;
    ImageView profilePic;

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
                    Uri imageUri = data.getData();
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(requireActivity().getContentResolver(), imageUri);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                } else {
                    Bundle extras = data.getExtras();
                    bitmap = (Bitmap) extras.get("data");
                    profilePic.setImageBitmap(bitmap);
                }
                profilePic.setImageBitmap(bitmap);

            }
        }
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


/*
        public void uploadProfileImage(){
            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageRef = storage.getReference();
            // Create a reference to the file you want to upload
            String directory = "images/";

            StorageReference fileRef = storageRef.child(directory + serverFileName);

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] data = stream.toByteArray();

            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {


                    UploadTask uploadTask = fileRef.putBytes(data);
                    uploadTask.addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            // Handle unsuccessful uploads
                            Log.e("oops","error in bitmap uploading");

                        }
                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                            // ...
                            // now download url first
                        }
                    });

                    Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                        @Override
                        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                            if (!task.isSuccessful()) {
                                throw task.getException();
                            }

                            // Continue with the task to get the download URL
                            return fileRef.getDownloadUrl();
                        }
                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if (task.isSuccessful()) {
                                Uri downloadUri = task.getResult();
                                String stringUrl = downloadUri.toString();

                                // Great image is uploaded and url is here, you can load image anywhere via this stringUrl with Glide or Picasso.

                            } else {
                                // Handle unsuccessful uploads
                                Log.e("oops","error in url retrieval");
                            }
                        }
                    });


                }
            });
        }

 */
    }