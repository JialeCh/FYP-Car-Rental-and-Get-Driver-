package com.example.ez_rental.activity.StartUp;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ez_rental.R;

public class ActivitySplash extends AppCompatActivity {

    ProgressBar splashProgress;
    int SPLASH_TIME = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


        splashProgress = findViewById(R.id.splashProgress);
        playProgress();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent mySuperIntent = new Intent(ActivitySplash.this,ActivityLauncher.class);
                startActivity(mySuperIntent);

                finish();

            }
        }, SPLASH_TIME);
    }

    private void playProgress() {
        ObjectAnimator.ofInt(splashProgress, "progress", 100)
                .setDuration(5000)
                .start();
    }
}