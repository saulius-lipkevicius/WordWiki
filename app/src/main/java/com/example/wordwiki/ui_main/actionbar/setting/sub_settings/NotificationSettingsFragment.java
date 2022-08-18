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
import com.example.wordwiki.databinding.FragmentNotificationBinding;
import com.example.wordwiki.databinding.FragmentNotificationSettingsBinding;
import com.example.wordwiki.databinding.FragmentSettingBinding;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

public class NotificationSettingsFragment extends Fragment {
    FragmentNotificationSettingsBinding binding;
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
        // Inflate the layout for this fragment
        binding = FragmentNotificationSettingsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // get the username for firebase pushes
        username = ((MainActivity)getActivity()).getUsername();

        setButtons();

        onClickNotification();

        return root;
    }

    public void setButtons() {
        toolbarTitle = binding.getRoot().findViewById(R.id.toolbar_title);
        toolbarTitle.setText("Notification");

        toSettings = binding.getRoot().findViewById(R.id.toolbar_back_btn);

        NavHostFragment navHostFragment = (NavHostFragment) getActivity().getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment_activity_main);
        NavController navCo = navHostFragment.getNavController();

        toSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("a", "onClick: aaaa");
                navCo.navigate(R.id.action_navigation_notification_to_navigation_setting);
            }
        });
    }

    public void onClickNotification(){
        SwitchMaterial isNotification = binding.getRoot().findViewById(R.id.preference_notification_notification_switch);
        TextView createRemainder = binding.getRoot().findViewById(R.id.preference_notification_create_remainder);
        TextView snoozeNotification = binding.getRoot().findViewById(R.id.preference_notification_snooze_notification);
        SwitchMaterial isMarketing = binding.getRoot().findViewById(R.id.preference_notification_marketing);

        isNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("preferences", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();

                editor.putBoolean("isNotification", isNotification.isChecked());
                editor.apply();

                FirebaseDatabase.getInstance("https://wordwiki-af0d4-default-rtdb.europe-west1.firebasedatabase.app/").getReference()
                        .child("Users").child(username).child("preference")
                        .child("isNotification").setValue(isNotification.isChecked());
            }
        });

        isMarketing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("preferences", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();

                editor.putBoolean("isMarketing", isNotification.isChecked());
                editor.apply();

                FirebaseDatabase.getInstance("https://wordwiki-af0d4-default-rtdb.europe-west1.firebasedatabase.app/").getReference()
                        .child("Users").child(username).child("preference")
                        .child("isMarketing").setValue(isMarketing.isChecked());
            }
        });

        // TODO create dialogFragments for snooze and create...
    }
}