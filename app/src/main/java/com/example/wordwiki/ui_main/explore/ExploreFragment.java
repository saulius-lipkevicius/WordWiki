package com.example.wordwiki.ui_main.explore;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.wordwiki.R;
import com.example.wordwiki.databinding.FragmentExploreBinding;
import com.example.wordwiki.databinding.FragmentSettingBinding;

public class ExploreFragment extends Fragment {
    FragmentExploreBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentExploreBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


        return root;
    }
}