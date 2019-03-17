package com.sk.workextinder.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.sk.workextinder.R;
import com.sk.workextinder.util.AppAndroidUtils;
import com.sk.workextinder.util.AppPreferenceStorage;

/*
 * Created by Sambhaji Karad on 12-03-2019
 */

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                if (AppPreferenceStorage.getUserLoginStatus()){
                    openHomeScreen();
                } else {
                    openLoginScreen();
                }

            }
        }, 1000);
    }

    private void openHomeScreen() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        finish();
        AppAndroidUtils.startFwdAnimation(SplashActivity.this);
    }

    private void openLoginScreen() {
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intent);
        finish();
        AppAndroidUtils.startFwdAnimation(SplashActivity.this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //Do nothing
    }
}
