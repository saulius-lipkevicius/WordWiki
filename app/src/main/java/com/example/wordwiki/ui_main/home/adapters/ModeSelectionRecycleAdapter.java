package com.example.wordwiki.ui_main.home.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wordwiki.R;
import com.example.wordwiki.ui_main.home.models.ModeSelectionModel;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ModeSelectionRecycleAdapter extends RecyclerView.Adapter<ModeSelectionRecycleAdapter.MyAdapter> {
    Context c;
    List<ModeSelectionModel> list;
    int size;

    public ModeSelectionRecycleAdapter(Context c, List<ModeSelectionModel> list, int size) {
        this.c = c;
        this.list = list;
        this.size = size;
    }

    @NonNull
    @Override
    public MyAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_model_selection_item, parent,false);
        return new MyAdapter(rootView);
    }

    public void selectAll(){
        Log.e("onClickSelectAll","yes");
        SharedPreferences sharedPreferences = c.getSharedPreferences("entrance dialogs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putBoolean("mode_1", true);
        editor.commit();

        notifyDataSetChanged();
    }

    public void unselectAll(){
        SharedPreferences sharedPreferences = c.getSharedPreferences("entrance dialogs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putBoolean("mode_1", false);
        editor.commit();
        notifyDataSetChanged();
    }


    @Override
    public void onBindViewHolder(@NonNull MyAdapter holder, int position) {
        // Language preferences
        SharedPreferences sharedPreferences_lng = c.getSharedPreferences("Modes", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor_mode = sharedPreferences_lng.edit();

        // buttons preferences
        SharedPreferences sharedPreferences = c.getSharedPreferences("entrance dialogs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        ModeSelectionModel model = list.get(position);

        // Beautify text ---> Capitalize first letter and lowercase other
        String mode_name_beautified = capitalize(model.getModeName());
        holder.modeName.setText(mode_name_beautified);

        String mode_summary_beautified = capitalize(model.getModeSummaryName());
        holder.modeSummary.setText(mode_summary_beautified);


        if (sharedPreferences.getBoolean("mode_2",false)) {
            if (!sharedPreferences.getBoolean("mode_1", false)) {
                holder.checkBox.setChecked(false);
                editor_mode.putBoolean(model.getModeName(), false);
            } else {
                holder.checkBox.setChecked(true);
                editor_mode.putBoolean(model.getModeName(), true);
            }
            editor_mode.commit();
        } else {
            holder.checkBox.setChecked(sharedPreferences_lng.getBoolean(model.getModeName(),false));
        }


        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            // TODO fix false statement
            public void onClick(View view) {
                editor.putBoolean("mode_2", false);
                editor.commit();

                editor_mode.putBoolean(model.getModeName(), !sharedPreferences_lng.getBoolean(model.getModeName(),false));
                editor_mode.commit();
            }
        });
    }

    @Override
    public int getItemCount() {
        return size;
    }


    public class MyAdapter extends RecyclerView.ViewHolder {
        TextView modeName;
        TextView modeSummary;
        CheckBox checkBox;

        public MyAdapter(@NonNull View itemView) {
            super(itemView);
            modeName = itemView.findViewById(R.id.mode_name);
            modeSummary = itemView.findViewById(R.id.mode_summary);
            checkBox = itemView.findViewById(R.id.check_box);
        }
    }

    private String capitalize(String capString){
        StringBuffer capBuffer = new StringBuffer();
        Matcher capMatcher = Pattern.compile("([a-z])([a-z]*)", Pattern.CASE_INSENSITIVE).matcher(capString);
        while (capMatcher.find()){
            capMatcher.appendReplacement(capBuffer, capMatcher.group(1).toUpperCase() + capMatcher.group(2).toLowerCase());
        }

        return capMatcher.appendTail(capBuffer).toString();
    }
}

