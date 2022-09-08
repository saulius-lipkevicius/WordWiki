package com.example.wordwiki.ui_main.profile;

import static android.content.ContentValues.TAG;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.example.wordwiki.R;
import com.example.wordwiki.database.DatabaseHelper;
import com.example.wordwiki.ui_main.actionbar.setting.SettingFragment;
import com.example.wordwiki.ui_main.home.adapters.DailyProgressAdapter;
import com.example.wordwiki.ui_main.home.classes.AsyncTaskClassGetStatistics;
import com.example.wordwiki.ui_main.library.adapters.SectionAdapter;
import com.example.wordwiki.ui_main.profile.adapters.ProgressSectionAdapter;
import com.example.wordwiki.ui_main.profile.models.ProgressSectionHelper;

import java.util.ArrayList;

public class FullScreenDialog extends DialogFragment implements View.OnClickListener{
    DatabaseHelper myDb;
    private Callback callback;

    // progress viewer and statistics
    ViewPager viewPager;
    DailyProgressAdapter viewAdapter;
    TextView viewpagerText;
    int mCurCheckPosition;
    LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);

    // tabs for the spinner
    TextView newWords, approachedWords, repeatedWords;

    public static FullScreenDialog newInstance() {
        return new FullScreenDialog();
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        return super.onCreateDialog(savedInstanceState);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.FullScreenDialogTheme);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_profile_fullscreen_dialog, container, false);

        myDb = new DatabaseHelper(getContext());

        Bundle bundle = getArguments();
        String dialogTitleText = bundle.getString("title","");
        int dialogFlag = bundle.getInt("flag", 0);

        ImageButton closeDialog = root.findViewById(R.id.dialog_back);
        TextView titleDialog = root.findViewById(R.id.dialog_title);
        ImageView flagDialog = root.findViewById(R.id.dialog_flag);

        closeDialog.setOnClickListener(this);
        titleDialog.setText(dialogTitleText);
        flagDialog.setImageResource(dialogFlag);


        // stats
        viewpagerText = root.findViewById(R.id.viewpager_text);
        viewPager = root.findViewById(R.id.view_pager);
        AsyncTaskClassGetStatistics taskClassGetStatistics = new AsyncTaskClassGetStatistics(viewPager, viewAdapter, getContext());
        taskClassGetStatistics.execute(mCurCheckPosition);


        String[] stats = getResources().getStringArray(R.array.home_stats);
        ArrayAdapter statsAdapter = new ArrayAdapter(requireContext(), R.layout.home_dropdown_stats, stats);

        AutoCompleteTextView spinnerText = root.findViewById(R.id.viewpager_text);
        spinnerText.setAdapter(statsAdapter);

        /*
        spinnerText.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                viewAdapter = new DailyProgressAdapter(getContext(), position);
                viewPager.setCurrentItem(position);
                viewPager.setAdapter(viewAdapter);

                mCurCheckPosition = position;
            }
        });

         */
        spinnerText.setBackground(getResources().getDrawable(R.drawable.home_fragment_selector));


        newWords = root.findViewById(R.id.words_new);
        newWords.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewAdapter = new DailyProgressAdapter(getContext(), 2);
                viewPager.setCurrentItem(2);
                viewPager.setAdapter(viewAdapter);

                mCurCheckPosition = 2;

                newWords.setTextColor(getResources().getColor(R.color.white));
                newWords.setBackground(getResources().getDrawable(R.drawable.fragment_home_tab_clicked_bg));

                approachedWords.setTextColor(getResources().getColor(R.color.black));
                approachedWords.setBackground(getResources().getDrawable(R.drawable.fragment_home_tab_bg));

                repeatedWords.setTextColor(getResources().getColor(R.color.black));
                repeatedWords.setBackground(getResources().getDrawable(R.drawable.fragment_home_tab_bg));
            }
        });

        approachedWords = root.findViewById(R.id.words_approached);
        approachedWords.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewAdapter = new DailyProgressAdapter(getContext(), 1);
                viewPager.setCurrentItem(1);
                viewPager.setAdapter(viewAdapter);

                mCurCheckPosition = 1;

                approachedWords.setTextColor(getResources().getColor(R.color.white));
                approachedWords.setBackground(getResources().getDrawable(R.drawable.fragment_home_tab_clicked_bg));

                repeatedWords.setTextColor(getResources().getColor(R.color.black));
                repeatedWords.setBackground(getResources().getDrawable(R.drawable.fragment_home_tab_bg));

                newWords.setTextColor(getResources().getColor(R.color.black));
                newWords.setBackground(getResources().getDrawable(R.drawable.fragment_home_tab_bg));
            }
        });

        repeatedWords = root.findViewById(R.id.words_repeated);
        repeatedWords.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewAdapter = new DailyProgressAdapter(getContext(), 0);
                viewPager.setCurrentItem(0);
                viewPager.setAdapter(viewAdapter);

                mCurCheckPosition = 0;

                repeatedWords.setTextColor(getResources().getColor(R.color.white));
                repeatedWords.setBackground(getResources().getDrawable(R.drawable.fragment_home_tab_clicked_bg));

                approachedWords.setTextColor(getResources().getColor(R.color.black));
                approachedWords.setBackground(getResources().getDrawable(R.drawable.fragment_home_tab_bg));

                newWords.setTextColor(getResources().getColor(R.color.black));
                newWords.setBackground(getResources().getDrawable(R.drawable.fragment_home_tab_bg));
            }
        });



        // recycler view for section stats
        ArrayList<ProgressSectionHelper> sectionList = new ArrayList<>();


        sectionList.add(new ProgressSectionHelper("introduction", 11, 50));
        sectionList.add(new ProgressSectionHelper("introduction2", 11, 60));

        RecyclerView sectionStatsRecycler = root.findViewById(R.id.stats_recycler);
        ProgressSectionAdapter sectionAdapter = new ProgressSectionAdapter(sectionList, getContext());
        sectionStatsRecycler.setLayoutManager(layoutManager);
        sectionStatsRecycler.setAdapter(sectionAdapter);

        return root;
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        switch (id){
            case R.id.dialog_back:
                Log.i(TAG, "onClick: dialogFragment dialog_back");
                sendResultsSettings(5);
                Log.i(TAG, "onClick: sending request 5");
                dismiss();
                Log.i(TAG, "onClick: dismissing");
                break;

                /*
                case R.id.dialog_back:
                callback.onActionClick("This is used to communicate with the parent fragment")

                break;

                 */
        }
    }


    public  interface Callback {

        void onActionClick(String name);
    }

    public void sendResultsSettings(int requestCode) {
        // identify sender
        Intent intent = new Intent();
        intent.putExtra("isDismissed", true);
        if (requestCode == 6) {
            intent.putExtra("isSnack", true);
        }

        Log.i(TAG, "sendResultsSettings: results will be send");
        getTargetFragment().onActivityResult(
                getTargetRequestCode(), 5, intent);
        Log.i(TAG, "sendResultsSettings: results are sent");
    }
}
