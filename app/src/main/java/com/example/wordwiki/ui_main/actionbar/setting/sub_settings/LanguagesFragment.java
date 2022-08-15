package com.example.wordwiki.ui_main.actionbar.setting.sub_settings;

import static android.content.ContentValues.TAG;
import static com.facebook.FacebookSdk.getApplicationContext;

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
        TextView toolbarTitle = binding.getRoot().findViewById(R.id.toolbar_title);
        toolbarTitle.setText("Languages");

        World.init(getApplicationContext());
        myDb = new DatabaseHelper(getContext());
        toSettings = binding.getRoot().findViewById(R.id.toolbar_back_btn);
        NavHostFragment navHostFragment = (NavHostFragment) getActivity().getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment_activity_main);
        navCo = navHostFragment.getNavController();

        toSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("a", "onClick: aaaa");
                navCo.navigate(R.id.action_navigation_language_to_navigation_setting);
            }
        });

    }

    private void addLanguages() {
        String country_name = myDb.getFlagISO("Spanish");
        int flag = World.getFlagOf(country_name);
        languageList.clear();
        languageList.add(new LanguagesModel("English", flag));
        languageList.add(new LanguagesModel("Spanish", flag));
        languageList.add(new LanguagesModel("German", flag));
        languageList.add(new LanguagesModel("French", flag));
        languageList.add(new LanguagesModel("Lithuanian", flag));

    }

    @Override
    public void onSettingClick(int position) {
        Log.i(TAG, "onLanguage selected " + languageList.get(position).getLanguageName());
    }


}