package com.example.wordwiki.ui_intro.account.fragments;


import static android.content.ContentValues.TAG;
import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wordwiki.R;
import com.example.wordwiki.databinding.FragmentCreateUsernameBinding;
import com.example.wordwiki.ui_intro.account.User;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Map;

public class CreateUsernameFragment extends Fragment {
    FragmentCreateUsernameBinding binding;
    TextInputEditText editText;
    public boolean usernameEditTextFocus = true;
    Button nextFragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentCreateUsernameBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        setButtons();

        editText = root.findViewById(R.id.outlined_edit_text);
        TextInputLayout editText_layout = root.findViewById(R.id.outlined_text_input_layout);

        //editText.setText("@");
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                Log.i(TAG, "afterTextChanged: length 1is " + editable.length());
                Log.i(TAG, "afterTextChanged: length 2is " + editable);
                if (editable.length() == 0 && usernameEditTextFocus) {
                    editText.setText("@");
                    editText.setSelection(1);
                } else if (editable.length() > 1) {
                    readData(new MyCallback() {
                        @Override
                        public void onCallback(Boolean value) {
                            Log.i(TAG, "onCallback: " + value);
                            if (value) {
                                int colorInt = getResources().getColor(R.color.red);
                                ColorStateList csl = ColorStateList.valueOf(colorInt);

                                editText_layout.setBoxStrokeColorStateList(csl);
                                editText_layout.setHintTextColor(csl);
                                editText_layout.setErrorIconDrawable(null);
                                editText_layout.setError("Username is taken");

                                nextFragment.setEnabled(false);
                            } else {
                                int colorInt = getResources().getColor(R.color.black);
                                ColorStateList csl = ColorStateList.valueOf(colorInt);

                                editText_layout.setBoxStrokeColorStateList(csl);
                                editText_layout.setHintTextColor(csl);

                                editText_layout.setError(null);
                                nextFragment.setEnabled(true);
                            }
                        }
                    });
                }

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }
        });


        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                    if (editText.length() == 1) {
                        usernameEditTextFocus = false;
                        editText.setText("");
                    }
                } else {
                    usernameEditTextFocus = true;
                    if (editText.length() == 0) {
                        editText.setText("@");
                        //editText.setSelection(1);
                    }
                }
            }
        });

        return root;
    }


    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }


    private void setButtons() {

        nextFragment = binding.getRoot().findViewById(R.id.fragment_username_next_btn);
        nextFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editText.length() < 2) {
                    Toast.makeText(getContext(), "Username can't be empty", Toast.LENGTH_SHORT).show();
                } else {
                    createNewUser(editText.getText().toString(), null, null, null);

                    NavController navController = Navigation.findNavController(view);
                    navController.navigate(R.id.action_navigation_create_user_username_to_navigation_create_user_known_languages);
                }
            }
        });
    }

    public void readData(MyCallback myCallback) {
        String myUsername = editText.getText().toString().substring(1);
        Log.i(TAG, "onDataChange: mano testas edittext " + myUsername);
        FirebaseDatabase.getInstance("https://wordwiki-af0d4-default-rtdb.europe-west1.firebasedatabase.app/").getReference()
                .child("Users").child(myUsername).child("profile").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Boolean value = dataSnapshot.exists();
                myCallback.onCallback(value);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    public interface MyCallback {
        void onCallback(Boolean value);
    }


    private void createNewUser(String username, String nationality, Map<String, String> learningLanguages, Map<String, String> knownLanguages) {
        User user = new User(username.substring(1), null, null, null, null);

        FirebaseDatabase db = FirebaseDatabase.getInstance("https://wordwiki-af0d4-default-rtdb.europe-west1.firebasedatabase.app/");
        DatabaseReference databaseReference = db.getReference("Users");

        databaseReference.child(username.substring(1)).setValue(user);

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("general", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString("username", username.substring(1));
        editor.commit();
    }

}

