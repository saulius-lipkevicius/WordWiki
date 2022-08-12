package com.example.wordwiki.ui_intro.account;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;

import com.example.wordwiki.R;
import com.google.firebase.auth.FirebaseUser;

public class CreateProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_profile);

        Toolbar tb = findViewById(R.id.toolbar_about);
        setSupportActionBar(tb);
    }
}