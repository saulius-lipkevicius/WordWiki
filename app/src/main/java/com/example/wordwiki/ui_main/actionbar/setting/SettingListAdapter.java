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
    List<SettingListModel> list;
    int size;


    public SettingListAdapter(Context c, List<SettingListModel> list, int size) {
        this.c = c;
        this.list = list;
        this.size = size;
    }

    @NonNull
    @Override
    public MyAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_setting_item, parent, false);
        return new MyAdapter(rootView);
    }


    @Override
    public void onBindViewHolder(@NonNull MyAdapter holder, int position) {
        SettingListModel model = list.get(position);
        holder.friendName.setText(model.getFriendName());
    }

    @Override
    public int getItemCount() {
        return size;
    }


    public class MyAdapter extends RecyclerView.ViewHolder {
        TextView friendName;

        public MyAdapter(@NonNull View itemView) {
            super(itemView);
            friendName = itemView.findViewById(R.id.fragment_setting_name);
        }
    }
}




