package com.example.wordwiki.ui_intro.account.adapters;

import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.constraintlayout.utils.widget.ImageFilterView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wordwiki.R;
import com.example.wordwiki.ui_intro.account.classes.RecyclerViewClickInterface;
import com.example.wordwiki.ui_intro.account.models.KnownLanguageHelper;
import com.example.wordwiki.ui_main.library.adapters.SectionAdapter;
import com.example.wordwiki.ui_main.library.models.SectionHelper;

import java.util.List;

public class KnownLanguageAdapter extends RecyclerView.Adapter<KnownLanguageAdapter.ViewHolder> {
    final String TAG = "KnownLanguageAdapter";
    List<KnownLanguageHelper> knownLanguageList;

    private RecyclerViewClickInterface recyclerViewClickInterface;

    public KnownLanguageAdapter(List<KnownLanguageHelper> knownLanguageList, RecyclerViewClickInterface recyclerViewClickInterface) {
        this.knownLanguageList = knownLanguageList;
        this.recyclerViewClickInterface = recyclerViewClickInterface;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.fragment_create_user_known_language_item, parent, false);

        return new KnownLanguageAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        KnownLanguageHelper section = knownLanguageList.get(holder.getAdapterPosition());

        holder.languageName.setText(section.getLanguageName());
        holder.flag.setImageResource(section.getFlag());

        holder.a1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!section.isA1()){
                    section.setChosen(1);
                    holder.a1.setBackgroundColor(Color.BLACK);

                    holder.a2.setBackgroundColor(Color.TRANSPARENT);
                    holder.b1.setBackgroundColor(Color.TRANSPARENT);
                    holder.b2.setBackgroundColor(Color.TRANSPARENT);
                    holder.c1.setBackgroundColor(Color.TRANSPARENT);
                    holder.c2.setBackgroundColor(Color.TRANSPARENT);
                    holder.isNative.setBackgroundColor(Color.TRANSPARENT);
                } else {
                    holder.a1.setBackgroundColor(Color.TRANSPARENT);
                    section.setA1(false);
                }
                holder.itemView.performClick();
            }
        });

        holder.a2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!section.isA2()){
                    section.setChosen(2);
                    holder.a2.setBackgroundColor(Color.BLACK);

                    holder.a1.setBackgroundColor(Color.TRANSPARENT);
                    holder.b1.setBackgroundColor(Color.TRANSPARENT);
                    holder.b2.setBackgroundColor(Color.TRANSPARENT);
                    holder.c1.setBackgroundColor(Color.TRANSPARENT);
                    holder.c2.setBackgroundColor(Color.TRANSPARENT);
                    holder.isNative.setBackgroundColor(Color.TRANSPARENT);
                } else {
                    holder.a2.setBackgroundColor(Color.TRANSPARENT);
                    section.setA2(false);
                }
                holder.itemView.performClick();
            }
        });


        holder.b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!section.isB1()){
                    section.setChosen(3);
                    holder.b1.setBackgroundColor(Color.BLACK);

                    holder.a1.setBackgroundColor(Color.TRANSPARENT);
                    holder.a2.setBackgroundColor(Color.TRANSPARENT);
                    holder.b2.setBackgroundColor(Color.TRANSPARENT);
                    holder.c1.setBackgroundColor(Color.TRANSPARENT);
                    holder.c2.setBackgroundColor(Color.TRANSPARENT);
                    holder.isNative.setBackgroundColor(Color.TRANSPARENT);
                } else {
                    holder.b1.setBackgroundColor(Color.TRANSPARENT);
                    section.setB1(false);
                }
                holder.itemView.performClick();
            }
        });

        holder.b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!section.isB2()){
                    section.setChosen(4);
                    holder.b2.setBackgroundColor(Color.BLACK);

                    holder.a1.setBackgroundColor(Color.TRANSPARENT);
                    holder.a2.setBackgroundColor(Color.TRANSPARENT);
                    holder.b1.setBackgroundColor(Color.TRANSPARENT);
                    holder.c1.setBackgroundColor(Color.TRANSPARENT);
                    holder.c2.setBackgroundColor(Color.TRANSPARENT);
                    holder.isNative.setBackgroundColor(Color.TRANSPARENT);
                } else {
                    holder.b2.setBackgroundColor(Color.TRANSPARENT);
                    section.setB2(false);
                }
                holder.itemView.performClick();
            }
        });

        holder.c1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!section.isC1()){
                    section.setChosen(5);
                    holder.c1.setBackgroundColor(Color.BLACK);

                    holder.a1.setBackgroundColor(Color.TRANSPARENT);
                    holder.a2.setBackgroundColor(Color.TRANSPARENT);
                    holder.b1.setBackgroundColor(Color.TRANSPARENT);
                    holder.b2.setBackgroundColor(Color.TRANSPARENT);
                    holder.c2.setBackgroundColor(Color.TRANSPARENT);
                    holder.isNative.setBackgroundColor(Color.TRANSPARENT);
                } else {
                    holder.c1.setBackgroundColor(Color.TRANSPARENT);
                    section.setC1(false);
                }
                holder.itemView.performClick();
            }
        });

        holder.c2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!section.isC2()){
                    section.setChosen(6);
                    holder.c2.setBackgroundColor(Color.BLACK);

                    holder.a1.setBackgroundColor(Color.TRANSPARENT);
                    holder.a2.setBackgroundColor(Color.TRANSPARENT);
                    holder.b1.setBackgroundColor(Color.TRANSPARENT);
                    holder.b2.setBackgroundColor(Color.TRANSPARENT);
                    holder.c1.setBackgroundColor(Color.TRANSPARENT);
                    holder.isNative.setBackgroundColor(Color.TRANSPARENT);
                } else {
                    holder.c2.setBackgroundColor(Color.TRANSPARENT);
                    section.setC2(false);
                }
                holder.itemView.performClick();
            }
        });


        holder.isNative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!section.isNative()){
                    section.setChosen(7);
                    holder.isNative.setBackgroundColor(Color.BLACK);

                    holder.a1.setBackgroundColor(Color.TRANSPARENT);
                    holder.a2.setBackgroundColor(Color.TRANSPARENT);
                    holder.b1.setBackgroundColor(Color.TRANSPARENT);
                    holder.b2.setBackgroundColor(Color.TRANSPARENT);
                    holder.c1.setBackgroundColor(Color.TRANSPARENT);
                    holder.c2.setBackgroundColor(Color.TRANSPARENT);
                } else {
                    holder.isNative.setBackgroundColor(Color.TRANSPARENT);
                    section.setNative(false);
                }
                holder.itemView.performClick();
            }
        });

    }

    @Override
    public int getItemCount() {
        return knownLanguageList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        private TextView languageName;
        private ImageFilterView flag;
        private AppCompatButton a1, a2, b1, b2, c1, c2, isNative;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    recyclerViewClickInterface.onItemClick(getAdapterPosition());
                }
            });

            languageName = itemView.findViewById(R.id.fragment_create_user_known_language_name);
            flag = itemView.findViewById(R.id.fragment_create_user_known_language_flag);

            a1 = itemView.findViewById(R.id.know_language_btn_a1);
            a2 = itemView.findViewById(R.id.know_language_btn_a2);
            b1 = itemView.findViewById(R.id.know_language_btn_b1);
            b2 = itemView.findViewById(R.id.know_language_btn_b2);
            c1 = itemView.findViewById(R.id.know_language_btn_c1);
            c2 = itemView.findViewById(R.id.know_language_btn_c2);
            isNative = itemView.findViewById(R.id.know_language_btn_native);

        }
    }
}
