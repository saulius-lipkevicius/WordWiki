package com.example.wordwiki.ui_intro.login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.wordwiki.MainActivity;
import com.example.wordwiki.R;
import com.example.wordwiki.ui_intro.RegistrationActivity;
import com.example.wordwiki.ui_intro.account.CreateProfileActivity;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {
    String TAG = "LoginActivity";

    private CallbackManager mCallbackManager;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private AccessTokenTracker accessTokenTracker;

    // google sign-in
    FirebaseUser user;
    SignInButton mGoogleSignInButton;
    private GoogleApiClient googleApiClient;
    private static final int RC_SIGN_IN = 123;
    private GoogleSignInClient mGoogleSignInClient;

    TextInputEditText etLoginEmail, etLoginPassword;
    Button btnLogin;
    private LoginButton loginButtonHidden_fb;
    String email, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        // use custom toolbar and layout
        setUpView();

        // setup login providers
        setUpGoogleSignIn();
        setUpFbLoginIn();
        setUpEmailLogin();

        // create links to other activities and authListeners/trackers
        setUpLinks();
        setUpListeners();
    }

    private void setUpListeners() {
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
            }
        };

        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
                if (currentAccessToken == null) {
                    mAuth.signOut();
                }
            }
        };
    }

    private void setUpLinks() {
        Button toRegistration = findViewById(R.id.login_actionbar_registration);
        toRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, RegistrationActivity.class));
            }
        });

        TextView btnForgotLogin = findViewById(R.id.forgot_password);
        btnForgotLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setUpForgotPassDialog();
            }
        });
    }

    private void setUpGoogleSignIn() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        googleApiClient = new GoogleApiClient.Builder(LoginActivity.this).enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso).build();

        mGoogleSignInButton = findViewById(R.id.google_login_button_hidden);
        View loginButton_google = findViewById(R.id.login_button_google);

        loginButton_google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
                startActivityForResult(intent, RC_SIGN_IN);
            }
        });
    }

    private void setUpView() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Toolbar tb = findViewById(R.id.toolbar);
        setSupportActionBar(tb);

        setContentView(R.layout.activity_login);
    }

    private void setUpFbLoginIn() {
        FacebookSdk.sdkInitialize(getApplicationContext());
        View loginButton_fb = findViewById(R.id.login_button_fb);
        loginButtonHidden_fb = findViewById(R.id.fb_login_button_hidden);
        loginButtonHidden_fb.setPermissions("email", "public_profile");

        loginButton_fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginButtonHidden_fb.performClick();
            }
        });

        mCallbackManager = CallbackManager.Factory.create();
        loginButtonHidden_fb.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d("Entrance", "Facebook Authentication on Success: " + loginResult);
                handleFacebookToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Log.d("Entrance", "Facebook Authentication on Canceled");
            }

            @Override
            public void onError(@NonNull FacebookException e) {
                Log.d("Entrance", "Facebook Authentication on Error: " + e);
            }
        });
    }

    private void setUpForgotPassDialog() {
        final AlertDialog.Builder mode_mBuilder = new AlertDialog.Builder(this);
        LayoutInflater mode_inflater = getLayoutInflater();
        View convertView = mode_inflater.inflate(R.layout.dialog_forgot_password, null);
        mode_mBuilder.setView(convertView);
        AlertDialog mode_mDialog = mode_mBuilder.create();
        //mode_mDialog.getWindow().setWindowAnimations(R.style.DialogAnimation);
        mode_mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mode_mDialog.show();

        Button dialog_btn = convertView.findViewById(R.id.forgot_password_send_btn);
        TextInputEditText dialog_code = convertView.findViewById(R.id.forgot_password_send_code);
        TextInputEditText dialog_email = convertView.findViewById(R.id.forgot_password_send_email);


        dialog_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String emails = dialog_email.getText().toString();
                if (TextUtils.isEmpty(emails)) {
                    dialog_email.setError("Email cannot be empty");
                    dialog_email.requestFocus();
                }

                FirebaseAuth.getInstance().sendPasswordResetEmail(dialog_email.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Log.d(TAG, "Email sent.");
                                    mode_mDialog.dismiss();
                                }
                            }
                        });
            }
        });
    }

    private void setUpEmailLogin() {
        // find objects and extract information from it
        etLoginEmail = findViewById(R.id.etLoginEmail);
        etLoginPassword = findViewById(R.id.etLoginPass);
        btnLogin = findViewById(R.id.btnLogin);

        email = etLoginEmail.getText().toString();
        password = etLoginPassword.getText().toString();

        // perform login if all conditions are satisfied
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginUser();
            }
        });
    }

    private void loginUser() {
        // TODO implement additional conditions for mail/password
        // check if email/password is not empty
        if (TextUtils.isEmpty(email)) {
            etLoginEmail.setError("Email cannot be empty");
            etLoginEmail.requestFocus();
        } else if (TextUtils.isEmpty(password)) {
            etLoginPassword.setError("Password cannot be empty");
            etLoginPassword.requestFocus();
        } else {
            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        checkNewUser(task);

                        Toast.makeText(LoginActivity.this, "User logged in successfully", Toast.LENGTH_SHORT).show();
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user.isEmailVerified()) {
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));


                        } else {
                            Toast.makeText(LoginActivity.this, "Email is not verified", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(LoginActivity.this, "Log in Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private void handleFacebookToken(AccessToken token) {
        Log.d(TAG, "handleFacebookToken " + token);
        AuthCredential credentials = FacebookAuthProvider.getCredential(token.getToken());

        mAuth.signInWithCredential(credentials).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                checkNewUser(task);
            }
        });
    }

    // creates a shared preference that follows if the user is new or not. If it is new, call information
    // dialogs to create username and etc.
    public void checkNewUser(Task<AuthResult>  task) {
        SharedPreferences sharedPreferences = getSharedPreferences("general", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        boolean isNewUser = task.getResult().getAdditionalUserInfo().isNewUser();
        if (isNewUser) {
            editor.putBoolean("new_user", true);
            startActivity(new Intent(LoginActivity.this, CreateProfileActivity.class));
        } else {
            editor.putBoolean("new_user", false);
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
        }
        editor.apply();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCallbackManager.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
            } else {
                Toast.makeText(this, "Connection failed", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);

        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            checkNewUser(task);
                            //startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("TAG", "signInWithCredential:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    }
}