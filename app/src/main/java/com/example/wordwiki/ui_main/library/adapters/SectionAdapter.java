package com.example.wordwiki.ui_main.library.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wordwiki.R;
import com.example.wordwiki.ui_main.library.models.SectionHelper;
import com.example.wordwiki.ui_main.library.models.SubsectionHelper;
import com.github.mikephil.charting.formatter.IFillFormatter;

import java.util.ArrayList;
import java.util.List;

public class SectionAdapter extends RecyclerView.Adapter<SectionAdapter.ViewHolder> implements Filterable  {


    //private ItemClickListener mClickListener;

    List<SectionHelper> sectionList;

    // additional list for storing filtered search
    List<SectionHelper> sectionListFull;
    final String TAG = "Hi";
    List<SectionHelper> sectionList2;


    //private ItemClickListener mClickListener;

    public SectionAdapter(List<SectionHelper> sectionList) {
        this.sectionList = sectionList;
        sectionListFull = new ArrayList<>(sectionList);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.fragment_library_section_item, parent, false);

        return new ViewHolder(view);
    }



    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int positionA) {

        SectionHelper section = sectionList.get(positionA);
        String sectionName = section.getSectionName();
        int sectionFlag = section.getSectionFlag();
        List<SubsectionHelper> items = section.getSectionItems();

        holder.sectionName.setText(sectionName);
        holder.sectionFlag.setImageResource(sectionFlag);
        // create recycler view for inner list
        
        holder.sectionName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sectionList.remove(holder.getAdapterPosition());
                notifyItemRemoved(holder.getAdapterPosition());
            }
        });
        
        SubsectionAdapter subsectionAdapter = new SubsectionAdapter(items
                , holder
        );
        holder.subsectionRecyclerView.setAdapter(subsectionAdapter);
        //holder.subsectionRecyclerView.addItemDecoration(new DividerItemDecoration(get, DividerItemDecoration.VERTICAL));

        
        Log.i(TAG, "onClick2: " + subsectionAdapter.getItemCount());

        // testing deleting items ---> when there is 0 elements in it.


           
    }

    @Override
    public int getItemCount() {
        return sectionList.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView sectionName;
        ImageView sectionFlag;
        RecyclerView subsectionRecyclerView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            sectionName = itemView.findViewById(R.id.fragment_library_section_name);
            sectionFlag = itemView.findViewById(R.id.fragment_library_section_flag);
            subsectionRecyclerView = itemView.findViewById(R.id.fragment_library_inner_recycler_view);
            itemView.setOnClickListener((View.OnClickListener) this);
        }

        @Override
        public void onClick(View view) {
            Log.i(TAG, "onClick: cia clickino");
            sectionList.remove(0);
            notifyItemRemoved(0);
        }
    }


    @Override
    public Filter getFilter() {
        return sectionFilter;
    }

    private Filter sectionFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            List<SectionHelper> filteredList = new ArrayList<>();


            if (charSequence == null || charSequence.length() == 0) {

                filteredList.addAll(sectionListFull);
            } else {
                String filterPattern = charSequence.toString().toLowerCase().trim();

                for (SectionHelper item : sectionListFull) {
                    //if (item.getSectionName().toLowerCase().contains(filterPattern)) {
                    List<SubsectionHelper> filteredSubList = new ArrayList<>();
                    for (int i = 0; i < item.getSectionItems().size(); i++) {
                        if (item.getSectionItems().get(i).getSubsectionName().toLowerCase().contains(filterPattern)) {
                            filteredSubList.add(new SubsectionHelper(item.getSectionName()
                                    , item.getSectionItems().get(i).getSubsectionName()
                                    , item.getSectionItems().get(i).getDescription()
                                    , item.getSectionItems().get(i).getCreator()
                                    , item.getSectionItems().get(i).getSubsectionLevel()
                                    , item.getSectionItems().get(i).getRatedUp()
                                    , item.getSectionItems().get(i).getRatedDown()
                                    , item.getSectionItems().get(i).getExpanded()));
                        }
                    }

                    filteredList.add(new SectionHelper(item.getSectionName(), item.getSectionFlag(), filteredSubList));
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;

            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            sectionList.clear();
            sectionList.addAll((List) filterResults.values);
            notifyDataSetChanged();
        }
    };
}
