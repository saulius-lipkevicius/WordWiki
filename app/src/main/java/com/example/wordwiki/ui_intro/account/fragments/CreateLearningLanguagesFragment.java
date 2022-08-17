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
import com.example.wordwiki.databinding.FragmentCreateLearningLanguagesBinding;
import com.example.wordwiki.databinding.FragmentHomeBinding;
import com.example.wordwiki.ui_intro.account.adapters.KnownLanguageAdapter;
import com.example.wordwiki.ui_intro.account.adapters.LearningLanguageAdapter;
import com.example.wordwiki.ui_intro.account.classes.RecyclerViewClickInterface;
import com.example.wordwiki.ui_intro.account.models.KnownLanguageHelper;
import com.example.wordwiki.ui_intro.account.models.LearningLanguageHelper;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;

public class CreateLearningLanguagesFragment extends Fragment  implements RecyclerViewClickInterface {
    private FragmentCreateLearningLanguagesBinding binding;
    ArrayList<LearningLanguageHelper> learningLanguages;
    HashMap<String, Integer> learningLanguageMap;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentCreateLearningLanguagesBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        setButtons();
        setRecyclerView();

        learningLanguageMap = new HashMap<>();

        return root;
    }


    private void setRecyclerView() {
        RecyclerView recyclerView = binding.getRoot().findViewById(R.id.fragment_create_learning_language_recycle_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));

        learningLanguages = new ArrayList<>();

        learningLanguages.add(new LearningLanguageHelper("English", com.blongho.country_data.R.drawable.gb, false));
        learningLanguages.add(new LearningLanguageHelper("Spanish", com.blongho.country_data.R.drawable.es, false));
        learningLanguages.add(new LearningLanguageHelper("German", com.blongho.country_data.R.drawable.de, false));
        learningLanguages.add(new LearningLanguageHelper("Italian", com.blongho.country_data.R.drawable.it, false));
        learningLanguages.add(new LearningLanguageHelper("French", com.blongho.country_data.R.drawable.fr, false));

        LearningLanguageAdapter languageAdapter = new LearningLanguageAdapter(learningLanguages, this::onItemClick);
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
                navController.navigate(R.id.action_navigation_create_user_learning_languages_to_navigation_create_user_known_languages);
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
                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("general", MODE_PRIVATE);
                String username = sharedPreferences.getString("username", "");


                FirebaseDatabase.getInstance("https://wordwiki-af0d4-default-rtdb.europe-west1.firebasedatabase.app/").getReference()
                        .child("Users").child(username).child("profile")
                        .child("learning").setValue(learningLanguageMap);

                NavController navController = Navigation.findNavController(view);
                navController.navigate(R.id.action_navigation_create_user_learning_languages_to_navigation_create_user_picture);

            }
        });
    }

    @Override
    public void onItemClick(int position) {
        if (learningLanguages.get(position).isChecked()) {
            learningLanguageMap.put(learningLanguages.get(position).getLanguageName(), 1);
        } else {
            try {
                learningLanguageMap.remove(learningLanguages.get(position).getLanguageName());
            } catch (Exception e) {
                Log.i(TAG, "onItemClick: error: " + e.toString());
            }
        }
    }
}