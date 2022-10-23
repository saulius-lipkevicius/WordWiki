package com.example.wordwiki.ui_main.profile.adapters;

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
import com.example.wordwiki.ui_main.profile.models.ProgressHelper;
import com.example.wordwiki.ui_main.profile.models.ProgressSectionHelper;
import com.google.android.material.progressindicator.LinearProgressIndicator;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class ProgressSectionAdapter extends RecyclerView.Adapter<ProgressSectionAdapter.ViewHolder> {
    Context context;
    ArrayList<ProgressSectionHelper> progressList;

    public ProgressSectionAdapter(ArrayList<ProgressSectionHelper> progressList,  Context context) {
        this.progressList = progressList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_profile_fullscreen_dialog_item, parent, false);
        return new ProgressSectionAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ProgressSectionHelper progressItem = progressList.get(position);

        holder.sectionName.setText(progressItem.getSectionName());

        double progressEstimate = (double) progressItem.getLearnedCount() / progressItem.getAllCount() * 100;
        int progressEstimateInt = (int) progressEstimate;
        Log.i(TAG, "onBindViewHolder: progress parameter is : " + progressEstimate + " : " + progressEstimateInt);
        holder.progressIndicator.setProgress(progressEstimateInt);
        holder.progressIndicator.setScaleY(5f);

        holder.sectionCountIndicator.setText(progressItem.getLearnedCount() + " / " + progressItem.getAllCount());
        /*
        AsyncTaskClassProfileProgress profileProgressTask = new AsyncTaskClassProfileProgress(holder.image, context);
        Log.i(TAG, "onBindViewHolder: " + progressItem.getTitle() + " : " + progressItem.getWordCounter());
        profileProgressTask.execute(progressItem);

        holder.title.setText(progressItem.getTitle());
        holder.wordCounter.setText(progressItem.getWordCounter());
         */
    }

    @Override
    public int getItemCount() {
        return progressList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView sectionName;
        TextView sectionCountIndicator;
        LinearProgressIndicator progressIndicator;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            sectionName = itemView.findViewById(R.id.profile_follow_section_name);
            sectionCountIndicator = itemView.findViewById(R.id.profile_follow_section_counter);
            progressIndicator = itemView.findViewById(R.id.profile_follow_section_progressbar);
        }
    }
}
