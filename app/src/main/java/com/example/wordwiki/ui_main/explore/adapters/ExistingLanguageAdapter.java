package com.example.wordwiki.ui_main.explore.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.utils.widget.ImageFilterView;
import androidx.recyclerview.widget.RecyclerView;

import com.blongho.country_data.World;
import com.example.wordwiki.R;
import com.example.wordwiki.database.DatabaseHelper;
import com.example.wordwiki.ui_main.explore.models.ExistingLanguageHelper;

import java.util.List;

public class ExistingLanguageAdapter extends RecyclerView.Adapter<ExistingLanguageAdapter.ExistingLanguageViewHolder>{
    private List<ExistingLanguageHelper> sectionsList;
    DatabaseHelper myDb;
    Context context;
    String countryISO;
    private String TAG = "aaa";

    final private ListItemClickListener mOnClickListener;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    public ExistingLanguageAdapter(List<ExistingLanguageHelper> sectionsList, Context context, ListItemClickListener listener) {
        this.sectionsList = sectionsList;
        //sectionsListFull =  new ArrayList<>(sectionsList);
       // Log.d(TAG, "onCreate: loc4: " + sectionsList.size() + " ir full size: " + sectionsListFull.size());
        this.context = context;
        mOnClickListener = listener;
    }

    @NonNull
    @Override
    public ExistingLanguageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_explore_filter_dictionary_item,
                parent, false);

         sharedPreferences = context.getSharedPreferences("filters", Context.MODE_PRIVATE);
         editor = sharedPreferences.edit();

        return new ExistingLanguageAdapter.ExistingLanguageViewHolder(v);
    }


    @Override
    public void onBindViewHolder(@NonNull ExistingLanguageViewHolder holder, int position) {
        World.init(context.getApplicationContext());
        myDb = new DatabaseHelper(context);

        ExistingLanguageHelper currentItem = sectionsList.get(position);

        holder.languageName.setText(currentItem.getLanguageName());

        countryISO = myDb.getFlagISO(currentItem.getLanguageName());
        int flag = World.getFlagOf(countryISO);
        holder.countryImage.setImageResource(flag);

        if (sharedPreferences.getInt(sectionsList.get(position).getLanguageName(), 0) == 1) {
            holder.itemView.setBackgroundColor(Color.GRAY);
        } else {
            holder.itemView.setBackgroundColor(Color.TRANSPARENT);
        }

        // set dictionaryCounter
        holder.dictionaryCounter.setText("(" + currentItem.getDictionaryCounter() + ")");

        /*
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (holder.itemView.getDrawingCacheBackgroundColor() == Color.GRAY){
                    // make it unselected
                    holder.itemView.setBackgroundColor(Color.TRANSPARENT);
                } else {
                    // make it selected
                    holder.itemView.setBackgroundColor(Color.GRAY);

                }
            }
        });

         */

        // TODO add functionality to click on a language bar
    }

    @Override
    public int getItemCount() {
        return sectionsList.size();
    }

    public interface ListItemClickListener {
        void onProgressListClick(int clickedItemIndex, Boolean isSelected);
    }

    public class ExistingLanguageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageFilterView countryImage;
        TextView languageName;
        TextView dictionaryCounter;

        public ExistingLanguageViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);

            // change ids
            languageName = itemView.findViewById(R.id.explore_filter_language);
            countryImage = itemView.findViewById(R.id.explore_filter_flag);
            dictionaryCounter = itemView.findViewById(R.id.explore_filter_dictionary_count);
        }

        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            Log.i(TAG, "onClick: language is clicked bool is: " + sectionsList.get(clickedPosition).getLanguageName() + " with bool of " + sharedPreferences.getInt(sectionsList.get(clickedPosition).getLanguageName(), 0));



            if (sharedPreferences.getInt(sectionsList.get(clickedPosition).getLanguageName(), 0) == 1) {
                itemView.setBackgroundColor(Color.TRANSPARENT);
                editor.putInt(sectionsList.get(clickedPosition).getLanguageName(), 0);
                mOnClickListener.onProgressListClick(clickedPosition, false);
            } else {
                itemView.setBackgroundColor(Color.GRAY);
                editor.putInt(sectionsList.get(clickedPosition).getLanguageName(), 1);
                mOnClickListener.onProgressListClick(clickedPosition, true);
            }

            editor.apply();

            // TODO make it async task after language is pressed
        }
    }
}
