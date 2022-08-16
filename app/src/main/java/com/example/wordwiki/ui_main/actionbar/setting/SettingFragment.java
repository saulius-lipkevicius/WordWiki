package com.example.wordwiki.ui_main.actionbar.setting;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wordwiki.MainActivity;
import com.example.wordwiki.R;
import com.example.wordwiki.databinding.FragmentSettingBinding;
import com.example.wordwiki.ui_main.actionbar.setting.adapters.SettingListAdapter;
import com.example.wordwiki.ui_main.actionbar.setting.models.SettingListModel;
import com.example.wordwiki.ui_main.actionbar.setting.sub_settings.dialogs.FeedbackFragmentDialog;
import com.example.wordwiki.ui_main.actionbar.setting.sub_settings.dialogs.HelpFragmentDialog;
import com.example.wordwiki.ui_main.actionbar.setting.sub_settings.dialogs.RequestFeatureFragmentDialog;
import com.example.wordwiki.ui_main.profile.FullScreenDialog;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;


public class SettingFragment extends Fragment implements SettingListAdapter.OnSettingListener {
    List<SettingListModel> settingList = new ArrayList<>();
    SettingListAdapter settingAdapter;
    RecyclerView settingListRecycle;
    private FragmentSettingBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSettingBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        // setup feed of friends in the feed
        addSettingFolders();
        settingAdapter = new SettingListAdapter(getContext(), settingList, settingList.size(), this);
        settingListRecycle = root.findViewById(R.id.fragment_setting_recycle_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        settingListRecycle.setLayoutManager(layoutManager);

        //settingListRecycle.addItemDecoration(new DividerItemDecoration(settingListRecycle.getContext(),
        //        layoutManager.getOrientation()));
        settingListRecycle.setAdapter(settingAdapter);

        Log.i(TAG, "onCreateView: asd " + settingListRecycle.getItemDecorationCount());
        setUpActionBarLinks(root);
        return root;
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Make sure fragment codes match up
        Log.i(TAG, "onActivityResult:::" + requestCode);

        if (requestCode == 0) {
            Boolean editTextString = data.getBooleanExtra("isDismissed", false);
            Boolean createSnack = data.getBooleanExtra("isSnack", false);
            if (editTextString) {
                changeStatusBarColor(true);
            }

            if (createSnack) {
                Snackbar snackbar = Snackbar.make(binding.getRoot().getRootView(), "It is successfully sent. Thanks for the input.", Snackbar.LENGTH_SHORT);
                BottomNavigationView bottomNavigationView = getActivity().findViewById(R.id.nav_view);
                snackbar.setAnchorView(bottomNavigationView);
                snackbar.show();
            }
        }
    }


    private void setUpActionBarLinks(View root) {
        // moves of the actionbar in the mainActivity
        ImageButton getBack = root.findViewById(R.id.setting_back);
        getBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavController navController = Navigation.findNavController(binding.getRoot());
                navController.navigate(R.id.action_navigation_setting_to_navigation_profile);
            }
        });
    }

    private void addSettingFolders() {
        settingList.clear();
        settingList.add(new SettingListModel(R.drawable.ic_feedback, "Give Feedback"));
        settingList.add(new SettingListModel(R.drawable.ic_feedback, "Request Feature"));
        settingList.add(new SettingListModel(R.drawable.ic_language, ""));

        settingList.add(new SettingListModel(R.drawable.ic_account, "My Account"));
        settingList.add(new SettingListModel(R.drawable.ic_notifications, "Notification"));
        settingList.add(new SettingListModel(R.drawable.ic_preference, "Preferences"));
        settingList.add(new SettingListModel(R.drawable.ic_language, "Language"));
        settingList.add(new SettingListModel(R.drawable.ic_language, ""));

        settingList.add(new SettingListModel(R.drawable.ic_about, "Help & Support"));
        settingList.add(new SettingListModel(R.drawable.ic_about, "About Us"));
        settingList.add(new SettingListModel(R.drawable.ic_language, ""));


        settingList.add(new SettingListModel(R.drawable.ic_logout_w200, "Logout"));
    }

    @Override
    public void onSettingClick(int position) {
        String settingsClicked = settingList.get(position).getSettingsName();
        NavController navController = Navigation.findNavController(binding.getRoot());

        // sending some data to change titles IF dialogFragment is called
        Bundle bundle = new Bundle();
        bundle.putString("title", settingsClicked);

        // if it is a navigation case
        // TODO finish it up later


        if (settingsClicked.equals("Give Feedback")) {
            changeStatusBarColor(false);
            // to identify a sender later on
            FragmentManager fm = SettingFragment.this.getParentFragmentManager();
            DialogFragment dialogFragment = FeedbackFragmentDialog.newInstance();
            dialogFragment.setTargetFragment(this, 0);
            dialogFragment.show(fm, "TAG");

        } else if (settingsClicked.equals("Request Feature")) {
            changeStatusBarColor(false);

            FragmentManager fm = SettingFragment.this.getParentFragmentManager();
            DialogFragment dialogFragment = RequestFeatureFragmentDialog.newInstance();
            dialogFragment.setTargetFragment(this, 0);
            dialogFragment.show(fm, "TAG");

        } else if (settingsClicked.equals("My Account")) {
            navController.navigate(R.id.action_navigation_setting_to_navigation_myaccount);

        } else if (settingsClicked.equals("Notification")) {
            navController.navigate(R.id.action_navigation_setting_to_navigation_notification);

        } else if (settingsClicked.equals("Preferences")) {
            navController.navigate(R.id.action_navigation_setting_to_navigation_preferences);

        } else if (settingsClicked.equals("Language")) {
            navController.navigate(R.id.action_navigation_setting_to_navigation_language);

        } else if (settingsClicked.equals("Help & Support")) {
            changeStatusBarColor(false);
            FragmentManager fm = SettingFragment.this.getParentFragmentManager();
            DialogFragment dialogFragment = HelpFragmentDialog.newInstance();
            dialogFragment.setTargetFragment(this, 0);
            dialogFragment.show(fm, "TAG");

        } else if (settingsClicked.equals("About Us")) {
            navController.navigate(R.id.action_navigation_setting_to_navigation_about);

        } else if (settingsClicked.equals("Logout")) {
            ((MainActivity) getActivity()).signOut();

        }
    }

    public void changeStatusBarColor(Boolean isDismissed){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window =  getActivity().getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            if (isDismissed) {
                window.setStatusBarColor(Color.BLACK);
            } else {
                window.setStatusBarColor(Color.TRANSPARENT);
            }

        }
    }
}