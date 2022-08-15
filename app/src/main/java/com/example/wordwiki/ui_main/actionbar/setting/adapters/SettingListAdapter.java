package com.example.wordwiki.ui_main.actionbar.setting.adapters;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wordwiki.R;
import com.example.wordwiki.ui_main.actionbar.setting.models.SettingListModel;

import java.util.List;

public class SettingListAdapter extends RecyclerView.Adapter {
    Context c;
    List<SettingListModel> mlist;
    int size;
    private final OnSettingListener onSettingListener;

    public SettingListAdapter(Context c, List<SettingListModel> list, int size, OnSettingListener onSettingListener) {
        this.c = c;
        this.mlist = list;
        this.size = size;
        this.onSettingListener = onSettingListener;
    }

    // Create diff. viewTypes
    @Override
    public int getItemViewType(int position) {
        if (mlist.get(position).getSettingsName().equals("")) {
            return 1;
        } else if (mlist.get(position).getSettingsName().equals("Give Feedback")
                || mlist.get(position).getSettingsName().equals("Request Feature")
                || mlist.get(position).getSettingsName().equals("Help & Support")) {
            return 2;
        } else if (mlist.get(position).getSettingsName().equals("Logout")) {
            return 3;
        } else {
            return 0;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view;

        if (viewType == 0 || viewType == 2) {
            view = layoutInflater.inflate(R.layout.fragment_setting_item, parent, false);
            return new SettingListAdapter.ViewHolderOne(view, onSettingListener);
        } else if (viewType == 3) {
            view = layoutInflater.inflate(R.layout.fragment_settings_item_red, parent, false);
            return new SettingListAdapter.ViewHolderThree(view, onSettingListener);
        }
        view = layoutInflater.inflate(R.layout.fragment_settings_item_lines, parent, false);
        return new SettingListAdapter.ViewHolderTwo(view);
        /*
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String settingName = holder.friendName.toString();

                if (settingName.equals("About Us")) {
                    Navigation.createNavigateOnClickListener(R.id.action_navigation_setting_to_navigation_about);
                }
            }
        });

         */
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        SettingListModel model = mlist.get(position);
        if (mlist.get(position).getSettingsName().equals("")) {
            SettingListAdapter.ViewHolderTwo viewHolderTwo = (SettingListAdapter.ViewHolderTwo) holder;
        } else if (mlist.get(position).getSettingsName().equals("Give Feedback")
                || mlist.get(position).getSettingsName().equals("Request Feature")
                || mlist.get(position).getSettingsName().equals("Help & Support")) {
            SettingListAdapter.ViewHolderOne viewHolderOne = (SettingListAdapter.ViewHolderOne) holder;

            viewHolderOne.settingsIcon.setImageResource(model.getSettingIcon());
            viewHolderOne.settingsName.setText(model.getSettingsName());
            viewHolderOne.forwardIcon.setVisibility(View.INVISIBLE);
        } else if (mlist.get(position).getSettingsName().equals("Logout")) {
            SettingListAdapter.ViewHolderThree viewHolderThree = (SettingListAdapter.ViewHolderThree) holder;

            viewHolderThree.forwardIcon.setVisibility(View.INVISIBLE);
            viewHolderThree.settingsName.setText(model.getSettingsName());
        } else {
            SettingListAdapter.ViewHolderOne viewHolderOne = (SettingListAdapter.ViewHolderOne) holder;

            viewHolderOne.settingsIcon.setImageResource(model.getSettingIcon());
            viewHolderOne.settingsName.setText(model.getSettingsName());
        }
    }

    @Override
    public int getItemCount() {
        return size;
    }

    class ViewHolderOne extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView settingsIcon, forwardIcon;
        TextView settingsName;
        SettingListAdapter.OnSettingListener onSettingListener;

        public ViewHolderOne(@NonNull View itemView, OnSettingListener onSettingListener) {
            super(itemView);

            forwardIcon = itemView.findViewById(R.id.settings_icon_forward);
            settingsIcon = itemView.findViewById(R.id.settings_icon);
            settingsName = itemView.findViewById(R.id.fragment_setting_name);

            this.onSettingListener = onSettingListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Log.i(TAG, "onClick: testas " + getAdapterPosition());
            onSettingListener.onSettingClick(getAdapterPosition());
        }
    }

    class ViewHolderTwo extends RecyclerView.ViewHolder{
        SettingListAdapter.OnSettingListener onSettingListener;
        public ViewHolderTwo(@NonNull View itemView) {
            super(itemView);
        }
    }

    public interface OnSettingListener {
        void onSettingClick(int position);
    }

    class ViewHolderThree extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView settingsIcon, forwardIcon;
        TextView settingsName;
        SettingListAdapter.OnSettingListener onSettingListener;

        public ViewHolderThree(@NonNull View itemView, OnSettingListener onSettingListener) {
            super(itemView);

            forwardIcon = itemView.findViewById(R.id.settings_icon_forward);
            settingsName = itemView.findViewById(R.id.fragment_setting_name);

            this.onSettingListener = onSettingListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onSettingListener.onSettingClick(getAdapterPosition());
        }
    }
}




