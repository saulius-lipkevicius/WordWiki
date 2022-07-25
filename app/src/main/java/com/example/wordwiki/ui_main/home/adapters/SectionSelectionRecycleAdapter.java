package com.example.wordwiki.ui_main.home.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wordwiki.R;
import com.example.wordwiki.ui_main.home.models.SectionSelectionModel;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SectionSelectionRecycleAdapter extends RecyclerView.Adapter<SectionSelectionRecycleAdapter.MyAdapter> {
    Context c;
    List<SectionSelectionModel> list;
    int size;

    public SectionSelectionRecycleAdapter(Context c, List<SectionSelectionModel> list, int size) {
        this.c = c;
        this.list = list;
        this.size = size;
    }

    @NonNull
    @Override
    public MyAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_section_selection_item, parent, false);
        return new MyAdapter(rootView);
    }

    public void selectAll() {
        Log.e("onClickSelectAll", "yes");
        SharedPreferences sharedPreferences = c.getSharedPreferences("entrance dialogs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putBoolean("section_1", true);
        editor.commit();

        notifyDataSetChanged();
    }

    public void unselectAll() {
        SharedPreferences sharedPreferences = c.getSharedPreferences("entrance dialogs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putBoolean("section_1", false);
        editor.commit();
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull MyAdapter holder, int position) {
        // Language preferences
        SharedPreferences sharedPreferences_sec = c.getSharedPreferences("Sections", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor_sec = sharedPreferences_sec.edit();

        // buttons preferences
        SharedPreferences sharedPreferences = c.getSharedPreferences("entrance dialogs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        SectionSelectionModel model = list.get(position);

        // Beautification
        String language_name_beautified = capitalize(model.getLanguageName());
        String section_name_beautified = capitalize(model.getSectionName());

        holder.sectionName.setText(section_name_beautified);
        holder.languageName.setText(language_name_beautified);
        holder.countryImage.setImageResource(model.getImageResource());

        if (sharedPreferences.getBoolean("section_2", false)) {
            if (!sharedPreferences.getBoolean("section_1", false)) {
                holder.checkBox.setChecked(false);
                editor_sec.putBoolean(model.getLanguageName() + model.getSectionName(), false);
            } else {
                holder.checkBox.setChecked(true);
                editor_sec.putBoolean(model.getLanguageName() + model.getSectionName(), true);
            }
            editor_sec.commit();
        } else {
            holder.checkBox.setChecked(sharedPreferences_sec.getBoolean(model.getLanguageName() + model.getSectionName(), false));
        }


        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            // TODO fix false statement
            public void onClick(View view) {
                editor.putBoolean("section_2", false);
                editor.commit();

                editor_sec.putBoolean(model.getLanguageName() + model.getSectionName()
                        , !sharedPreferences_sec.getBoolean(model.getLanguageName() + model.getSectionName(), false));
                editor_sec.commit();
            }
        });


    }


    @Override
    public int getItemCount() {
        return size;
    }

    public class MyAdapter extends RecyclerView.ViewHolder {
        TextView sectionName;
        TextView languageName;
        CheckBox checkBox;
        ImageView countryImage;

        public MyAdapter(@NonNull View itemView) {
            super(itemView);
            sectionName = itemView.findViewById(R.id.section_name);
            languageName = itemView.findViewById(R.id.language_name);
            checkBox = itemView.findViewById(R.id.check_box);
            countryImage = itemView.findViewById(R.id.section_selection_row_image);
        }
    }

    private String capitalize(String capString) {
        StringBuffer capBuffer = new StringBuffer();
        Matcher capMatcher = Pattern.compile("([a-z])([a-z]*)", Pattern.CASE_INSENSITIVE).matcher(capString);
        while (capMatcher.find()) {
            capMatcher.appendReplacement(capBuffer, capMatcher.group(1).toUpperCase() + capMatcher.group(2).toLowerCase());
        }

        return capMatcher.appendTail(capBuffer).toString();
    }
}
