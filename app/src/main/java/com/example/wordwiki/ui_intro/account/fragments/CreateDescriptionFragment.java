package com.example.wordwiki.ui_intro.account.fragments;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.wordwiki.MainActivity;
import com.example.wordwiki.R;
import com.example.wordwiki.databinding.FragmentCreateDescriptionBinding;
import com.example.wordwiki.ui_intro.account.User;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.apache.log4j.chainsaw.Main;

import java.util.HashMap;
import java.util.Map;

public class CreateDescriptionFragment extends Fragment {
    FragmentCreateDescriptionBinding binding;
    TextInputEditText editText;
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


        editText = root.findViewById(R.id.outlined_edit_text);
        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });

        TextView editTextCounter = root.findViewById(R.id.fragment_create_user_description_counter);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                editTextCounter.setText(editable.length() + " / 150");
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }
        });

        //Map<String, String> learningLanguages = new HashMap<>();
        //Map<String, String> knownLanguages = new HashMap<>();


        //createNewUser(username, "Lithuanian", learningLanguages, knownLanguages);

        return root;
    }

    private void setButtons() {
        ImageButton backBtn = binding.getRoot().findViewById(R.id.back);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavController navController = Navigation.findNavController(view);
                navController.navigate(R.id.action_navigation_create_user_description_to_navigation_create_user_picture);
            }
        });

        Button finishIntroductionBtn = binding.getRoot().findViewById(R.id.fragment_username_next_btn);
        finishIntroductionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("user_profile", MODE_PRIVATE);
                String username = sharedPreferences.getString("username", "");

                String description = editText.getText().toString();

                FirebaseDatabase.getInstance("https://wordwiki-af0d4-default-rtdb.europe-west1.firebasedatabase.app/").getReference()
                        .child("Users").child(username).child("profileInfo")
                        .child("description").setValue(description);

                Intent intent = new Intent(requireActivity(), MainActivity.class);
                startActivity(intent);
            }
        });
    }

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}