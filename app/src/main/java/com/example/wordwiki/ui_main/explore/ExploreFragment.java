package com.example.wordwiki.ui_main.explore;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wordwiki.R;
import com.example.wordwiki.classes.LoadingDialog;
import com.example.wordwiki.classes.SuccessDialog;
import com.example.wordwiki.database.DatabaseHelper;
import com.example.wordwiki.databinding.FragmentExploreBinding;
import com.example.wordwiki.ui_main.explore.adapters.ImportAdapter;
import com.example.wordwiki.ui_main.explore.classes.AsyncTaskClassesGetCloudDictionaries;
import com.example.wordwiki.ui_main.explore.models.ImportModel;
import com.example.wordwiki.ui_main.explore.models.LanguageModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ExploreFragment extends Fragment{
    FragmentExploreBinding binding;
    final String TAG = "Dictionary Importing";


    private List<ImportModel> sectionsList;
    private List<ImportModel> sectionsListFull;
    private ArrayList<String> checkedItems;
    private ImportAdapter adapter;
    Button downloadBtn;

    private DatabaseHelper myDb;
    DatabaseReference dbLanguage;
    private String learningLanguageFiller, sectionNameFiller;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    // TODO create minimized recycle view where you tag libraries you want to import.
    // TODO add description of library + small descriptive list?

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentExploreBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        setUpRecyclerView();

        //fillSectionsList();


        //setUpButtons();

        return root;
    }

    /*
    private void setUpButtons() {
        downloadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "Dictionaries are finally loaded up :)", Toast.LENGTH_SHORT).show();

                //Log.d(TAG, "onClick: 2222: " + checkedItems.size());
                //languageImporter.importCloud(languageName, sectionName, getContext());

                // TODO loadup screen and dialog that it was written in downloads
                //final LoadingDialog loadingDialog = new LoadingDialog(ImportCloudActivity.this);
                //final SuccessDialog successDialog = new SuccessDialog(ImportCloudActivity.this);

                //showAlert();
            }
        });
    }

     */

    ValueEventListener valueEventListener = new ValueEventListener() {

        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {

            Log.d(TAG, "onCreate: loc2: ");
            sectionsList.clear();
            if (snapshot.exists()) {
                for (DataSnapshot snapshot_iter : snapshot.getChildren()) {
                    LanguageModel lng = snapshot_iter.getValue(LanguageModel.class);


                    // TODO do not add all words here, better create an alternative reference in the firebase

                    sectionsList.add(new ImportModel( lng.getNativeLanguage()
                            , lng.getLearningLanguage()
                            , lng.getUsername()
                            , lng.getDownloadCount()
                            , lng.getSectionLevel()
                            , lng.getLikesSum()
                            , lng.getWordsCount()
                            , lng.getSectionName()
                            , null //lng.getWords()
                            , null //lng.getTranslations()
                            , false

                    ));  // cia papildyti su quey resultatais
                    Log.d("a", "onCreate: change7 " + sectionsList.size());

                }
                Collections.sort(sectionsList, new Comparator<ImportModel>() {
                    @Override
                    public int compare(ImportModel lhs, ImportModel rhs) {
                        return lhs.getLanguageSection().compareTo(rhs.getLanguageSection());
                    }
                });

                Log.d("test", "onDataChange: 111 ");

                Log.d(TAG, "onCreate: loc3: ");
                adapter.notifyDataSetChanged();



            }
            if (sectionsListFull == null || sectionsListFull.size() < sectionsList.size()){
                sectionsListFull = sectionsList;
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    };


    private void setUpRecyclerView() {
        RecyclerView recyclerView = binding.getRoot().findViewById(R.id.import_cloud_recycler_view);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        sectionsList = new ArrayList<>();
        sectionsListFull = new ArrayList<>();
        adapter = new ImportAdapter(sectionsList, getContext());
        recyclerView.setAdapter(adapter);


        // execute a task that creats a small loading screen and then pops up
        AsyncTaskClassesGetCloudDictionaries taskClassesGetCloudDictionaries = new AsyncTaskClassesGetCloudDictionaries(dbLanguage, valueEventListener);

        String[] inputLanguageInfo = {"Danish", "A1"};
        taskClassesGetCloudDictionaries.execute(inputLanguageInfo);

        //


    }


/*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.import_cloud_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.import_cloud_action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();

        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });
        return true;
    }

 */

    public void showAlert() {
        int width = (int) (getResources().getDisplayMetrics().widthPixels * 0.90);
        int height = (int) (getResources().getDisplayMetrics().heightPixels * 0.40);

        // Continuing with dialog flow
        final LoadingDialog loadingDialog = new LoadingDialog(getActivity());
        final SuccessDialog successDialog = new SuccessDialog(getActivity());

        loadingDialog.startLoadingDialog();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                loadingDialog.dismissDialog();

                successDialog.startConfirmationDialog();

                Button main_activity_btn_confirmation = successDialog.dialog.findViewById(R.id.btn_okay_confirmation);
                Button continue_btn_confirmation = successDialog.dialog.findViewById(R.id.btn_cont_confirmation);
                TextView popUpText = successDialog.dialog.findViewById(R.id.wording_total_statistics_text);

                popUpText.setText("You have exported Language words to the excel in your Download Directory");

                continue_btn_confirmation.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        successDialog.dismissDialog();
                    }
                });

                main_activity_btn_confirmation.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        successDialog.dismissDialog();
                        // TODO add navigation here to the destination
                        //Intent intent = new Intent(view.getContext(), EntranceActivity.class);
                        //startActivity(intent);
                    }
                });
            }
        }, 5000);
    }
}