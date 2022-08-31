package com.example.wordwiki.ui_main.explore;

import static android.content.ContentValues.TAG;
import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcelable;
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
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blongho.country_data.World;
import com.example.wordwiki.R;
import com.example.wordwiki.ui_main.explore.adapters.ExistingLanguageAdapter;
import com.example.wordwiki.ui_main.explore.models.ExistingLanguageHelper;
import com.example.wordwiki.ui_main.profile.FullScreenDialog;
import com.example.wordwiki.ui_main.profile.ProfileFragment;
import com.example.wordwiki.ui_main.profile.adapters.progressAdapter;
import com.example.wordwiki.ui_main.profile.models.progressHelper;
import com.facebook.share.Share;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.openxmlformats.schemas.wordprocessingml.x2006.main.impl.STHexColorRGBImpl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
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

        // to get the intended resize when we have focus on the text field
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        // close the dialogFragment by connecting it to the listener which finds id of item pressed
        ImageButton closeDialog = view.findViewById(R.id.toolbar_back_btn);
        closeDialog.setOnClickListener(this);

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

        filteringRecycler = view.findViewById(R.id.fragment_explore_filter_dialog_language_recycler);
        setUpLanguageRecycler();


        adapter = new ExistingLanguageAdapter(languageList, getContext(), this::onProgressListClick);
        return view;
    }

    public void setUpLanguageRecycler(){


        DatabaseReference referenceProfile = FirebaseDatabase.getInstance("https://wordwiki-af0d4-default-rtdb.europe-west1.firebasedatabase.app").getReference("Metadate");
        referenceProfile.child("ExistingLanguages").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot language : snapshot.getChildren()) {
                    // create an unique language list of possible dictionaries to choose from
                    //languageList.add(language.getKey());
                    String languageText = language.getKey();
                    languageList.add(new ExistingLanguageHelper(languageText));
                    Log.i(TAG, "onDataChange: added: " + languageText);
                }

                filteringRecycler.setLayoutManager(layoutManager);
                filteringRecycler.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });

        // set the recycler itself



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

        int id = view.getId();

        switch (id){
            case R.id.toolbar_back_btn:
                //sendResultsSettings(0);
                dismiss();

                // send results back
                sendResultsSettings(0);
                break;

            case R.id.fragment_explore_filter_submit_btn:
                dismiss();

                // send results
                sendResultsSettings(0);
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
        }

        editor.apply();

        Log.i(TAG, "selectDictionaryLevel: xxxxx " + selectedLevelList);
    }


    public  interface Callback {
        void onActionClick(String name);
    }

    public void sendResultsSettings(int requestCode) {
        // identify sender
        Intent intent = new Intent();
        intent.putExtra("level",  selectedLevelList.iterator().next());
        intent.putExtra("language", selectedLanguageList.iterator().next());


        getTargetFragment().onActivityResult(
                getTargetRequestCode(), 0, intent);
    }

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

}
