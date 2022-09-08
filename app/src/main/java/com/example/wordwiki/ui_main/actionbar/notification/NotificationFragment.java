package com.example.wordwiki.ui_main.actionbar.notification;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wordwiki.R;
import com.example.wordwiki.databinding.FragmentNotificationBinding;

import java.util.ArrayList;
import java.util.List;


public class NotificationFragment extends Fragment {
    List<NotificationListModel> notificationList = new ArrayList<>();
    NotificationListAdapter notificationAdapter;
    RecyclerView notificationListRecycle;

    private FragmentNotificationBinding binding;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addSettingFolders();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentNotificationBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        // Inflate the layout for this fragment
        //View root =  inflater.inflate(R.layout.fragment_notification, container, false);

        //  title name change
        TextView titleName = root.findViewById(R.id.toolbar_title);
        titleName.setText("Notification");

        // setup feed of friends in the feed
        notificationAdapter = new NotificationListAdapter(getContext(), notificationList, notificationList.size());
        notificationListRecycle = root.findViewById(R.id.fragment_notification_recycle_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        notificationListRecycle.setLayoutManager(layoutManager);
        notificationListRecycle.addItemDecoration(new DividerItemDecoration(notificationListRecycle.getContext(),
                layoutManager.getOrientation()));

        notificationListRecycle.setAdapter(notificationAdapter);

        ImageButton toHome = root.findViewById(R.id.toolbar_back_btn);

        toHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavController navController = Navigation.findNavController(view);
                navController.navigate(R.id.action_navigation_notification_to_navigation_home);
            }
        });

        return root;
    }

    private void addSettingFolders() {
        notificationList.clear();
        notificationList.add(new NotificationListModel("Version 1.0 is launched."));
        notificationList.add(new NotificationListModel("Look at the preferences you can change. Press to see more"));
        notificationList.add(new NotificationListModel("App is ready for multilingual users. We support English, Lithuanian..."));

        notificationList.add(new NotificationListModel("Version 1.0 is launched."));
        notificationList.add(new NotificationListModel("Look at the preferences you can change. Press to see more"));
        notificationList.add(new NotificationListModel("App is ready for multilingual users. We support English, Lithuanian..."));


        notificationList.add(new NotificationListModel("Version 1.0 is launched."));
        notificationList.add(new NotificationListModel("Look at the preferences you can change. Press to see more"));
        notificationList.add(new NotificationListModel("App is ready for multilingual users. We support English, Lithuanian..."));

        notificationList.add(new NotificationListModel("Version 1.0 is launched."));
        notificationList.add(new NotificationListModel("Look at the preferences you can change. Press to see more"));
        notificationList.add(new NotificationListModel("App is ready for multilingual users. We support English, Lithuanian..."));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}