package com.example.wordwiki.ui_main.explore;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wordwiki.R;
import com.example.wordwiki.ui_main.explore.adapters.ExistingLanguageAdapter;
import com.example.wordwiki.ui_main.explore.models.ExistingLanguageHelper;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class ExploreFilterFragmentDialog  extends DialogFragment implements View.OnClickListener{
    private static final String TAG = "fragment dialog for dictionary filtering";
    TextInputEditText inputText;
    TextView inputTextCounter;
    Button sendFeedback;
    private ExploreFilterFragmentDialog.Callback callback;

    AppCompatButton isA1, isA2, isB1, isB2, isC1, isC2;

    // array list for languages that are selected in the filter
    Set<String> selectedLanguageList = new HashSet<String>();
    Set<String> selectedLevelList = new HashSet<String>();

    ArrayList<ExistingLanguageHelper> languageList= new ArrayList<>();
    RecyclerView filteringRecycler;
    LinearLayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
    ExistingLanguageAdapter adapter;

    SharedPreferences sp;


    // this is for passing an arraylist to the dialogFragment
    private ArrayList<ExistingLanguageHelper> list;
    // testas
    private Bundle savedState = null;
    private TextView vstup;

    public void setData(ArrayList<ExistingLanguageHelper> list){
        this.list = list;
    }

    public static ExploreFilterFragmentDialog newInstance() {
        return new ExploreFilterFragmentDialog();
    }

    public void setCallback(ExploreFilterFragmentDialog.Callback callback) {
        this.callback = callback;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.FullScreenDialogTheme);
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_explore_filter_dialog, container, false);

        Log.i(TAG, "onCreateView: testing: " + list.size() + " " + list.get(0).getLanguageName() + " " + list.get(1).getLanguageName());


        // to get the intended resize when we have focus on the text field
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        // close the dialogFragment by connecting it to the listener which finds id of item pressed
        ImageButton closeDialog = view.findViewById(R.id.toolbar_back_btn);
        closeDialog.setOnClickListener(this);

        Button removeFilters = view.findViewById(R.id.fragment_explore_filter_clear_btn);
        removeFilters.setOnClickListener(this);

        Button searchDictionaries = view.findViewById(R.id.fragment_explore_filter_submit_btn);
        searchDictionaries.setOnClickListener(this);

        isA1 = view.findViewById(R.id.fragment_explore_filter_dictionary_level_bar_a1);
        isA2 = view.findViewById(R.id.fragment_explore_filter_dictionary_level_bar_a2);
        isB1 = view.findViewById(R.id.fragment_explore_filter_dictionary_level_bar_b1);
        isB2 = view.findViewById(R.id.fragment_explore_filter_dictionary_level_bar_b2);
        isC1 = view.findViewById(R.id.fragment_explore_filter_dictionary_level_bar_c1);
        isC2 = view.findViewById(R.id.fragment_explore_filter_dictionary_level_bar_c2);

        isA1.setOnClickListener(this);
        isA2.setOnClickListener(this);
        isB1.setOnClickListener(this);
        isB2.setOnClickListener(this);
        isC1.setOnClickListener(this);
        isC2.setOnClickListener(this);

        // remember states of it
        sp = getActivity().getSharedPreferences("filters", MODE_PRIVATE);

        if (sp.getBoolean("A1", false)){
            isA1.setBackgroundColor(Color.GRAY);
        } else {
            isA1.setBackgroundColor(Color.TRANSPARENT);
        }

        if (sp.getBoolean("A2", false)){
            isA2.setBackgroundColor(Color.GRAY);
        } else {
            isA2.setBackgroundColor(Color.TRANSPARENT);
        }

        if (sp.getBoolean("B1", false)){
            isB1.setBackgroundColor(Color.GRAY);
        } else {
            isB1.setBackgroundColor(Color.TRANSPARENT);
        }

        if (sp.getBoolean("B2", false)){
            isB2.setBackgroundColor(Color.GRAY);
        } else {
            isB2.setBackgroundColor(Color.TRANSPARENT);
        }

        if (sp.getBoolean("C1", false)){
            isC1.setBackgroundColor(Color.GRAY);
        } else {
            isC1.setBackgroundColor(Color.TRANSPARENT);
        }

        if (sp.getBoolean("C2", false)){
            isC2.setBackgroundColor(Color.GRAY);
        } else {
            isC2.setBackgroundColor(Color.TRANSPARENT);
        }

        // create a single value to trace if it was visited for a first time

        languageList = list;
        filteringRecycler = view.findViewById(R.id.fragment_explore_filter_dialog_language_recycler);
        adapter = new ExistingLanguageAdapter(languageList, getContext(), this::onProgressListClick);
        setUpLanguageRecycler();



        return view;
    }

    public void setUpLanguageRecycler(){
        // Async to load up language with its number

        filteringRecycler.setLayoutManager(layoutManager);
        filteringRecycler.setAdapter(adapter);

 /*
        DatabaseReference referenceProfile = FirebaseDatabase.getInstance("https://wordwiki-af0d4-default-rtdb.europe-west1.firebasedatabase.app").getReference("Metadate");
        referenceProfile.child("ExistingLanguages").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot language : snapshot.getChildren()) {
                    // create an unique language list of possible dictionaries to choose from
                    //languageList.add(language.getKey());
                    String languageText = language.getKey();

                    Log.i(TAG, "onDataChange: 1: " + language.getValue());


                    long dictionaryCount = (long) language.getValue();
                    int dictionaryCountInt = (int) dictionaryCount;
                    languageList.add(new ExistingLanguageHelper(languageText, dictionaryCountInt));
                    Log.i(TAG, "onDataChange: added: " + languageText);
                }

                filteringRecycler.setLayoutManager(layoutManager);
                filteringRecycler.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });

  */
    }

    private void onProgressListClick(int i, Boolean isSelected) {
        Log.i(TAG, "onProgressListClick:  huhuhuhu " + languageList.get(i).getLanguageName() + " and the bool is: " + isSelected);

        if (isSelected){
            selectedLanguageList.add(languageList.get(i).getLanguageName());
        } else {
            selectedLanguageList.remove(languageList.get(i).getLanguageName());
        }

        Log.i(TAG, "onProgressListClick:  huhuhuhu " + selectedLanguageList);
    }


    @Override
    public void onClick(View view) {
        sp = requireContext().getSharedPreferences("filters", MODE_PRIVATE);
        int id = view.getId();

        switch (id){
            case R.id.toolbar_back_btn:
                //sendResultsSettings(0);
                dismiss();

                // send results back
                sendResultsSettings(0);


                //sp.edit().clear().apply();
                break;

            case R.id.fragment_explore_filter_clear_btn:
                Log.i(TAG, "onClick: clear filters button is pressed");
                
                // remove language
                selectedLanguageList.clear();
                selectedLevelList.clear();

                sp.edit().clear().apply();

                // unselect all levels
                selectDictionaryLevel(0);
                // refresh language part
                filteringRecycler.setAdapter(adapter);
                break;

            case R.id.fragment_explore_filter_submit_btn:
                dismiss();

                // send results
                sendResultsSettings(0);

                //sp.edit().clear().apply();
                break;

            case R.id.fragment_explore_filter_dictionary_level_bar_a1:
                selectDictionaryLevel(1);

                break;

            case R.id.fragment_explore_filter_dictionary_level_bar_a2:
                selectDictionaryLevel(2);

                break;

            case R.id.fragment_explore_filter_dictionary_level_bar_b1:
                selectDictionaryLevel(3);

                break;

            case R.id.fragment_explore_filter_dictionary_level_bar_b2:
                selectDictionaryLevel(4);

                break;

            case R.id.fragment_explore_filter_dictionary_level_bar_c1:
                selectDictionaryLevel(5);

                break;

            case R.id.fragment_explore_filter_dictionary_level_bar_c2:
                selectDictionaryLevel(6);

                break;
        }

    }

    private void selectDictionaryLevel(int i) {
        Log.i(TAG, "selectDictionaryLevel: selected dictionary level: " + i);

        SharedPreferences sp = getActivity().getSharedPreferences("filters", MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();



        if (i == 1) {
            if (sp.getBoolean("A1", false)) {
                isA1.setBackgroundColor(Color.TRANSPARENT);
                editor.putBoolean("A1", false);

                selectedLevelList.remove("A1");
            } else {
                isA1.setBackgroundColor(Color.GRAY);
                editor.putBoolean("A1", true);

                selectedLevelList.add("A1");
            }

        } else if (i == 2) {
            if (sp.getBoolean("A2", false)) {
                isA2.setBackgroundColor(Color.TRANSPARENT);
                editor.putBoolean("A2", false);

                selectedLevelList.remove("A2");
            } else {
                isA2.setBackgroundColor(Color.GRAY);
                editor.putBoolean("A2", true);

                selectedLevelList.add("A2");
            }


        } else if (i == 3) {
            if (sp.getBoolean("B1", false)) {
                isB1.setBackgroundColor(Color.TRANSPARENT);
                editor.putBoolean("B1", false);

                selectedLevelList.remove("B1");
            } else {
                isB1.setBackgroundColor(Color.GRAY);
                editor.putBoolean("B1", true);

                selectedLevelList.add("B1");
            }

        } else if (i == 4) {
            if (sp.getBoolean("B2", false)) {
                isB2.setBackgroundColor(Color.TRANSPARENT);
                editor.putBoolean("B2", false);

                selectedLevelList.remove("B2");
            } else {
                isB2.setBackgroundColor(Color.GRAY);
                editor.putBoolean("B2", true);

                selectedLevelList.add("B2");
            }

        } else if (i == 5) {
            if (sp.getBoolean("C1", false)) {
                isC1.setBackgroundColor(Color.TRANSPARENT);
                editor.putBoolean("C1", false);

                selectedLevelList.remove("C1");
            } else {
                isC1.setBackgroundColor(Color.GRAY);
                editor.putBoolean("C1", true);

                selectedLevelList.add("C1");
            }

        } else if (i == 6) {
            if (sp.getBoolean("C2", false)) {
                isC2.setBackgroundColor(Color.TRANSPARENT);
                editor.putBoolean("C2", false);

                selectedLevelList.remove("C2");
            } else {
                isC2.setBackgroundColor(Color.GRAY);
                editor.putBoolean("C2", true);

                selectedLevelList.add("C2");
            }
        } else if (i == 0) {
            isA1.setBackgroundColor(Color.TRANSPARENT);
            isA2.setBackgroundColor(Color.TRANSPARENT);
            isB1.setBackgroundColor(Color.TRANSPARENT);
            isB2.setBackgroundColor(Color.TRANSPARENT);
            isC1.setBackgroundColor(Color.TRANSPARENT);
            isC2.setBackgroundColor(Color.TRANSPARENT);
        }

        editor.apply();

        Log.i(TAG, "selectDictionaryLevel: xxxxx " + selectedLevelList);
    }


    public  interface Callback {
        void onActionClick(String name);
    }

    public void sendResultsSettings(int requestCode) {
        if (!selectedLevelList.isEmpty() && !selectedLanguageList.isEmpty()){
            // identify sender
            Intent intent = new Intent();
            intent.putExtra("level",  selectedLevelList.iterator().next());
            intent.putExtra("language", selectedLanguageList.iterator().next());


            getTargetFragment().onActivityResult(
                    getTargetRequestCode(), 0, intent);
        } else {
            getTargetFragment().onActivityResult(
                    getTargetRequestCode(), 0, new Intent());
        }
    }

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
