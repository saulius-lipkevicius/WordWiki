package com.example.wordwiki.ui_intro.account.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wordwiki.R;
import com.example.wordwiki.ui_intro.account.models.KnownLanguageHelper;
import com.example.wordwiki.ui_main.library.adapters.SectionAdapter;
import com.example.wordwiki.ui_main.library.models.SectionHelper;

import java.util.List;

public class KnownLanguageAdapter extends RecyclerView.Adapter<KnownLanguageAdapter.ViewHolder> {
    final String TAG = "KnownLanguageAdapter";
    List<KnownLanguageHelper> knownLanguageList;

    public KnownLanguageAdapter(List<KnownLanguageHelper> knownLanguageList) {
        this.knownLanguageList = knownLanguageList;
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
        KnownLanguageHelper section = knownLanguageList.get(position);

        holder.languageName.setText(section.getLanguageName());
        holder.flag.setImageResource(section.getFlag());

        holder.a1.setEnabled(section.isA1());
        holder.a2.setEnabled(section.isA2());
        holder.a1.setEnabled(section.isB1());
        holder.a2.setEnabled(section.isB2());
        holder.a1.setEnabled(section.isC1());
        holder.a2.setEnabled(section.isC2());
        //holder.isNative.setEnabled(section.isNative());

        holder.a1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                section.setA1(section.isA1());
                holder.a1.setEnabled(section.isA1());
            }
        });

        holder.a2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                section.setA2(section.isA2());
                holder.a2.setEnabled(section.isA2());
            }
        });

        holder.b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                section.setB1(section.isB1());
                holder.b1.setEnabled(section.isB1());
            }
        });

        holder.b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                section.setB2(section.isB2());
                holder.b2.setEnabled(section.isB2());
            }
        });

        holder.c1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                section.setC1(section.isC1());
                holder.c1.setEnabled(section.isC1());
            }
        });

        holder.c2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                section.setC2(section.isC2());
                holder.c2.setEnabled(section.isC2());
            }
        });

        /*
        holder.isNative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                section.setNative(section.isNative());
                holder.isNative.setEnabled(section.isNative());
            }
        });

         */


    }

    @Override
    public int getItemCount() {
        return knownLanguageList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView languageName;
        private ImageView flag;
        private Button a1, a2, b1, b2, c1, c2, isNative;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            languageName = itemView.findViewById(R.id.fragment_create_user_known_language_name);
            flag = itemView.findViewById(R.id.fragment_create_user_known_language_flag);

            a1 = itemView.findViewById(R.id.know_language_btn_a1);
            a2 = itemView.findViewById(R.id.know_language_btn_a2);
            b1 = itemView.findViewById(R.id.know_language_btn_b1);
            b2 = itemView.findViewById(R.id.know_language_btn_b2);
            c1 = itemView.findViewById(R.id.know_language_btn_c1);
            c2 = itemView.findViewById(R.id.know_language_btn_c2);
            //isNative = itemView.findViewById(R.id.know_language_btn_native);

        }
    }
}
