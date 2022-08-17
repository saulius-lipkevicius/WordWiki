package com.example.wordwiki.ui_main.actionbar.setting.sub_settings;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

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
import com.example.wordwiki.databinding.FragmentMyAccountBinding;
import com.example.wordwiki.databinding.FragmentPreferenceBinding;
import com.example.wordwiki.ui_main.actionbar.setting.SettingFragment;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.firebase.database.FirebaseDatabase;

public class PreferenceFragment extends Fragment {
    FragmentPreferenceBinding binding;
    ImageButton toSettings;
    TextView toolbarTitle;
    String username;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentPreferenceBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // get the username for firebase pushes
        username = ((MainActivity)getActivity()).getUsername();

        setButtons();

        onClickPreference();

        return root;
    }

    public void setButtons() {
        toolbarTitle = binding.getRoot().findViewById(R.id.toolbar_title);
        toolbarTitle.setText("Preference");

        toSettings = binding.getRoot().findViewById(R.id.toolbar_back_btn);

        NavHostFragment navHostFragment = (NavHostFragment) getActivity().getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment_activity_main);
        NavController navCo = navHostFragment.getNavController();

        toSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navCo.navigate(R.id.action_navigation_preferences_to_navigation_setting);
            }
        });
    }

    public void onClickPreference(){
        SwitchMaterial isNightMode = binding.getRoot().findViewById(R.id.preference_preference_night_mode);
        TextView fontSize = binding.getRoot().findViewById(R.id.preference_preference_font_size);
        SwitchMaterial isSoundEffect = binding.getRoot().findViewById(R.id.preference_preference_sound_effect);

        isNightMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("preferences", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();

                editor.putBoolean("isNightMode", isNightMode.isChecked());
                editor.apply();

                FirebaseDatabase.getInstance("https://wordwiki-af0d4-default-rtdb.europe-west1.firebasedatabase.app/").getReference()
                        .child("Users").child(username).child("preference")
                        .child("nightMode").setValue(isNightMode.isChecked());
            }
        });

        // TODO get the font scaler from the last project
        /*
        FirebaseDatabase.getInstance("https://wordwiki-af0d4-default-rtdb.europe-west1.firebasedatabase.app/").getReference()
                        .child("Users").child(username).child("preference")
                        .child("fontSize").setValue(fontSize.getText()
                        );
         */

        isSoundEffect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("preferences", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();

                editor.putBoolean("isSound", isNightMode.isChecked());
                editor.apply();

                FirebaseDatabase.getInstance("https://wordwiki-af0d4-default-rtdb.europe-west1.firebasedatabase.app/").getReference()
                        .child("Users").child(username).child("preference")
                        .child("sound").setValue(isNightMode.isChecked());
            }
        });
    }
}