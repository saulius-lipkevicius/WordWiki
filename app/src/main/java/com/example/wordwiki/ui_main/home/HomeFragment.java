package com.example.wordwiki.ui_main.home;

import static android.content.Context.MODE_PRIVATE;
import static com.facebook.FacebookSdk.getApplicationContext;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.blongho.country_data.World;
import com.example.wordwiki.R;
import com.example.wordwiki.database.DatabaseHelper;
import com.example.wordwiki.databinding.FragmentHomeBinding;
import com.example.wordwiki.ui_main.actionbar.notification.NotificationFragment;
import com.example.wordwiki.ui_main.home.adapters.LanguageSelectionRecycleAdapter;
import com.example.wordwiki.ui_main.home.adapters.ModeSelectionRecycleAdapter;
import com.example.wordwiki.ui_main.home.adapters.SectionSelectionRecycleAdapter;
import com.example.wordwiki.ui_main.home.adapters.dailyProgressAdapter;
import com.example.wordwiki.ui_main.home.models.LanguageSelectionModel;
import com.example.wordwiki.ui_main.home.models.ModeSelectionModel;
import com.example.wordwiki.ui_main.home.models.SectionSelectionModel;
import com.facebook.share.Share;
import com.tbuonomo.viewpagerdotsindicator.SpringDotsIndicator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

public class HomeFragment extends Fragment {
    private static final String TAG = "Home Fragment";

    DatabaseHelper myDb;

    List<SectionSelectionModel> section_list = new ArrayList<>();
    SectionSelectionRecycleAdapter section_adapter;
    List<LanguageSelectionModel> language_list = new ArrayList<>();
    LanguageSelectionRecycleAdapter language_adapter;
    List<ModeSelectionModel> mode_list = new ArrayList<>();
    ModeSelectionRecycleAdapter mode_adapter;

    // progress viewer and statistics
    ViewPager viewPager;
    SpringDotsIndicator springDotsIndicator;
    dailyProgressAdapter viewAdapter;

    Button to_wording_activity_btn, choose_language_btn, choose_section_btn, choose_mode_btn;
    Button dialog_none_btn, dialog_all_btn, dialog_ok_btn;

    TextView viewpagerText;

    int mCurCheckPosition;

    private FragmentHomeBinding binding;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onStart() {
        super.onStart();
        setUpActionBarLinks();
    }

