package com.example.wordwiki.ui_main.actionbar.setting.sub_settings;

import static android.content.ContentValues.TAG;
import static android.content.Context.MODE_PRIVATE;
import static com.facebook.FacebookSdk.getApplicationContext;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.blongho.country_data.World;
import com.example.wordwiki.R;
import com.example.wordwiki.database.DatabaseHelper;
import com.example.wordwiki.databinding.FragmentLanguagesBinding;
import com.example.wordwiki.ui_main.actionbar.setting.adapters.LanguageListAdapter;
import com.example.wordwiki.ui_main.actionbar.setting.models.LanguagesModel;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class LanguagesFragment extends Fragment implements LanguageListAdapter.OnSettingListener {
    FragmentLanguagesBinding binding;
    List<LanguagesModel> languageList = new ArrayList<>();
    LanguageListAdapter languageAdapter;
    RecyclerView languageListRecycle;
    DatabaseHelper myDb;
    ImageButton toSettings;
    NavController navCo;
    TextView toolbarTitle;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO implement language change by a click on a language row

        // Inflate the layout for this fragment
        binding = FragmentLanguagesBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        setUpFunctionality();
        addLanguages();
        setUpLanguageRecycler();

        return root;
    }

    private void setUpLanguageRecycler() {
        languageAdapter = new LanguageListAdapter(languageList, this);
        languageListRecycle = binding.getRoot().findViewById(R.id.fragment_settings_language_recycler);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        languageListRecycle.setLayoutManager(layoutManager);

        languageListRecycle.addItemDecoration(new DividerItemDecoration(languageListRecycle.getContext(),
                layoutManager.getOrientation()));
        languageListRecycle.setAdapter(languageAdapter);
    }

    private void setUpFunctionality() {
        toolbarTitle = binding.getRoot().findViewById(R.id.toolbar_title);
        toolbarTitle.setText("Language");

        World.init(getApplicationContext());
        myDb = new DatabaseHelper(getContext());
        toSettings = binding.getRoot().findViewById(R.id.toolbar_back_btn);
        NavHostFragment navHostFragment = (NavHostFragment) getActivity().getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment_activity_main);
        navCo = navHostFragment.getNavController();

        toSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navCo.navigate(R.id.action_navigation_language_to_navigation_setting);
            }
        });

    }

    private void addLanguages() {
        languageList.clear();
        languageList.add(new LanguagesModel("English", World.getFlagOf("gb")));
        languageList.add(new LanguagesModel("Spanish", World.getFlagOf("es")));
        languageList.add(new LanguagesModel("German", World.getFlagOf("de")));
        languageList.add(new LanguagesModel("French", World.getFlagOf("fr")));
        languageList.add(new LanguagesModel("Lithuanian", World.getFlagOf("lt")));

    }

    @Override
    public void onSettingClick(int position) {
        // TODO finish the following
        // if the item is clicked, put confirmation
        Boolean isConfirmed = true;

        if (isConfirmed){
            // save it to the preferences
            SharedPreferences sharedPreferences = getActivity().getSharedPreferences("preferences", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();

            String chosenLanguage = languageList.get(position).getLanguageName();
            editor.putString(chosenLanguage, "English");
            editor.apply();

            // get username from preferences
            SharedPreferences usernameSharedPreference = getActivity().getSharedPreferences("general", MODE_PRIVATE);
            String username = usernameSharedPreference.getString("username", "");

            // save it to the firebase database for later use
            FirebaseDatabase.getInstance("https://wordwiki-af0d4-default-rtdb.europe-west1.firebasedatabase.app/").getReference()
                    .child("Users").child(username).child("preference")
                    .child("language").setValue(chosenLanguage);

            // call function which determines app language and how to translate it
        } else {
            Snackbar.make(binding.getRoot(), "Try again to change the language", Snackbar.LENGTH_SHORT).show();
        }


    }


}