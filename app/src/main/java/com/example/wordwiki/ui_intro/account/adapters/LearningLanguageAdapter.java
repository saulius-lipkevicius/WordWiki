package com.example.wordwiki.ui_intro.account.adapters;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.utils.widget.ImageFilterView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wordwiki.R;
import com.example.wordwiki.ui_intro.account.classes.RecyclerViewClickInterface;
import com.example.wordwiki.ui_intro.account.models.LearningLanguageHelper;
import java.util.List;

public class LearningLanguageAdapter extends RecyclerView.Adapter<LearningLanguageAdapter.ViewHolder>{
    final String TAG = "LearningLanguageAdapter";
    List<LearningLanguageHelper> learningLanguageList;
    private RecyclerViewClickInterface recyclerViewClickInterface;

    public LearningLanguageAdapter(List<LearningLanguageHelper> learningLanguageList, RecyclerViewClickInterface recyclerViewClickInterface) {
        this.learningLanguageList = learningLanguageList;
        this.recyclerViewClickInterface = recyclerViewClickInterface;
    }

    @NonNull
    @Override
    public LearningLanguageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.fragment_create_user_learning_language_item, parent, false);

        return new LearningLanguageAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LearningLanguageAdapter.ViewHolder holder, int position) {
        LearningLanguageHelper section = learningLanguageList.get(holder.getAdapterPosition());

        holder.languageName.setText(section.getLanguageName());
        holder.flag.setImageResource(section.getFlag());

        holder.isChecked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // change color of RecyclerView if it is checked.
                if (section.isChecked()) {
                    holder.isChecked.setImageResource(R.drawable.ic_check_box);
                    holder.itemView.getBackground().setTint(Color.TRANSPARENT);
                } else {
                    holder.isChecked.setImageResource(R.drawable.ic_checked);
                    holder.itemView.getBackground().setTint(Color.GRAY);
                }
                section.setChecked(!section.isChecked());

                holder.itemView.performClick();
            }
        });
    }

    @Override
    public int getItemCount() {
        return learningLanguageList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView languageName;
        private ImageFilterView flag;
        private ImageButton isChecked;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            languageName = itemView.findViewById(R.id.fragment_create_user_learning_language_name);
            flag = itemView.findViewById(R.id.fragment_create_user_learning_language_flag);
            isChecked = itemView.findViewById(R.id.fragment_create_user_known_language_check);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    recyclerViewClickInterface.onItemClick(getAdapterPosition());
                }
            });
        }
    }
}
