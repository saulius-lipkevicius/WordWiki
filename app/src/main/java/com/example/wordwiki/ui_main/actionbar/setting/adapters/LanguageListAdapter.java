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
import androidx.constraintlayout.utils.widget.ImageFilterView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wordwiki.R;
import com.example.wordwiki.ui_main.actionbar.setting.models.LanguagesModel;
import com.example.wordwiki.ui_main.actionbar.setting.models.SettingListModel;

import java.util.List;

public class LanguageListAdapter extends RecyclerView.Adapter {
    List<LanguagesModel> mlist;
    private final LanguageListAdapter.OnSettingListener onSettingListener;

    public LanguageListAdapter(List<LanguagesModel> list, LanguageListAdapter.OnSettingListener onSettingListener) {
        this.mlist = list;
        this.onSettingListener = onSettingListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.fragment_settings_languages_item, parent, false);

        return new LanguageListAdapter.ViewHolder(view, onSettingListener);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        LanguagesModel model = mlist.get(position);
        LanguageListAdapter.ViewHolder viewHolder = (LanguageListAdapter.ViewHolder) holder;

        viewHolder.languageFlag.setImageResource(model.getFlag());
        viewHolder.languageName.setText(model.getLanguageName());
    }

    @Override
    public int getItemCount() {
        return mlist.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageFilterView languageFlag;
        TextView languageName;
        LanguageListAdapter.OnSettingListener onSettingListener;

        public ViewHolder(@NonNull View itemView, LanguageListAdapter.OnSettingListener onSettingListener) {
            super(itemView);

            languageName = itemView.findViewById(R.id.language_name);
            languageFlag = itemView.findViewById(R.id.language_flag);

            this.onSettingListener = onSettingListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onSettingListener.onSettingClick(getAdapterPosition());
        }
    }

    public interface OnSettingListener {
        void onSettingClick(int position);
    }
}
