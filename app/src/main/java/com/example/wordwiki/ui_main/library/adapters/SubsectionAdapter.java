package com.example.wordwiki.ui_main.library.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wordwiki.R;
import com.example.wordwiki.ui_main.library.models.SectionHelper;
import com.example.wordwiki.ui_main.library.models.SubsectionHelper;

import java.util.List;

public class SubsectionAdapter extends RecyclerView.Adapter<SubsectionAdapter.ViewHolder> {

    List<SubsectionHelper> items;
    final String TAG = "subsection item";
    public SubsectionAdapter(List<SubsectionHelper> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.fragment_library_subsection_item, parent, false);

        return new SubsectionAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        SubsectionHelper section = items.get(holder.getAdapterPosition());

        holder.subsectionName.setText(items.get(holder.getAdapterPosition()).getSubsectionName());
        holder.description.setText(section.getDescription());
        holder.creatorName.setText(section.getCreator());
        holder.subsectionLevel.setText(section.getSubsectionLevel());

        holder.deleteItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG, "onClick: " + items.get(holder.getAdapterPosition()).getSectionName() + ", " + items.get(holder.getAdapterPosition()).getSubsectionName());

                //TODO dialog to confirm and color it red
            }
        });

        holder.rateDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (items.get(holder.getAdapterPosition()).getRatedDown()){
                    holder.rateDown.setImageResource(R.drawable.ic_thumb_down);
                    items.get(holder.getAdapterPosition()).setRatedDown(false);
                }else{
                    if (items.get(holder.getAdapterPosition()).getRatedUp()){
                        holder.rateUp.setImageResource(R.drawable.ic_thumb_up);
                        items.get(holder.getAdapterPosition()).setRatedUp(false);
                    }

                    holder.rateDown.setImageResource(R.drawable.ic_thumb_down_fill);
                    items.get(holder.getAdapterPosition()).setRatedDown(true);
                }
            }
        });

        holder.rateUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (items.get(holder.getAdapterPosition()).getRatedUp()){
                    holder.rateUp.setImageResource(R.drawable.ic_thumb_up);
                    items.get(holder.getAdapterPosition()).setRatedUp(false);
                }else{
                    if (items.get(holder.getAdapterPosition()).getRatedDown()){
                        holder.rateDown.setImageResource(R.drawable.ic_thumb_down);
                        items.get(holder.getAdapterPosition()).setRatedDown(false);
                    }

                    holder.rateUp.setImageResource(R.drawable.ic_thumb_up_fill);
                    items.get(holder.getAdapterPosition()).setRatedUp(true);
                }
            }
        });

        holder.expandItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG, "onClick: aaaaa " + holder.expandableLayout.getVisibility());
                if (holder.expandableLayout.getVisibility() == View.VISIBLE){
                    holder.expandableLayout.setVisibility(View.GONE);
                }else{
                    holder.expandableLayout.setVisibility(View.VISIBLE);
                }

            }
        });

        holder.creatorName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG, "onClick: go to user");
            }
        });

        holder.reportSubsection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG, "onClick: report it." );
            }
        });

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        TextView subsectionName, description, creatorName, reportSubsection, subsectionLevel;
        ImageButton deleteItem ,rateUp, rateDown, expandItem;
        ConstraintLayout expandableLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            subsectionName = itemView.findViewById(R.id.fragment_library_subsection_name);
            description = itemView.findViewById(R.id.fragment_library_section_description);
            creatorName = itemView.findViewById(R.id.fragment_library_subsection_creator);
            reportSubsection = itemView.findViewById(R.id.fragment_library_subsection_report);
            subsectionLevel= itemView.findViewById(R.id.fragment_library_subsection_level);

            deleteItem = itemView.findViewById(R.id.fragment_library_subsection_delete);
            rateUp = itemView.findViewById(R.id.fragment_library_subsection_rate_up);
            rateDown = itemView.findViewById(R.id.fragment_library_subsection_rate_down);
            expandItem = itemView.findViewById(R.id.fragment_library_subsection_expand);

            expandableLayout = itemView.findViewById(R.id.fragment_library_expandable_layout);


        }
    }
}
