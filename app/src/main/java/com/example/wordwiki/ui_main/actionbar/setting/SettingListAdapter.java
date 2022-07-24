package com.example.wordwiki.ui_main.actionbar.setting;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wordwiki.R;

import java.util.List;

public class SettingListAdapter extends RecyclerView.Adapter<SettingListAdapter.MyAdapter> {
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

    @NonNull
    @Override
    public MyAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_setting_item, parent, false);

        final MyAdapter holder = new MyAdapter(rootView, onSettingListener);
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

        return holder;
    }


    @Override
    public void onBindViewHolder(@NonNull MyAdapter holder, int position) {
        SettingListModel model = mlist.get(position);
        holder.settingsName.setText(model.getSettingsName());
    }

    @Override
    public int getItemCount() {
        return size;
    }

    public class MyAdapter extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView settingsName;
        OnSettingListener onSettingListener;
        public MyAdapter(@NonNull View itemView, OnSettingListener onSettingListener) {
            super(itemView);
            settingsName = itemView.findViewById(R.id.fragment_setting_name);

            this.onSettingListener = onSettingListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onSettingListener.onSettingClick(getAdapterPosition());
        }
    }

    public interface OnSettingListener{
        void onSettingClick(int position);
    }
}




