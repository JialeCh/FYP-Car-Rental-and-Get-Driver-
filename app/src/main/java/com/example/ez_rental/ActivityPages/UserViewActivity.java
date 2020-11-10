package com.example.ez_rental.ActivityPages;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.ez_rental.FragmentPages.AccountFragment;
import com.example.ez_rental.FragmentPages.ReservationFragment;
import com.example.ez_rental.FragmentPages.TransactionFragment;
import com.example.ez_rental.R;
import com.example.ez_rental.activity.PermissionsActivity;

import nl.joery.animatedbottombar.AnimatedBottomBar;

public class UserViewActivity extends AppCompatActivity {

    private FrameLayout frameLayout;
    private AccountFragment accountFragment;
    private static final String TAG = UserViewActivity.class.getSimpleName();
    AnimatedBottomBar animatedBottomBar;
    FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_view);
        animatedBottomBar = findViewById(R.id.bottom_nav);
        frameLayout = findViewById(R.id.framelayout);

        if (savedInstanceState == null) {
            animatedBottomBar.selectTabById(R.id.nav_account, true);
            fragmentManager = getSupportFragmentManager();
            accountFragment = new AccountFragment();
            fragmentManager.beginTransaction().replace(R.id.framelayout, accountFragment)
                    .commit();
        }
        clickListener();

    }

    private void clickListener() {
        animatedBottomBar.setOnTabSelectListener((lastIndex, lastTab, newIndex, newTab) -> {
            Fragment fragment = null;
            switch (newTab.getId()) {
                case R.id.nav_payment:
                    fragment = new TransactionFragment();
                    break;
                case R.id.nav_booking:
                    fragment = new ReservationFragment();
                    break;
                case R.id.nav_search :
                    startActivity(new Intent(getApplicationContext(), PermissionsActivity.class));
                    break;
                case R.id.nav_account :
                    fragment = new AccountFragment();
                    break;
            }

            if (fragment != null) {
                fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.framelayout, fragment)
                        .commit();
            } else {
                Log.e(TAG, "Error in creating Fragment");
            }
        });
    }



}
