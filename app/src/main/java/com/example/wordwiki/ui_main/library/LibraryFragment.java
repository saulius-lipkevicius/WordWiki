package com.example.wordwiki.ui_main.library;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wordwiki.R;
import com.example.wordwiki.databinding.FragmentLibraryBinding;
import com.example.wordwiki.ui_main.actionbar.notification.NotificationFragment;
import com.example.wordwiki.ui_main.actionbar.setting.SettingFragment;
import com.example.wordwiki.ui_main.library.adapters.SectionAdapter;
import com.example.wordwiki.ui_main.library.models.SectionHelper;
import com.example.wordwiki.ui_main.library.models.SubsectionHelper;

import java.util.ArrayList;
import java.util.List;

public class LibraryFragment extends Fragment {

    private static final String TAG = "Library Fragment, root.";
    private FragmentLibraryBinding binding;

    // recycle views
    List<SectionHelper> sectionList = new ArrayList<>();
    RecyclerView mainRecyclerView;
    SectionAdapter sectionAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentLibraryBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        Toolbar tb = root.findViewById(R.id.toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(tb);

        //initialize recycle viewers
        initData();
        mainRecyclerView = root.findViewById(R.id.fragment_library_top_recycle_view);
        sectionAdapter = new SectionAdapter(sectionList);
        mainRecyclerView.setAdapter(sectionAdapter);
        //mainRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));

        return root;
    }

    private void initData() {
        String sectionOneName = "English";
        List<SubsectionHelper> sectionOneItems = new ArrayList<>();

        sectionOneItems.add(new SubsectionHelper(sectionOneName, "Introduction", "This dictionary considers 100 most used words", "mike2", "B2", false, false, false));
        sectionOneItems.add(new SubsectionHelper(sectionOneName, "Animals", "This dictionary considers 100 most used words", "mike2", "C2",false, false, false));
        sectionOneItems.add(new SubsectionHelper(sectionOneName, "Colors", "This dictionary considers 100 most used words", "mike2", "A2",false, false, false));
        sectionOneItems.add(new SubsectionHelper(sectionOneName, "Body Parts", "This dictionary considers 100 most used words", "mike2", "A2",false, false, false));

        String sectionTwoName = "German";
        List<SubsectionHelper> sectionTwoItems = new ArrayList<>();

        sectionTwoItems.add(new SubsectionHelper(sectionTwoName, "Introduction to human anatomy", "This dictionary considers 100 most used words", "mike2", "A2",false, false, false));
        sectionTwoItems.add(new SubsectionHelper(sectionTwoName, "Animals", "This dictionary considers 100 most used words", "mike2", "A2",false, false, false));
        sectionTwoItems.add(new SubsectionHelper(sectionTwoName, "Colors", "This dictionary considers 100 most used words", "mike2", "A1",false, false, false));
        sectionTwoItems.add(new SubsectionHelper(sectionTwoName, "Body Parts", "This dictionary considers 100 most used words", "mike2", "A2",false, false, false));
        sectionTwoItems.add(new SubsectionHelper(sectionTwoName, "Nouns A1", "This dictionary considers 100 most used words", "mike2", "B2",false, false, false));
        sectionTwoItems.add(new SubsectionHelper(sectionTwoName, "BVerbs B1", "This dictionary considers 100 most used words", "mike2", "C2",false, false, false));

        sectionList.add(new SectionHelper(sectionOneName, com.blongho.country_data.R.drawable.gb, sectionOneItems));
        sectionList.add(new SectionHelper(sectionTwoName, com.blongho.country_data.R.drawable.de, sectionTwoItems));

        Log.d(TAG, "initData: " + sectionList);
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.fragment_library_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                sectionAdapter.getFilter().filter(newText);
                return false;
            }
        });

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}