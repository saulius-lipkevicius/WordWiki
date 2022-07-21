package com.example.wordwiki.ui_main.style;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.example.wordwiki.R;
import com.example.wordwiki.databinding.FragmentStyleBinding;
import com.example.wordwiki.ui_main.actionbar.notification.NotificationFragment;
import com.example.wordwiki.ui_main.actionbar.setting.SettingFragment;

public class StyleFragment extends Fragment {

    private FragmentStyleBinding binding;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TextView actionBarTitle = getActivity().findViewById(R.id.tvTitle);
        actionBarTitle.setText("Style");
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        StyleViewModel notificationsViewModel =
                new ViewModelProvider(this).get(StyleViewModel.class);

        binding = FragmentStyleBinding.inflate(inflater, container, false);
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