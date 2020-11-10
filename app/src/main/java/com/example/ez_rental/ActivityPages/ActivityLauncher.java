package com.example.ez_rental.ActivityPages;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.motion.widget.MotionLayout;

import com.example.ez_rental.R;
import com.example.ez_rental.activity.AdminLoginActivity;
import com.example.ez_rental.activity.LoginActivity;
import com.example.ez_rental.helper.SQLiteHelper;


public class ActivityLauncher extends AppCompatActivity {
    private TextView guestView;
    private LinearLayout viewAdmin, viewUser;
    private SQLiteHelper db;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_motion);

    }

    @SuppressLint("CutPasteId")
    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        guestView = findViewById(R.id.Guest);
        viewAdmin = findViewById(R.id.image_admin);
        viewUser = findViewById(R.id.image_user);
        View btnContinue = findViewById(R.id.view_continue);
        viewUser.setOnClickListener(v -> {
            viewUser.setSelected(!viewUser.isSelected());
            viewAdmin.setSelected(false);
        });


        viewAdmin.setOnClickListener(v -> {

            viewAdmin.setSelected(!viewAdmin.isSelected());
            viewUser.setSelected(false);
        });
        // Link to Register Screen
        guestView.setOnClickListener(view -> {
            Intent i = new Intent(getApplicationContext(),
                    GuestViewActivity.class);
            startActivity(i);
            finish();
        });
        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validate();
            }
        });

        startMotion();
    }

    private void startMotion() {

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                    MotionLayout motionLayout = findViewById(R.id.start_screen);
                    motionLayout.transitionToEnd();
            }
        }, 1500);
    }

    private void validate() {
        try {

            Intent intent;
            if (viewUser.isSelected()) {

                intent = new Intent(com.example.ez_rental.ActivityPages.ActivityLauncher.this, LoginActivity.class);

            } else if (viewAdmin.isSelected()) {

                intent = new Intent(com.example.ez_rental.ActivityPages.ActivityLauncher.this, AdminLoginActivity.class);

            }
            else {
                Toast.makeText(this, "Select One to Continue", Toast.LENGTH_SHORT).show();
                return;
            }
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
