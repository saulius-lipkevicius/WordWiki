package com.example.wordwiki.ui_intro.account.fragments;

import static android.content.ContentValues.TAG;
import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.wordwiki.R;
import com.example.wordwiki.databinding.FragmentCreateKnownLanguagesBinding;
import com.example.wordwiki.databinding.FragmentCreateLearningLanguagesBinding;
import com.example.wordwiki.ui_intro.account.adapters.KnownLanguageAdapter;
import com.example.wordwiki.ui_intro.account.classes.RecyclerViewClickInterface;
import com.example.wordwiki.ui_intro.account.models.KnownLanguageHelper;
import com.example.wordwiki.ui_main.library.adapters.SectionAdapter;
import com.example.wordwiki.ui_main.profile.models.progressHelper;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CreateKnownLanguagesFragment extends Fragment implements RecyclerViewClickInterface {
    FragmentCreateKnownLanguagesBinding binding;
    ArrayList<KnownLanguageHelper> knownLanguages;
    HashMap<String, Integer> knownLanguageMap;
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

        knownLanguageMap = new HashMap<>();


        return root;
        }

    private void setRecyclerView() {
        RecyclerView recyclerView = binding.getRoot().findViewById(R.id.fragment_create_known_language_recycle_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));

        knownLanguages = new ArrayList<>();

        knownLanguages.add(new KnownLanguageHelper("English", com.blongho.country_data.R.drawable.gb, false, false, false, false, false, false, false , 0));
        knownLanguages.add(new KnownLanguageHelper("Spanish", com.blongho.country_data.R.drawable.es, false, false, false, false, false, false, false , 0));
        knownLanguages.add(new KnownLanguageHelper("German", com.blongho.country_data.R.drawable.de, false, false, false, false, false, false, false , 0));
        knownLanguages.add(new KnownLanguageHelper("Italian", com.blongho.country_data.R.drawable.it, false, false, false, false, false, false, false , 0));
        knownLanguages.add(new KnownLanguageHelper("French", com.blongho.country_data.R.drawable.fr, false, false, false, false, false, false, false , 0));

        KnownLanguageAdapter languageAdapter = new KnownLanguageAdapter(knownLanguages, this::onItemClick);
        recyclerView.setAdapter(languageAdapter);
        // TODO create an adapter for it and item (fragment_create_user_known_language_item)

        // TODO make it vertical with 2 columns, using gridLayout
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
                FirebaseAuth mAuth = FirebaseAuth.getInstance();
                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("user_profile", MODE_PRIVATE);
                String username = sharedPreferences.getString("username", "");

                FirebaseDatabase.getInstance("https://wordwiki-af0d4-default-rtdb.europe-west1.firebasedatabase.app/").getReference()
                        .child("Users").child("Personal").child(mAuth.getUid()).child("ProfileInfo")
                        .child("proficiency").setValue(knownLanguageMap);

                // save the same in the public root
                FirebaseDatabase.getInstance("https://wordwiki-af0d4-default-rtdb.europe-west1.firebasedatabase.app/").getReference()
                        .child("Users").child("Public").child(username)
                        .child("proficiency").setValue(knownLanguageMap);


                NavController navController = Navigation.findNavController(view);
                navController.navigate(R.id.action_navigation_create_user_known_languages_to_navigation_create_user_learning_languages);

            }
        });
    }

    @Override
    public void onItemClick(int position) {
        knownLanguageMap.put(knownLanguages.get(position).getLanguageName(), knownLanguages.get(position).getSelectedLevel());
    }
}