package com.example.wordwiki;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.blongho.country_data.World;
import com.example.wordwiki.classes.AsyncTaskClassFetchProfileData;
import com.example.wordwiki.databinding.ActivityMainBinding;
import com.example.wordwiki.ui_intro.account.User;
import com.example.wordwiki.ui_intro.login.LoginActivity;
import com.example.wordwiki.ui_main.profile.models.flagHelper;
import com.facebook.login.LoginManager;
import com.github.mikephil.charting.formatter.IFillFormatter;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.Collections;
import java.util.Comparator;

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

        checkUserProfileData();

        setUpView();
        setUpGoogleLogin();
        setUpTrackers();
        bottomNavigationReselection();
    }

    public String getUsername(){
        SharedPreferences usernameSharedPreference = getSharedPreferences("user_profile", MODE_PRIVATE);
        String username = usernameSharedPreference.getString("username", "");

        /*
        if (username.equals("")) {
            FirebaseDatabase.getInstance("https://wordwiki-af0d4-default-rtdb.europe-west1.firebasedatabase.app").getReference("Authorization")
                    .child(getInstance().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            String username = snapshot.getValue().toString();
                            Log.i(TAG, "onDataChange: aaaa " + username);
                            usernameSharedPreference.edit().putString("username", username).apply();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
        }
        username = usernameSharedPreference.getString("username", "");
*/
        return username;
    }


    private void setUpView() {
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        Toolbar tb = findViewById(R.id.toolbar);
        setSupportActionBar(tb);

        navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_library, R.id.navigation_profile)
                .build();
        navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);
        tb.hideOverflowMenu();
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

    public FirebaseAuth getInstance() {
        return FirebaseAuth.getInstance();
    }

    public String  getAuthenticationType (){
        return FirebaseAuth.getInstance().getCurrentUser().getProviderData().get(0).getProviderId();
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
        //signout from firebase + forget FB and google login information
        mAuth.signOut();
        LoginManager.getInstance().logOut();
        mGoogleSignInClient.signOut();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    public void checkUserProfileData(){
        SharedPreferences sharedPreferences = getSharedPreferences("user_profile", MODE_PRIVATE);
        Boolean isProfileFilled = sharedPreferences.getBoolean("isFilled", false);
        Boolean isNewAccount = getSharedPreferences("general", MODE_PRIVATE).getBoolean("new_user", false);
        if(!isProfileFilled || isNewAccount) {
            Log.i(TAG, "checkUserProfileData: filling account information from firebase because it is initiated for the first time after the authorization. Given Uid: " + mAuth.getUid());
            AsyncTaskClassFetchProfileData taskFetchProfileData = new AsyncTaskClassFetchProfileData(this);
            taskFetchProfileData.execute(mAuth.getUid());

            sharedPreferences.edit().putBoolean("isFilled", true).apply();
        }
    }
}