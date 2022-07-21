package com.example.wordwiki.ui_intro;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.wordwiki.R;
import com.example.wordwiki.ui_intro.login.LoginActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class RegistrationActivity extends AppCompatActivity {
    String TAG = "Registration Activity";
    FirebaseAuth mAuth;
    TextInputEditText etRegEmail, etRegPassword;
    TextView tvLoginHere;
    Button btnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();

        setUpView();
        setUpLinks();
    }

    private void setUpLinks() {
        ImageButton toLogIn = findViewById(R.id.registration_back);
        toLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegistrationActivity.this, LoginActivity.class));
                overridePendingTransition(R.anim.enter_to_right, R.anim.exit_to_left);
            }
        });



        etRegEmail = findViewById(R.id.etRegEmail);
        etRegPassword = findViewById(R.id.etRegPass);
        tvLoginHere = findViewById(R.id.tvLoginHere);
        btnRegister = findViewById(R.id.btnRegister);

        btnRegister.setOnClickListener(view -> {
            createUser();
            //TODO create custom Toast message identifying verification needed
        });

        tvLoginHere.setOnClickListener(view -> {
            startActivity(new Intent(RegistrationActivity.this, LoginActivity.class));
            overridePendingTransition(R.anim.enter_to_right, R.anim.exit_to_left);
        });
    }

    private void setUpView() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Toolbar tb = findViewById(R.id.toolbar);
        setSupportActionBar(tb);

        setContentView(R.layout.activity_registration);
    }

    private void createUser() {
        String email = etRegEmail.getText().toString();
        String password = etRegPassword.getText().toString();


        if (TextUtils.isEmpty(email)) {
            etRegEmail.setError("Email cannot be empty");
            etRegEmail.requestFocus();
        } else if (TextUtils.isEmpty(password)) {
            etRegPassword.setError("Password cannot be empty");
            etRegPassword.requestFocus();
        } else {
            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        mAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(RegistrationActivity.this, "Please verify your email address", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(RegistrationActivity.this, LoginActivity.class));
                                overridePendingTransition(R.anim.enter_to_right, R.anim.exit_to_left);
                            }
                        });
                        Toast.makeText(RegistrationActivity.this, "User registered successfully", Toast.LENGTH_SHORT).show();

                        Map<String, String> learningLanguages = new HashMap<>();
                        learningLanguages.put("English", "A1");

                        Map<String, String> knownLanguages = new HashMap<>();
                        knownLanguages.put("German", "C1");

                        createNewUser("as useris", "Lithuanian", learningLanguages, knownLanguages);
                    } else {
                        Toast.makeText(RegistrationActivity.this, "Registration Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private void createNewUser(String username, String nationality, Map<String, String> learningLanguages, Map<String, String> knownLanguages) {
        // TODO move to firebase
        String userId = mAuth.getUid();
        User user = new User(username, nationality, learningLanguages, knownLanguages);


        FirebaseDatabase db = FirebaseDatabase.getInstance("https://wordwiki-af0d4-default-rtdb.europe-west1.firebasedatabase.app/");
        DatabaseReference databaseReference = db.getReference("users");
        assert userId != null;
        databaseReference.child(username).setValue(user);

        // TODO move to the storage

    }
}