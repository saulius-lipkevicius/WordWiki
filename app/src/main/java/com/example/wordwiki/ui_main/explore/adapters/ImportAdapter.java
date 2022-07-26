package com.example.wordwiki.ui_main.explore.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.blongho.country_data.World;
import com.example.wordwiki.R;
import com.example.wordwiki.database.DatabaseHelper;
import com.example.wordwiki.ui_main.explore.OnItemClick;
import com.example.wordwiki.ui_main.explore.models.ImportModel;

import java.util.ArrayList;
import java.util.List;

public class ImportAdapter extends RecyclerView.Adapter<ImportAdapter.ImportLanguageViewHolder> implements Filterable {
    final String TAG = "import adapter";
    private List<ImportModel> sectionsList;
    private List<ImportModel> sectionsListFull;
    private OnItemClick mCallback;
    Context context;

    DatabaseHelper myDb;

    // holder to define one liner
    static class ImportLanguageViewHolder extends RecyclerView.ViewHolder {
        ImageView countryImage;
        TextView section_name;
        TextView language_name;
        CheckBox checkBox;

        ImportLanguageViewHolder(View itemView) {
            super(itemView);
            countryImage = itemView.findViewById(R.id.import_cloud_image);
            section_name = itemView.findViewById(R.id.import_cloud_section);
            language_name = itemView.findViewById(R.id.import_cloud_language);
            checkBox = itemView.findViewById(R.id.import_cloud_checkbox);
        }
    }

    public ImportAdapter(List<ImportModel> sectionsList, Context context, OnItemClick listener) {
        this.sectionsList = sectionsList;
        sectionsListFull =  new ArrayList<>(sectionsList);
        Log.d(TAG, "onCreate: loc4: " + sectionsList.size() + " ir full size: " + sectionsListFull.size());
        this.context = context;
        this.mCallback = listener;
    }

    @NonNull
    @Override
    public ImportLanguageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_explore_import_language_item,
                parent, false);
        return new ImportLanguageViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ImportLanguageViewHolder holder, int position) {
        World.init(context.getApplicationContext());
        myDb = new DatabaseHelper(context);

        ImportModel currentItem = sectionsList.get(position);

        holder.section_name.setText(currentItem.getSectionName());
        holder.language_name.setText(currentItem.getLearningLanguage());

        String country_name = myDb.getFlagISO(currentItem.getLearningLanguage());
        int flag = World.getFlagOf(country_name);
        holder.countryImage.setImageResource(flag);

        holder.checkBox.setChecked(currentItem.getCheckBox());

        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentItem.setCheckBox(!currentItem.getCheckBox());
                holder.checkBox.setChecked(currentItem.getCheckBox());

                mCallback.onClick(currentItem.getSectionName(), currentItem.getLearningLanguage(), currentItem.getCheckBox());
            }
        });
    }

    @Override
    public int getItemCount() {
        return sectionsList.size();
    }

    public Filter getFilter() {
        return exampleFilter;
    }

    private final Filter exampleFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            if (sectionsListFull == null || sectionsListFull.size() < sectionsList.size()){
                sectionsListFull.addAll(sectionsList);
            }

            Log.d(TAG, "onCreate: loc5: " + sectionsList.size() + " ir full size: " + sectionsListFull.size());
            List<ImportModel> filteredList = new ArrayList<>();

            Log.d("testas", "performFiltering: " + sectionsListFull.size());
            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(sectionsListFull);
                Log.d(TAG, "onCreate: loc6: " + sectionsList.size() + " ir full size: " + sectionsListFull.size());
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (ImportModel item : sectionsListFull) {
                    if (item.getLearningLanguage().toLowerCase().contains(filterPattern) || item.getSectionName().toLowerCase().contains(filterPattern)) {
                        filteredList.add(item);
                        Log.d(TAG, "onCreate: loc7: " + sectionsList.size() + " ir full size: " + sectionsListFull.size());
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;
            Log.d(TAG, "onCreate: loc8: " + sectionsList.size() + " ir full size: " + sectionsListFull.size());

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            Log.d(TAG, "onCreate: loc9: " + sectionsList.size() + " ir full size: " + sectionsListFull.size());
            sectionsList.clear();
            Log.d(TAG, "onCreate: loc10: " + sectionsList.size() + " ir full size: " + sectionsListFull.size());
            sectionsList.addAll((List) results.values);

            notifyDataSetChanged();
            Log.d(TAG, "onCreate: loc11: " + sectionsList.size() + " ir full size: " + sectionsListFull.size());
        }
    };
}
