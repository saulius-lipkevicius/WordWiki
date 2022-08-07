package com.example.wordwiki.ui_intro.account.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.wordwiki.R;
import com.example.wordwiki.databinding.FragmentCreateDescriptionBinding;

public class CreateDescriptionFragment extends Fragment {
    FragmentCreateDescriptionBinding binding;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentCreateDescriptionBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        setButtons();

        return root;
    }

    private void setButtons() {
        Button finishIntroductionBtn = binding.getRoot().findViewById(R.id.fragment_username_next_btn);
        finishIntroductionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO start mainActivity
                //NavController navController = Navigation.findNavController(view);
                //navController.navigate(R.id.action_navigation_create_user_username_to_navigation_create_user_known_languages);

            }
        });
    }
}