    @Override
    public void onResume() {
        super.onResume();
        String[] stats = getResources().getStringArray(R.array.home_stats);
        ArrayAdapter statsAdapter = new ArrayAdapter(requireContext(), R.layout.home_dropdown_stats, stats);

        AutoCompleteTextView spinnerText = binding.viewpagerText;
        spinnerText.setAdapter(statsAdapter);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        HomeViewModel homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        myDb = new DatabaseHelper(getContext());

        World.init(getApplicationContext());

//      Configurate dialogs for language/mode/section selection
        choose_section_btn = root.findViewById(R.id.btnChooseLearningSection);
        choose_language_btn = root.findViewById(R.id.btnChooseLearningLanguage);
        choose_mode_btn = root.findViewById(R.id.btnChooseLearningMode);

        viewpagerText = root.findViewById(R.id.viewpager_text);

        // daily progress following up\]
        viewPager = root.findViewById(R.id.view_pager);
        //springDotsIndicator = root.findViewById(R.id.dot1);

        //springDotsIndicator.setViewPager(viewPager);


        checkFilePermissions();
        setupSharedPreferences();

        chooseModeDialog();
        chooseLanguageDialog();
        chooseSectionDialog();


        viewAdapter = new dailyProgressAdapter(getContext(), mCurCheckPosition);
        viewPager.setCurrentItem(mCurCheckPosition);
        viewPager.setAdapter(viewAdapter);

        String[] stats = getResources().getStringArray(R.array.home_stats);
        ArrayAdapter statsAdapter = new ArrayAdapter(requireContext(), R.layout.home_dropdown_stats, stats);

        AutoCompleteTextView spinnerText = root.findViewById(R.id.viewpager_text);
        spinnerText.setAdapter(statsAdapter);
        spinnerText.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                viewAdapter = new dailyProgressAdapter(getContext(), position);
                viewPager.setCurrentItem(position);
                viewPager.setAdapter(viewAdapter);

                mCurCheckPosition = position;
            }
        });


        spinnerText.setBackground(getResources().getDrawable(R.drawable.home_fragment_selector));


        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("general", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("username", "saulius");
        editor.commit();


        return root;
    }


    private void setUpActionBarLinks() {
        Button toPlayBtn = getActivity().findViewById(R.id.button_to_second_activity);
        toPlayBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavController navController = Navigation.findNavController(view);
                navController.navigate(R.id.action_navigation_home_to_navigation_play);
            }
        });

        ImageButton toNotification = getActivity().findViewById(R.id.home_notification);
        toNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavController navController = Navigation.findNavController(view);
                navController.navigate(R.id.action_navigation_home_to_navigation_notification);
            }
        });

    }

    private void chooseModeDialog() {
        choose_mode_btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(final View view) {
                addModeData();
                final AlertDialog.Builder mode_mBuilder = new AlertDialog.Builder(getContext());
                LayoutInflater mode_inflater = getLayoutInflater();
                View convertView = mode_inflater.inflate(R.layout.home_mode_selection_alert_dialog, null);
                mode_adapter = new ModeSelectionRecycleAdapter(getContext(), mode_list, mode_list.size());
                RecyclerView mode_list = convertView.findViewById(R.id.recycleView);
                mode_list.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayout.VERTICAL));

                mode_list.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                mode_list.setAdapter(mode_adapter);
                mode_mBuilder.setView(convertView);

                AlertDialog mode_mDialog = mode_mBuilder.create();
                mode_mDialog.getWindow().setWindowAnimations(R.style.DialogAnimation);
                mode_mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                mode_mDialog.show();

                dialog_none_btn = convertView.findViewById(R.id.cancel_all);
                dialog_all_btn = convertView.findViewById(R.id.select_all);
                dialog_ok_btn = convertView.findViewById(R.id.ok);

                dialog_none_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("entrance dialogs", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putBoolean("mode_2", true);
                        editor.apply();

                        mode_adapter.unselectAll();
                    }
                });


                dialog_all_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("entrance dialogs", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putBoolean("mode_2", true);
                        editor.apply();
                        mode_adapter.selectAll();
                    }
                });

                dialog_ok_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // Check if all values are false and if it is, give a message
                        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("Modes", MODE_PRIVATE);

                        if (!sharedPreferences.getAll().containsValue(true)) {
                            toastMessage("You have to select at least one Language");
                        } else {
                            mode_mDialog.dismiss();
                        }

                    }
                });
            }
        });
    }

    private void chooseLanguageDialog() {
        choose_language_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                addLanguageData();
                final AlertDialog.Builder language_mBuilder = new AlertDialog.Builder(getContext());
                LayoutInflater language_inflater = getLayoutInflater();
                View convertView = language_inflater.inflate(R.layout.home_language_selection_alert_dialog, null);
                language_adapter = new LanguageSelectionRecycleAdapter(getContext(), language_list, language_list.size());
                RecyclerView language_list = convertView.findViewById(R.id.recycleView);
                language_list.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayout.VERTICAL));

                language_list.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                language_list.setAdapter(language_adapter);
                language_mBuilder.setView(convertView);

                AlertDialog language_mDialog = language_mBuilder.create();
                language_mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                language_mDialog.getWindow().setWindowAnimations(R.style.DialogAnimation);
                language_mDialog.show();

                dialog_none_btn = convertView.findViewById(R.id.cancel_all);
                dialog_all_btn = convertView.findViewById(R.id.select_all);
                dialog_ok_btn = convertView.findViewById(R.id.ok);

                dialog_none_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("entrance dialogs", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putBoolean("language_2", true);
                        editor.apply();

                        language_adapter.unselectAll();
                    }
                });

                dialog_all_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("entrance dialogs", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putBoolean("language_2", true);
                        editor.apply();
                        language_adapter.selectAll();
                    }
                });

                dialog_ok_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // Check if all values are false and if it is, give a message
                        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("Languages", MODE_PRIVATE);

                        if (!sharedPreferences.getAll().containsValue(true)) {
                            toastMessage("You have to select at least one Language");
                        } else {
                            language_mDialog.dismiss();
                        }

                        // Refresh data
                        section_list.clear();
                        addSectionData();
                    }
                });
            }
        });
    }

    private void chooseSectionDialog() {
        choose_section_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                addSectionData();
                final AlertDialog.Builder section_mBuilder = new AlertDialog.Builder(getContext());
                LayoutInflater section_inflater = getLayoutInflater();
                View convertView = section_inflater.inflate(R.layout.home_section_selection_alert_dialog, null);
                section_adapter = new SectionSelectionRecycleAdapter(getContext(), section_list, section_list.size());
                RecyclerView section_list = convertView.findViewById(R.id.recycleView);
                section_list.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayout.VERTICAL));

                section_list.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                section_list.setAdapter(section_adapter);
                section_mBuilder.setView(convertView);

                AlertDialog section_mDialog = section_mBuilder.create();
                section_mDialog.getWindow().setWindowAnimations(R.style.DialogAnimation);
                section_mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                section_mDialog.show();

                dialog_none_btn = convertView.findViewById(R.id.cancel_all);
                dialog_all_btn = convertView.findViewById(R.id.select_all);
                dialog_ok_btn = convertView.findViewById(R.id.ok);

                dialog_none_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("entrance dialogs", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putBoolean("section_2", true);
                        editor.apply();

                        section_adapter.unselectAll();
                    }
                });

                dialog_all_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("entrance dialogs", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putBoolean("section_2", true);
                        editor.apply();
                        section_adapter.selectAll();
                    }
                });

                dialog_ok_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // Check if all values are false and if it is, give a message
                        SharedPreferences sharedPreferences_lng = getActivity().getSharedPreferences("Languages", MODE_PRIVATE);
                        SharedPreferences sharedPreferences_sec = getActivity().getSharedPreferences("Sections", MODE_PRIVATE);

                        Boolean true_exists = false;

                        Cursor section_cursor = myDb.getAllSections();

                        if (section_cursor == null)
                            return; // can't do anything with a null cursor.
                        try {
                            if (sharedPreferences_lng.getBoolean(section_cursor.getString(0), false)) {
                                if (sharedPreferences_sec.getBoolean(section_cursor.getString(0) + section_cursor.getString(1), false)) {
                                    true_exists = true;
                                }
                            }

                            while (section_cursor.moveToNext()) {
                                if (sharedPreferences_lng.getBoolean(section_cursor.getString(0), false)) {
                                    if (sharedPreferences_sec.getBoolean(section_cursor.getString(0) + section_cursor.getString(1), false)) {
                                        true_exists = true;
                                    }
                                }
                            }
                        } finally {
                            section_cursor.close();
                        }

                        if (!true_exists) {
                            toastMessage("You have to select at least one Section");
                        } else {
                            section_mDialog.dismiss();
                        }
                    }
                });

            }
        });
    }

    private void addSectionData() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("Languages", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        // get all existing languages
        // TODO create Section column and respective functions to it
        Cursor section_cursor = myDb.getAllSections();
        section_list.clear();
        if (section_cursor == null)
            return; // can't do anything with a null cursor.
        try {
            if (sharedPreferences.getBoolean(section_cursor.getString(0), false)) {
                String country_name = myDb.getFlagISO(section_cursor.getString(0));
                int flag = World.getFlagOf(country_name);

                Boolean input_value_1 = sharedPreferences.getBoolean(section_cursor.getString(1) + section_cursor.getString(0), false);
                section_list.add(new SectionSelectionModel(section_cursor.getString(1), section_cursor.getString(0), true, flag));
            }

            while (section_cursor.moveToNext()) {
                if (sharedPreferences.getBoolean(section_cursor.getString(0), false)) {
                    String country_name = myDb.getFlagISO(section_cursor.getString(0));
                    int flag = World.getFlagOf(country_name);

                    Boolean input_value = sharedPreferences.getBoolean(section_cursor.getString(1) + section_cursor.getString(0), false);
                    section_list.add(new SectionSelectionModel(section_cursor.getString(1), section_cursor.getString(0), true, flag));
                }
            }
        } finally {
            section_cursor.close();
        }

        Collections.sort(section_list, new Comparator<SectionSelectionModel>() {
            @Override
            public int compare(SectionSelectionModel lhs, SectionSelectionModel rhs) {
                return lhs.getLanguageSection().compareTo(rhs.getLanguageSection());
            }
        });

        editor.apply();
    }

    private void addLanguageData() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("Languages", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        language_list.clear();
        // get all existing languages
        // TODO create Section column and respective functions to it
        Cursor languages_cursor = myDb.getAllLanguages();

        if (languages_cursor == null)
            return; // can't do anything with a null cursor.
        try {
            String country_name = myDb.getFlagISO(languages_cursor.getString(0));
            int flag = World.getFlagOf(country_name);

            Boolean input_value_1 = sharedPreferences.getBoolean(languages_cursor.getString(0), false);
            language_list.add(new LanguageSelectionModel(languages_cursor.getString(0), input_value_1, flag));

            while (languages_cursor.moveToNext()) {
                country_name = myDb.getFlagISO(languages_cursor.getString(0));
                flag = World.getFlagOf(country_name);

                Boolean input_value = sharedPreferences.getBoolean(languages_cursor.getString(0), false);
                language_list.add(new LanguageSelectionModel(languages_cursor.getString(0), input_value, flag));
            }
        } finally {
            languages_cursor.close();
        }

        Collections.sort(language_list, new Comparator<LanguageSelectionModel>() {
            @Override
            public int compare(LanguageSelectionModel lhs, LanguageSelectionModel rhs) {
                return lhs.getLanguageName().compareTo(rhs.getLanguageName());
            }
        });
        editor.apply();

    }

    private void addModeData() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("Modes", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        mode_list.clear();
        boolean input_value = sharedPreferences.getBoolean("Classical", false);
        mode_list.add(new ModeSelectionModel("Classical", "First foreign word is shown", input_value));

        boolean input_value2 = sharedPreferences.getBoolean("Swapped", false);
        mode_list.add(new ModeSelectionModel("Swapped", "Known word is shown first", input_value2));

        boolean input_value3 = sharedPreferences.getBoolean("Article", false);
        mode_list.add(new ModeSelectionModel("Article", "You guess what article is correct", input_value3));

        boolean input_value4 = sharedPreferences.getBoolean("Preposition", false);
        mode_list.add(new ModeSelectionModel("Preposition", "Learn most common verb preposition combinations", input_value4));

        boolean input_value5 = sharedPreferences.getBoolean("Idiom", false);
        mode_list.add(new ModeSelectionModel("Idiom", "Master vivid idioms", input_value5));

        /*
        Collections.sort(mode_list, new Comparator<ModeSelectionModel>() {
            @Override
            public int compare(ModeSelectionModel lhs, ModeSelectionModel rhs) {
                return lhs.getModeName().compareTo(rhs.getModeName());
            }
        });
         */

    }

    /**
     * Setup all preferences that could be needed for initial work app has to do: settings preferences, color, text size, etc
     */
    public void setupSharedPreferences() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());

        //sharedPreferences.getBoolean(getString(R.string.pref_night_mode_key), getResources().getBoolean(R.bool.pref_night_mode));
        //sharedPreferences.getBoolean(getString(R.string.pref_add_comment_key), getResources().getBoolean(R.bool.pref_add_comment_default));
        //sharedPreferences.getBoolean(getString(R.string.pref_ad_notification_key), getResources().getBoolean(R.bool.pref_ad_notification_default));
        //loadColorFromPreferences(sharedPreferences);
        //loadSizeFromSharedPreferences(sharedPreferences);

        //sharedPreferences.registerOnSharedPreferenceChangeListener(this);
    }

    /*
    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(getString(R.string.pref_add_comment_key))) {
            sharedPreferences.getBoolean(getString(R.string.pref_add_comment_key), getResources().getBoolean(R.bool.pref_add_comment_default));
        } else if (key.equals(getString(R.string.pref_ad_notification_key))) {
            sharedPreferences.getBoolean(getString(R.string.pref_ad_notification_key), getResources().getBoolean(R.bool.pref_ad_notification_default));
        } else if (key.equals(getString(R.string.pref_color_key))) {
            loadColorFromPreferences(sharedPreferences);
        } else if (key.equals(getString(R.string.pref_size_key))) {
            loadSizeFromSharedPreferences(sharedPreferences);
        }
    }



    private void loadSizeFromSharedPreferences(SharedPreferences sharedPreferences) {
        float minSize = Float.parseFloat(sharedPreferences.getString(getString(R.string.pref_size_key),
                getString(R.string.pref_size_default)));
    }

    private void loadColorFromPreferences(SharedPreferences sharedPreferences) {
        sharedPreferences.getString(getString(R.string.pref_color_key),
                getString(R.string.pref_color_red_value));
    }

     */

    public void changeModePreference(String inputString) {//ModePreference for changing types of learning
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("general", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("Mode", inputString);
        editor.apply();
    }

    public String getModePreference() {//ModePreference for changing types of learning
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("general", MODE_PRIVATE);
        String obtainedMode = sharedPreferences.getString("Mode", "Classical"); //second argument is default, if your request is empty

        return obtainedMode;
    }//preference


    public String getLanguage() {
        SharedPreferences sharedPreferencesNames = getActivity().getSharedPreferences("Languages", MODE_PRIVATE);
        Iterator<?> language_bool_for_commas = sharedPreferencesNames.getAll().values().iterator();
        Iterator<?> language_bool = sharedPreferencesNames.getAll().values().iterator();
        Iterator<String> language_name = sharedPreferencesNames.getAll().keySet().iterator();

        String inClause = "(";
        int i = 0;
        while (language_bool.hasNext()) {
            if (language_bool.next().toString().equals("true") && i == 0) {

                inClause = inClause + "'" + language_name.next() + "'";
                i++;
            } else if (language_bool_for_commas.next().toString().equals("true") && i > 0) {
                inClause = inClause + ", '" + language_name.next() + "'";
            } else {
                language_name.next();
            }

        }
        inClause = inClause + ")";

        return inClause;
    }

    private void toastMessage(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void checkFilePermissions() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            int permissionCheck = getActivity().checkSelfPermission("Manifest.permission.READ_EXTERNAL_STORAGE");
            permissionCheck += getActivity().checkSelfPermission("Manifest.permission.WRITE_EXTERNAL_STORAGE");
            if (permissionCheck != 0) {
                this.requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 1001);
            }
        } else {
            // Give notification if they do not confirm access rights
        }
    }
}