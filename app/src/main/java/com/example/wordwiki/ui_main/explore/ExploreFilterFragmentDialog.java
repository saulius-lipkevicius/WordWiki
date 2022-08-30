package com.example.wordwiki.ui_main.explore;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.DialogFragment;

import com.example.wordwiki.R;
import com.example.wordwiki.ui_main.actionbar.setting.models.UserFeedbackNoStarsModel;
import com.example.wordwiki.ui_main.actionbar.setting.sub_settings.dialogs.FeedbackFragmentDialog;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class ExploreFilterFragmentDialog  extends DialogFragment implements View.OnClickListener{
    private static final String TAG = "fragment dialog for dictionary filtering";
    TextInputEditText inputText;
    TextView inputTextCounter;
    Button sendFeedback;
    private ExploreFilterFragmentDialog.Callback callback;

    AppCompatButton isA1, isA2, isB1, isB2, isC1, isC2;

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

        return view;
    }

    public void setUpLanguageRecycler(){

    }

    @Override
    public void onClick(View view) {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("general", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        int id = view.getId();

        switch (id){
            case R.id.toolbar_back_btn:
                //sendResultsSettings(0);
                dismiss();
                break;

            case R.id.fragment_explore_filter_submit_btn:
                dismiss();
                break;

            case R.id.fragment_explore_filter_dictionary_level_bar_a1:
                // make it clicked
                unselectDictionaryLevel();
                selectDictionaryLevel(1);

                // save choice to the SP
                editor.putString("filtered_level", "A1");
                editor.apply();
                break;

            case R.id.fragment_explore_filter_dictionary_level_bar_a2:
                // make it clicked
                unselectDictionaryLevel();
                selectDictionaryLevel(2);

                // save choice to the SP
                editor.putString("filtered_level", "A2");
                editor.apply();
                break;

            case R.id.fragment_explore_filter_dictionary_level_bar_b1:
                // make it clicked
                unselectDictionaryLevel();
                selectDictionaryLevel(3);

                // save choice to the SP
                editor.putString("filtered_level", "B1");
                editor.apply();
                break;

            case R.id.fragment_explore_filter_dictionary_level_bar_b2:
                // make it clicked
                unselectDictionaryLevel();
                selectDictionaryLevel(4);

                // save choice to the SP
                editor.putString("filtered_level", "B2");
                editor.apply();
                break;

            case R.id.fragment_explore_filter_dictionary_level_bar_c1:
                // make it clicked
                unselectDictionaryLevel();
                selectDictionaryLevel(5);

                // save choice to the SP
                editor.putString("filtered_level", "C1");
                editor.apply();
                break;

            case R.id.fragment_explore_filter_dictionary_level_bar_c2:
                // make it clicked
                unselectDictionaryLevel();
                selectDictionaryLevel(6);

                // save choice to the SP
                editor.putString("filtered_level", "C2");
                editor.apply();
                break;
        }
    }

    private void selectDictionaryLevel(int i) {
        Log.i(TAG, "selectDictionaryLevel: selected dictionary level: " + i);
        if (i == 1) {
            isA1.setBackgroundColor(Color.GRAY);
        } else if (i == 2) {
            isA2.setBackgroundColor(Color.GRAY);
        } else if (i == 3) {
            isB1.setBackgroundColor(Color.GRAY);
        } else if (i == 4) {
            isB2.setBackgroundColor(Color.GRAY);
        } else if (i == 5) {
            isC1.setBackgroundColor(Color.GRAY);
        } else if (i == 6) {
            isC2.setBackgroundColor(Color.GRAY);
        }
    }

    private void unselectDictionaryLevel() {
        isA1.setBackgroundColor(Color.TRANSPARENT);
        isA2.setBackgroundColor(Color.TRANSPARENT);
        isB1.setBackgroundColor(Color.TRANSPARENT);
        isB2.setBackgroundColor(Color.TRANSPARENT);
        isC1.setBackgroundColor(Color.TRANSPARENT);
        isC2.setBackgroundColor(Color.TRANSPARENT);
    }

    public  interface Callback {
        void onActionClick(String name);
    }

    public void sendResultsSettings(int requestCode) {
        // identify sender
        Intent intent = new Intent();
        intent.putExtra("isDismissed", true);

        getTargetFragment().onActivityResult(
                getTargetRequestCode(), 0, intent);
    }

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
