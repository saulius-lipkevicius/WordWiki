package com.example.wordwiki;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.wordwiki.databinding.ActivityMainBinding;
import com.example.wordwiki.ui_intro.login.LoginActivity;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity{

    private static final String TAG = "main";
    private ActivityMainBinding binding;
    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthListener;

    BottomNavigationView navView;
    NavController navController;
    BottomNavigationView bottomNavigationView;

    private GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();

        setUpView();
        setUpGoogleLogin();
        setUpTrackers();
        bottomNavigationReselection();
    }

    private void setUpView() {
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //Toolbar tb = findViewById(R.id.toolbar);
        //setSupportActionBar(tb);

        navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_library, R.id.navigation_profile)
                .build();
        navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);
    }

    private void setUpGoogleLogin() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    public FirebaseUser getCurrentUser(){
        return mAuth.getCurrentUser();
    }

    public void bottomNavigationReselection() {
        navView.setOnItemReselectedListener(new NavigationBarView.OnItemReselectedListener()  {
            @Override
            public void onNavigationItemReselected(@NonNull MenuItem item) {
                int id = item.getItemId();

                if (id == R.id.home_navigation) {
                    navController.navigate(R.id.action_global_home_navigation);
                } else if (id == R.id.explore_navigation){
                    navController.navigate(R.id.action_global_navigation_explore);
                } else if (id == R.id.library_navigation){
                    navController.navigate(R.id.action_global_navigation_library);
                } else if (id == R.id.profile_navigation) {
                    navController.navigate(R.id.action_global_navigation_profile);
                }
            }
        });
    }


    private void setUpLinks() {
        // call signOut, which triggers listener to get back to login activity
        /*
        btnLogOut = findViewById(R.id.btnLogout);
        btnLogOut.setOnClickListener(view -> {
            signOut();
        });

         */
    }


    private void setUpTrackers() {
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() == null) {
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                    overridePendingTransition(R.anim.enter_to_right, R.anim.exit_to_left);
                }
            }
        };
    }

    public void signOut() {
        // signout from firebase + forget FB and google login information
        mAuth.signOut();
        LoginManager.getInstance().logOut();
        mGoogleSignInClient.signOut();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }
}