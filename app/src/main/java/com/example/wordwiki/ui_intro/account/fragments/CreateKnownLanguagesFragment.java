package com.example.wordwiki.ui_intro.account.fragments;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.wordwiki.R;
import com.example.wordwiki.databinding.FragmentCreateKnownLanguagesBinding;
import com.example.wordwiki.databinding.FragmentCreateLearningLanguagesBinding;

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


        return root;
        }

    private void setButtons() {
        ImageButton backBtn = binding.getRoot().findViewById(R.id.back);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("create_user", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("known", "");

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
                NavController navController = Navigation.findNavController(view);
                navController.navigate(R.id.action_navigation_create_user_known_languages_to_navigation_create_user_learning_languages);

            }
        });
    }
}