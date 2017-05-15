package com.example.android.roomrent.Activity;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Is the App Splash Screen
 */
public class SplashActivity extends AppCompatActivity {

    /**
     * Redirect to Home screen as soon as Splash Screen is created
     * <p>
     * Some processing such as query to database
     * can be added asynchronously later
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Intent intent = new Intent(SplashActivity.this,MainActivity.class);
        startActivity(intent);

    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}