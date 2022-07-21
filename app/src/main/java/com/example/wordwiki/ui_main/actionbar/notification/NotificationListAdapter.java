package com.example.wordwiki.ui_main.actionbar.notification;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wordwiki.R;

import java.util.List;

public class NotificationListAdapter extends RecyclerView.Adapter<NotificationListAdapter.MyAdapter> {
    Context c;
    List<NotificationListModel> list;
    int size;


    public NotificationListAdapter(Context c, List<NotificationListModel> list, int size) {
        this.c = c;
        this.list = list;
        this.size = size;
    }

    @NonNull
    @Override
    public MyAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_notification_item, parent, false);
        return new MyAdapter(rootView);
    }


    @Override
    public void onBindViewHolder(@NonNull MyAdapter holder, int position) {
        NotificationListModel model = list.get(position);
        holder.notificationDescription.setText(model.getFriendName());
    }

    @Override
    public int getItemCount() {
        return size;
    }


    public class MyAdapter extends RecyclerView.ViewHolder {
        TextView notificationDescription;

        public MyAdapter(@NonNull View itemView) {
            super(itemView);
            notificationDescription = itemView.findViewById(R.id.fragment_notification_name);
        }
    }
}




