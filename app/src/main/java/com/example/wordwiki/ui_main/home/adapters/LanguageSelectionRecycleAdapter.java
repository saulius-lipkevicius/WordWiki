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
import com.example.wordwiki.ui_main.home.models.LanguageSelectionModel;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LanguageSelectionRecycleAdapter extends RecyclerView.Adapter<LanguageSelectionRecycleAdapter.MyAdapter> {
    Context c;
    List<LanguageSelectionModel> list;
    int size;
    Boolean isSelectedAll;

    public LanguageSelectionRecycleAdapter(Context c, List<LanguageSelectionModel> list, int size) {
        this.c = c;
        this.list = list;
        this.size = size;
    }

    @NonNull
    @Override
    public MyAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_language_selection_item, parent,false);
        return new MyAdapter(rootView);
    }

    public void selectAll(){
        Log.e("onClickSelectAll","yes");
        SharedPreferences sharedPreferences = c.getSharedPreferences("entrance dialogs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putBoolean("language_1", true);
        editor.commit();

        notifyDataSetChanged();
    }

    public void unselectAll(){
        SharedPreferences sharedPreferences = c.getSharedPreferences("entrance dialogs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putBoolean("language_1", false);
        editor.commit();
        notifyDataSetChanged();
    }


    @Override
    public void onBindViewHolder(@NonNull MyAdapter holder, int position) {
        // Language preferences
        SharedPreferences sharedPreferences_lng = c.getSharedPreferences("Languages", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor_lng = sharedPreferences_lng.edit();

        // buttons preferences
        SharedPreferences sharedPreferences = c.getSharedPreferences("entrance dialogs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        LanguageSelectionModel model = list.get(position);

        // Beautify text ---> Capitalize first letter and lowercase other
        String language_name_beautified = capitalize(model.getLanguageName());
        holder.languageName.setText(language_name_beautified);
        holder.countryImage.setImageResource(model.getImageResource());

        if (sharedPreferences.getBoolean("language_2",false)) {
            if (!sharedPreferences.getBoolean("language_1", false)) {
                holder.checkBox.setChecked(false);
                editor_lng.putBoolean(model.getLanguageName(), false);
                editor_lng.commit();
            } else {
                holder.checkBox.setChecked(true);
                editor_lng.putBoolean(model.getLanguageName(), true);
                editor_lng.commit();
            }
        } else {
            holder.checkBox.setChecked(sharedPreferences_lng.getBoolean(model.getLanguageName(),false));
        }


        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            // TODO fix false statement
            public void onClick(View view) {
                editor.putBoolean("language_2", false);
                editor.commit();

                editor_lng.putBoolean(model.getLanguageName(), !sharedPreferences_lng.getBoolean(model.getLanguageName(),false));
                editor_lng.commit();
            }
        });
    }

    @Override
    public int getItemCount() {
        return size;
    }


    public class MyAdapter extends RecyclerView.ViewHolder {
        TextView languageName;
        CheckBox checkBox;
        ImageView countryImage;

        public MyAdapter(@NonNull View itemView) {
            super(itemView);
            languageName = itemView.findViewById(R.id.language_name);
            checkBox = itemView.findViewById(R.id.check_box);
            countryImage = itemView.findViewById(R.id.language_selection_row_image);
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
