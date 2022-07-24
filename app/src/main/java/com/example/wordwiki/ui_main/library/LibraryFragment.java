package com.example.wordwiki.ui_main.library;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.wordwiki.R;
import com.example.wordwiki.databinding.FragmentLibraryBinding;
import com.example.wordwiki.ui_main.actionbar.notification.NotificationFragment;
import com.example.wordwiki.ui_main.actionbar.setting.SettingFragment;

public class LibraryFragment extends Fragment {

    private FragmentLibraryBinding binding;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentLibraryBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        Toolbar tb = root.findViewById(R.id.toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(tb);

        //setUpActionBarLinks();

        return root;
    }

    private void setUpActionBarLinks() {
        // moves of the actionbar in the mainActivity
        ImageButton toSetting = getActivity().findViewById(R.id.main_actionbar_settings);
        toSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction fr = getFragmentManager().beginTransaction();
                fr.replace(R.id.nav_host_fragment_activity_main,new SettingFragment());
                fr.commit();
            }
        });

        ImageButton toNotification = getActivity().findViewById(R.id.main_actionbar_notification);
        toNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction fr = getFragmentManager().beginTransaction();
                fr.replace(R.id.nav_host_fragment_activity_main,new NotificationFragment());
                fr.commit();
            }
        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}