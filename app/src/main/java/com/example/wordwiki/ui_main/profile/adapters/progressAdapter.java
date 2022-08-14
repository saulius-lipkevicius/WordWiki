package com.example.wordwiki.ui_main.profile.adapters;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wordwiki.R;
import com.example.wordwiki.ui_main.profile.models.progressHelper;

import java.util.ArrayList;

public class progressAdapter extends RecyclerView.Adapter<progressAdapter.PhoneViewHold>  {

    ArrayList<progressHelper> progressLocations;
    final private ListItemClickListener mOnClickListener;

    public progressAdapter(ArrayList<progressHelper> phoneLaocations, ListItemClickListener listener) {
        this.progressLocations = phoneLaocations;
        mOnClickListener = listener;
    }

    @NonNull
    @Override
    public PhoneViewHold onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_profile_progress_item, parent, false);
        return new PhoneViewHold(view);

    }

    @Override
    public void onBindViewHolder(@NonNull PhoneViewHold holder, int position) {
        progressHelper progressHelper = progressLocations.get(position);
        holder.image.setImageResource(progressHelper.getImage());
        holder.title.setText(progressHelper.getTitle());
        holder.wordCounter.setText(progressHelper.getWordCounter());
    }

    @Override
    public int getItemCount() {
        return progressLocations.size();
    }

    public interface ListItemClickListener {
        void onProgressListClick(int clickedItemIndex);
    }

    public class PhoneViewHold extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView image;
        TextView title;
        TextView wordCounter;

        public PhoneViewHold(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            //hooks

            image = itemView.findViewById(R.id.phone_image);
            title = itemView.findViewById(R.id.phone_title);
            wordCounter = itemView.findViewById(R.id.number_of_words);
        }

        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            mOnClickListener.onProgressListClick(clickedPosition);
        }
    }

}