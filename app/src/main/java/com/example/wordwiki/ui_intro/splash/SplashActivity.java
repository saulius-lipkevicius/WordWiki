package com.example.wordwiki.ui_intro.splash;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.example.wordwiki.R;
import com.example.wordwiki.ui_intro.intro.IntroActivity;

public class SplashActivity extends AppCompatActivity {
    private static final int SPLASH_TIME_OUT = 200;
    Intent nextActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        SharedPreferences sharedPreferences = getSharedPreferences("general", MODE_PRIVATE);
        //boolean introBool = sharedPreferences.getBoolean("Status", false); //second argument is default, if your request is empty
        //boolean acquaintanceBool = sharedPreferences.getBoolean("Status_acquaintance", false); //second argument is default, if your request is empty
        //boolean authBool = sharedPreferences.getBoolean("Status_auth", false); //second argument is default, if your request is empty

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                /*
                if (!introBool) {
                    nextActivity = new Intent(SplashActivity.this, IntroActivity.class);
                } else if (!authBool) {
                    nextActivity = new Intent(SplashActivity.this, LoginActivity.class);
                } else if(!acquaintanceBool) {
                    nextActivity = new Intent(SplashActivity.this, AcqActivity.class);
                } else {
                    nextActivity = new Intent(SplashActivity.this, EntranceActivity.class);
                }

                 */
                nextActivity = new Intent(SplashActivity.this, IntroActivity.class);
                startActivity(nextActivity);
                finish();
            }
        }, SPLASH_TIME_OUT);
    }

    public boolean launcherPreferenceAuth() {//ModePreference for changing types of learning
        SharedPreferences sharedPreferences = getSharedPreferences("general", MODE_PRIVATE);
        return sharedPreferences.getBoolean("Status_auth", false); //second argument is default, if your request is empty
    }//preference
}