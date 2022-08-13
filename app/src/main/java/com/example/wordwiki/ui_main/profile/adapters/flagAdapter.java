package com.example.wordwiki.ui_main.profile.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wordwiki.R;
import com.example.wordwiki.ui_main.profile.models.flagHelper;
import com.example.wordwiki.ui_main.profile.models.progressHelper;

import java.util.ArrayList;

public class flagAdapter extends RecyclerView.Adapter<flagAdapter.ViewHolder> {

    ArrayList<flagHelper> flagLocations;

    public flagAdapter(ArrayList<flagHelper> flagLocations) {
        this.flagLocations = flagLocations;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.profile_fragment_flag_item, parent, false);
        return new flagAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        flagHelper progressHelper = flagLocations.get(position);
        holder.flag.setImageResource(progressHelper.getFlag());
    }

    @Override
    public int getItemCount() {
        return flagLocations.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView flag;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            flag = itemView.findViewById(R.id.fragment_profile_flag_item_flag);
        }
    }
}
