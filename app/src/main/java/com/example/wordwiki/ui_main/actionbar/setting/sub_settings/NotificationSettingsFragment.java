package com.example.wordwiki.ui_main.actionbar.setting.sub_settings;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.example.wordwiki.R;
import com.example.wordwiki.databinding.FragmentNotificationBinding;
import com.example.wordwiki.databinding.FragmentNotificationSettingsBinding;
import com.example.wordwiki.databinding.FragmentSettingBinding;

public class NotificationSettingsFragment extends Fragment {
    FragmentNotificationSettingsBinding binding;

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

        ImageButton toSettings = root.findViewById(R.id.toolbar_back_btn);

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

        return root;
    }
}