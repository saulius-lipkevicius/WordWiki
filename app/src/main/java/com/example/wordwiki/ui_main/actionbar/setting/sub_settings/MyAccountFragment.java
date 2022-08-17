package com.example.wordwiki.ui_main.actionbar.setting.sub_settings;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.wordwiki.MainActivity;
import com.example.wordwiki.R;
import com.example.wordwiki.databinding.FragmentLanguagesBinding;
import com.example.wordwiki.databinding.FragmentMyAccountBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class MyAccountFragment extends Fragment {
    FragmentMyAccountBinding binding;
    ImageButton toSettings;
    TextView toolbarTitle;
    String username;
    String authenticationType;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentMyAccountBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // get the username for firebase pushes
        username = ((MainActivity)getActivity()).getUsername();

        setButtons();

        return root;
    }

    public void setButtons() {
        toolbarTitle = binding.getRoot().findViewById(R.id.toolbar_title);
        toolbarTitle.setText("Account");

        toSettings = binding.getRoot().findViewById(R.id.toolbar_back_btn);

        NavHostFragment navHostFragment = (NavHostFragment) getActivity().getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment_activity_main);
        NavController navCo = navHostFragment.getNavController();

        toSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navCo.navigate(R.id.action_navigation_myaccount_to_navigation_setting);
            }
        });
    }

    public void onClickPreference(){
        TextView editProfile = binding.getRoot().findViewById(R.id.fragment_myaccount_edit_profile);
        TextView saveProgress = binding.getRoot().findViewById(R.id.fragment_myaccount_save_progress);
        TextView changePassword = binding.getRoot().findViewById(R.id.fragment_myaccount_change_password);
        TextView deleteAccount = binding.getRoot().findViewById(R.id.fragment_myaccount_delete_account);

        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        saveProgress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                /* TODO create a model to help move a whole table or its finle
                FirebaseDatabase.getInstance("https://wordwiki-af0d4-default-rtdb.europe-west1.firebasedatabase.app/").getReference()
                        .child("Users").child(username).child("preference")
                        .child("isNotification").setValue(isNotification.isChecked());

                 */
            }
        });

        changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                authenticationType = ((MainActivity)getActivity()).getAuthenticationType();
                if (authenticationType.equals("password")){

                    // TODO create a small dialog with old + new password, to confirm the user
                    FirebaseUser user = ((MainActivity)getActivity()).getCurrentUser();
                    String newPassword = "test";
                    user.updatePassword(newPassword)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Log.d(TAG, "User password updated.");
                                    }
                                }
                            });
                }
            }
        });

        deleteAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseDatabase.getInstance("https://wordwiki-af0d4-default-rtdb.europe-west1.firebasedatabase.app/").getReference()
                        .child("Users")
                        .child(username).removeValue();

                // also remove a profile image from the firebase storage
                FirebaseStorage.getInstance().getReference().child("user_profile/" + username).delete();

                // TODO change information of a dictionary created by an user
            }
        });
    }
}