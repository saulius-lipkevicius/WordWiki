package com.example.wordwiki.ui_intro.account.fragments;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.wordwiki.R;
import com.example.wordwiki.databinding.FragmentCreateKnownLanguagesBinding;
import com.example.wordwiki.databinding.FragmentCreateLearningLanguagesBinding;
import com.example.wordwiki.ui_intro.account.adapters.KnownLanguageAdapter;
import com.example.wordwiki.ui_intro.account.models.KnownLanguageHelper;
import com.example.wordwiki.ui_main.library.adapters.SectionAdapter;
import com.example.wordwiki.ui_main.profile.models.progressHelper;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class CreateKnownLanguagesFragment extends Fragment {
    FragmentCreateKnownLanguagesBinding binding;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentCreateKnownLanguagesBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        setButtons();
        setRecyclerView();
        fillRecyclerView();




        return root;
        }

    private void setRecyclerView() {
        RecyclerView recyclerView = binding.getRoot().findViewById(R.id.fragment_create_known_language_recycle_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));

        ArrayList<KnownLanguageHelper> knownLanguages = new ArrayList<>();

        knownLanguages.add(new KnownLanguageHelper("English", com.blongho.country_data.R.drawable.gb, false, false, false, false, false, false, false ));
        knownLanguages.add(new KnownLanguageHelper("Spanish", com.blongho.country_data.R.drawable.es, false, false, false, false, false, false, false ));
        knownLanguages.add(new KnownLanguageHelper("German", com.blongho.country_data.R.drawable.de, false, false, false, false, false, false, false ));
        knownLanguages.add(new KnownLanguageHelper("Italian", com.blongho.country_data.R.drawable.it, false, false, false, false, false, false, false ));
        knownLanguages.add(new KnownLanguageHelper("French", com.blongho.country_data.R.drawable.fr, false, false, false, false, false, false, false ));

        KnownLanguageAdapter languageAdapter = new KnownLanguageAdapter(knownLanguages);
        recyclerView.setAdapter(languageAdapter);
        // TODO create an adapter for it and item (fragment_create_user_known_language_item)

        // TODO make it vertical with 2 columns, using gridLayout
    }

    private void fillRecyclerView() {
    }

    private void setButtons() {
        ImageButton backBtn = binding.getRoot().findViewById(R.id.back);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavController navController = Navigation.findNavController(view);
                navController.navigate(R.id.action_navigation_create_user_known_languages_to_navigation_create_user_username);
            }
        });

        Button skipFragment = binding.getRoot().findViewById(R.id.fragment_username_skip_btn);
        skipFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // navigating to the mainActivity with navView
            }
        });

        Button nextFragment = binding.getRoot().findViewById(R.id.fragment_username_next_btn);
        nextFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //FirebaseDatabase.getInstance("https://wordwiki-af0d4-default-rtdb.europe-west1.firebasedatabase.app/").getReference().child("Users").child("saulius").child("description").setValue(description);

                NavController navController = Navigation.findNavController(view);
                navController.navigate(R.id.action_navigation_create_user_known_languages_to_navigation_create_user_learning_languages);

            }
        });
    }
}