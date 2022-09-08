package com.example.wordwiki.ui_main.explore;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wordwiki.R;
import com.example.wordwiki.classes.LoadingDialog;
import com.example.wordwiki.classes.SuccessDialog;
import com.example.wordwiki.database.DatabaseHelper;
import com.example.wordwiki.databinding.FragmentExploreBinding;
import com.example.wordwiki.ui_main.actionbar.setting.SettingFragment;
import com.example.wordwiki.ui_main.actionbar.setting.sub_settings.dialogs.FeedbackFragmentDialog;
import com.example.wordwiki.ui_main.explore.adapters.ImportAdapter;
import com.example.wordwiki.ui_main.explore.classes.AsyncTaskClassLoadMetadata;
import com.example.wordwiki.ui_main.explore.classes.AsyncTaskClassesGetCloudDictionaries;
import com.example.wordwiki.ui_main.explore.models.ExistingLanguageHelper;
import com.example.wordwiki.ui_main.explore.models.ImportModel;
import com.example.wordwiki.ui_main.explore.models.LanguageModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ExploreFragment extends Fragment implements View.OnClickListener {
    FragmentExploreBinding binding;
    final String TAG = "Dictionary Importing";
    RecyclerView recyclerView;
    RelativeLayout emptyStateFiller;
    private List<ImportModel> sectionsList;
    private List<ImportModel> sectionsListFull;
    private ArrayList<String> checkedItems;
    private ImportAdapter adapter;

    private DatabaseHelper myDb;
    DatabaseReference dbLanguage;
    private String learningLanguageFiller, sectionNameFiller;

    ImageButton searchDictionary, filterDictionaries;

    String filterLanguage;
    String filterLevel;

    SharedPreferences sp;
    Button filterDictionariesFillerBtn;

    ArrayList<ExistingLanguageHelper> listToFilter = new ArrayList<>();
    // testas
    //private static final String BUNDLE_RECYCLER_LAYOUT = "recycler_layout";
    //private static String LIST_STATE = "list_state";
    //private Parcelable savedRecyclerLayoutState;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // creating an array to push it to the dialogFragment
        // input - listToFilter
        if (listToFilter.size() == 0) {
            Log.i(TAG, "onCreate: 1111111111");
            AsyncTaskClassLoadMetadata taskClassLoadMetadata = new AsyncTaskClassLoadMetadata(listToFilter, getContext());
            taskClassLoadMetadata.execute();
        }
    }

    // TODO create minimized recycle view where you tag libraries you want to import.
    // TODO add description of library + small descriptive list?

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentExploreBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


        sp = getActivity().getSharedPreferences("filters", Context.MODE_PRIVATE);

        Toolbar tb = root.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(tb);

        searchDictionary = root.findViewById(R.id.explore_search);

        searchDictionary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // implement it later
            }
        });

        filterDictionaries = root.findViewById(R.id.explore_filter);
        filterDictionaries.setOnClickListener(this);

        filterDictionariesFillerBtn = root.findViewById(R.id.explore_fragment_empty_filler_filter);
        filterDictionariesFillerBtn.setOnClickListener(this);

        setUpRecyclerView();


        // asycn task to load all existing languages with its respective count in the firebase database



        return root;
    }

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
                            , lng.getDescription()
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

            Log.i(TAG, "setUpRecyclerView: sectionList size: " + sectionsList.size());
            if (sectionsList.size() == 0) {
                recyclerView.setVisibility(View.GONE);
                emptyStateFiller.setVisibility(View.VISIBLE);

            } else {
                recyclerView.setVisibility(View.VISIBLE);
                emptyStateFiller.setVisibility(View.GONE);
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    };


    private void setUpRecyclerView() {
        // define layout layer that pops up a image in case we have an empty recycler
        emptyStateFiller = binding.getRoot().findViewById(R.id.explore_fragment_empty_filler_layout);

        recyclerView = binding.getRoot().findViewById(R.id.import_cloud_recycler_view);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        sectionsList = new ArrayList<>();
        sectionsListFull = new ArrayList<>();
        adapter = new ImportAdapter(sectionsList, getContext());
        recyclerView.setAdapter(adapter);

        Log.i(TAG, "setUpRecyclerView: sectionList size: " + sectionsList.size());
        if (sectionsList.size() == 0) {
            recyclerView.setVisibility(View.GONE);
            emptyStateFiller.setVisibility(View.VISIBLE);

        } else {
            recyclerView.setVisibility(View.VISIBLE);
            emptyStateFiller.setVisibility(View.GONE);
        }
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

    @Override
    public void onClick(View view) {
        changeStatusBarColor(false);

        FragmentManager fm = ExploreFragment.this.getParentFragmentManager();
        DialogFragment dialogFragment = ExploreFilterFragmentDialog.newInstance();
        dialogFragment.setTargetFragment(this,
                0);
        ((ExploreFilterFragmentDialog) dialogFragment).setData(listToFilter);
        dialogFragment.show(fm, "TAG");
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Make sure fragment codes match up
        Log.i(TAG, "onActivityResult:::" + requestCode);

        if (requestCode == 0) {
            changeStatusBarColor(true);


            filterLanguage = data.getStringExtra("language");
            filterLevel = data.getStringExtra("level");

            if (filterLanguage== null){
                recyclerView.setVisibility(View.GONE);
                emptyStateFiller.setVisibility(View.VISIBLE);
            } else {
                Log.i(TAG, "onActivityResult: " + filterLanguage + " : " + filterLanguage);

                sectionsList.clear();
                // execute a task that creats a small loading screen and then pops up
                AsyncTaskClassesGetCloudDictionaries taskClassesGetCloudDictionaries = new AsyncTaskClassesGetCloudDictionaries(dbLanguage, valueEventListener);

                String[] inputLanguageInfo = {filterLanguage, filterLevel};
                taskClassesGetCloudDictionaries.execute(inputLanguageInfo);

                Log.i(TAG, "onActivityResult: got language: " + filterLanguage + " and level: " + filterLevel);


            }
        }
    }

    public void changeStatusBarColor(Boolean isDismissed){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window =  getActivity().getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            if (isDismissed) {
                window.setStatusBarColor(Color.BLACK);
            } else {
                window.setStatusBarColor(Color.TRANSPARENT);
            }

        }
    }


}