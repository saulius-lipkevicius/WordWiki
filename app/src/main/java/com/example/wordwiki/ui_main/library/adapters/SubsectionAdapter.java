package com.example.wordwiki.ui_main.library.adapters;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wordwiki.R;
import com.example.wordwiki.classes.LoadingDialog;
import com.example.wordwiki.classes.SuccessDialog;
import com.example.wordwiki.database.DatabaseHelper;
import com.example.wordwiki.ui_main.library.classes.ExportClass;
import com.example.wordwiki.ui_main.library.models.SectionHelper;
import com.example.wordwiki.ui_main.library.models.SubsectionHelper;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SubsectionAdapter extends RecyclerView.Adapter<SubsectionAdapter.ViewHolder> {
    Context context;
    private final SectionAdapter.ViewHolder parent;
    DatabaseHelper myDb;
    List<SubsectionHelper> items;
    final String TAG = "subsection item";
    SubsectionHelper currentItem;

    public SubsectionAdapter(List<SubsectionHelper> items, SectionAdapter.ViewHolder parent, Context context) {
        this.items = items;
        this.parent = parent;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.fragment_library_subsection_item, parent, false);

        myDb = new DatabaseHelper(parent.getContext());

        return new SubsectionAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        myDb = new DatabaseHelper(context);
        currentItem = items.get(holder.getAdapterPosition());

        Log.i(TAG, "onBindViewHolder: test filter: " + currentItem.getSectionName() + " : " + holder.creatorName);

        holder.subsectionName.setText(items.get(holder.getAdapterPosition()).getSubsectionName());

        holder.creatorName.setText(currentItem.getCreator());
        holder.subsectionLevel.setText(currentItem.getSubsectionLevel());

        if (currentItem.getDescription() == null) {
            holder.description.setText("No Description. Press to Edit");
        } else {
            holder.description.setText(currentItem.getDescription());
        }

        // TODO find current username
        SharedPreferences sharedPreferences = context.getSharedPreferences("user_profile", Context.MODE_PRIVATE);
        String currentUsername = sharedPreferences.getString("username", "UNKNOWN");


        // TODO add username to an imported dictionary, otherwise it will give an error
        if (currentItem.getCreator().equals(currentUsername)) {
            holder.creatorName.setText("You");
            holder.reportSubsection.setText("Share");

            Boolean isShared = myDb.isYourDictionaryPublished(currentUsername    //holder.creatorName.getText().toString() // it used 'You' which gave an error in the share/shared case
                    , items.get(holder.getAdapterPosition()).getSectionName()
                    , holder.subsectionName.getText().toString());
            if (isShared) {
                holder.reportSubsection.setText("SharED");
                holder.reportSubsection.setClickable(false);
            } else {
                holder.reportSubsection.setText("Share");
            }

            // hide like/dislike buttons to remove self-evaluation
            holder.rateUp.setVisibility(View.GONE);
            holder.rateDown.setVisibility(View.GONE);
        } else {
            holder.creatorName.setText(currentItem.getCreator());
            holder.reportSubsection.setText("Report It");

            // recreate like/dislike buttons
            holder.rateUp.setVisibility(View.VISIBLE);
            holder.rateDown.setVisibility(View.VISIBLE);
        }


        if (items.get(holder.getAdapterPosition()).getRatedUp()) {
            holder.rateUp.setImageResource(R.drawable.ic_thumb_up_fill);
            holder.rateDown.setImageResource(R.drawable.ic_thumb_down);
        } else if (items.get(holder.getAdapterPosition()).getRatedDown()) {
            holder.rateDown.setImageResource(R.drawable.ic_thumb_down_fill);
            holder.rateUp.setImageResource(R.drawable.ic_thumb_up);
        } else {
            holder.rateDown.setImageResource(R.drawable.ic_thumb_down);
            holder.rateUp.setImageResource(R.drawable.ic_thumb_up);
        }


        holder.deleteItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String languageName = items.get(holder.getAdapterPosition()).getSectionName();
                String sectionName = items.get(holder.getAdapterPosition()).getSubsectionName();
                //notifyItemRemoved(position);


                if (parent.subsectionRecyclerView.getAdapter().getItemCount() == 1) {
                    parent.sectionName.callOnClick();
                }

                //parent.subsectionRecyclerView.getAdapter().notifyItemRemoved(parent.getAdapterPosition());


                myDb.deleteDictionary(languageName, sectionName);
                items.remove(holder.getAdapterPosition());
                notifyItemRemoved(holder.getAdapterPosition());
                /*
                // TODO
                if (parent.getAdapter().getItemCount() == 0) {
                    Log.i(TAG, "onClick: " + sectionList.get(0));
                    Log.i(TAG, "onClick: " + positionTRUE);
                    Log.i(TAG, "onClick: " + sectionList.get(0));

                    parent.getAdapter().notifyDataSetChanged();
                }

                 */

            }
        });

        holder.rateDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (items.get(holder.getAdapterPosition()).getRatedDown()) {
                    // if it is already ratedDown

                    holder.rateDown.setImageResource(R.drawable.ic_thumb_down);
                    items.get(holder.getAdapterPosition()).setRatedDown(false);

                    // +1 because we just cancel the last voteDown
                    firebaseRateDictionary(1);
                } else {

                    if (items.get(holder.getAdapterPosition()).getRatedUp()) {
                        // voteUp was clicked so it means we have -2
                        firebaseRateDictionary(-2);

                        holder.rateUp.setImageResource(R.drawable.ic_thumb_up);
                        items.get(holder.getAdapterPosition()).setRatedUp(false);
                    } else {
                        // neither vote was clicked so we just -1
                        firebaseRateDictionary(-1);
                    }

                    holder.rateDown.setImageResource(R.drawable.ic_thumb_down_fill);
                    items.get(holder.getAdapterPosition()).setRatedDown(true);
                }
                myDb.rateDictionary(items.get(holder.getAdapterPosition()).getSectionName()
                        , items.get(holder.getAdapterPosition()).getSubsectionName()
                        , "down"
                        , items.get(holder.getAdapterPosition()).getRatedDown());

            }
        });

        holder.rateUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (items.get(holder.getAdapterPosition()).getRatedUp()) {
                    // if voteUp was clicked, it means we just -1 with canceling the last one
                    firebaseRateDictionary(-1);

                    holder.rateUp.setImageResource(R.drawable.ic_thumb_up);
                    items.get(holder.getAdapterPosition()).setRatedUp(false);
                } else {
                    if (items.get(holder.getAdapterPosition()).getRatedDown()) {
                        // vote exchange so it is +2
                        firebaseRateDictionary(2);

                        holder.rateDown.setImageResource(R.drawable.ic_thumb_down);
                        items.get(holder.getAdapterPosition()).setRatedDown(false);
                    } else {
                        // non was clicked so it is +1
                        firebaseRateDictionary(1);
                    }

                    holder.rateUp.setImageResource(R.drawable.ic_thumb_up_fill);
                    items.get(holder.getAdapterPosition()).setRatedUp(true);
                }
                myDb.rateDictionary(items.get(holder.getAdapterPosition()).getSectionName()
                        , items.get(holder.getAdapterPosition()).getSubsectionName()
                        , "up"
                        , items.get(holder.getAdapterPosition()).getRatedUp());
            }
        });

        holder.expandItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG, "onClick: aaaaa " + holder.expandableLayout.getVisibility());
                if (holder.expandableLayout.getVisibility() == View.VISIBLE) {
                    holder.expandableLayout.setVisibility(View.GONE);
                    holder.expandItem.setImageResource(R.drawable.ic_expand_circle_down);

                } else {
                    holder.expandableLayout.setVisibility(View.VISIBLE);
                    holder.expandItem.setImageResource(R.drawable.ic_expand_circle_up);
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
                Log.i(TAG, "onClick: report it.");

                if (holder.reportSubsection.getText().equals("Share")) {
                    // export to the cloud class
                    ExportClass.exportCloud(parent.sectionName.getText().toString()
                            , holder.subsectionName.getText().toString()
                            , holder.description.getText().toString()
                            , holder.subsectionLevel.getText().toString(), view.getContext());

                    // change value of isShared in the myDb so it is seen to not overuse the button
                    myDb.setYourDictionaryPublished(currentUsername
                            , items.get(holder.getAdapterPosition()).getSectionName()
                            , holder.subsectionName.getText().toString(), "1");
                    holder.reportSubsection.setText("SharED");

                } else if (holder.reportSubsection.getText().equals("Report It")) {
                    // TODO report a dictionary if that contains some malicious stuff
                    //askForPermission(Manifest.permission.READ_EXTERNAL_STORAGE, READ_EXST);
                    //askForPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, WRITE_EXST);

                    //Toast.makeText(ExportActivity.this, "Excel is exported " + checkedItems, Toast.LENGTH_SHORT).show();
                    // TODO export dictionary to an excel sheet, through fileManager
                    // TODO should export it to excel and not in report it case
                    // TODO !!!!!!!!!!!!!!!!!
                    // ExportClass.export(dictionaryName, view.getContext());

                    // TODO loadup screen and dialog that it was written in downloads
                    //final LoadingDialog loadingDialog = new LoadingDialog(view.getAct);
                    //final SuccessDialog successDialog = new SuccessDialog(ExportActivity.this);

                    //showAlert();
                }

            }
        });

        ///////////
    }

    private void firebaseRateDictionary(int voteChange) {

        // set firebase connection
        DatabaseReference mDatabase = FirebaseDatabase.getInstance("https://wordwiki-af0d4-default-rtdb.europe-west1.firebasedatabase.app").getReference("Dictionaries")
                .child(currentItem.getSectionName()).child(currentItem.getSubsectionLevel()).child(currentItem.getCreator() + "_" + currentItem.getSubsectionName()).child("likesSum");
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                long totalLikes = (long) dataSnapshot.getValue();
                //mDatabase.setValue(totalLikes + 1);


                Log.i(TAG, "onDataChange: the vote count is: " + totalLikes);

                // decide the direction of vote
                mDatabase.setValue(totalLikes + voteChange);

                Log.i(TAG, "onDataChange: the vote count post is: " + totalLikes);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView subsectionName, description, creatorName, reportSubsection, subsectionLevel;
        ImageButton deleteItem, rateUp, rateDown, expandItem;
        LinearLayout expandableLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            subsectionName = itemView.findViewById(R.id.fragment_library_subsection_name);
            description = itemView.findViewById(R.id.fragment_library_section_description);
            creatorName = itemView.findViewById(R.id.fragment_library_subsection_creator);
            reportSubsection = itemView.findViewById(R.id.fragment_library_subsection_report);
            subsectionLevel = itemView.findViewById(R.id.fragment_library_subsection_level);

            deleteItem = itemView.findViewById(R.id.fragment_library_subsection_delete);
            rateUp = itemView.findViewById(R.id.fragment_library_subsection_rate_up);
            rateDown = itemView.findViewById(R.id.fragment_library_subsection_rate_down);
            expandItem = itemView.findViewById(R.id.fragment_library_subsection_expand);

            expandableLayout = itemView.findViewById(R.id.fragment_library_expandable_layout);


        }
    }
}
