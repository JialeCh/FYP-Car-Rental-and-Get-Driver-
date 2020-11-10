package com.example.ez_rental.ActivityPages;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.ez_rental.FragmentPages.AccountFragment;
import com.example.ez_rental.FragmentPages.MapsFragment;
import com.example.ez_rental.FragmentPages.ReservationFragment;
import com.example.ez_rental.FragmentPages.VehicleCategoryFragment;
import com.example.ez_rental.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class GuestViewActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private FrameLayout frameLayout;

    private VehicleCategoryFragment vehicleCategoryFragment;
    private ReservationFragment ReservationFragment;
    private AccountFragment accountFragment;
    private MapsFragment MapsFragment;
    private String loggedInCustomerID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guest_view);

        initComponents();
        setFragment(vehicleCategoryFragment);

        clickListener();

    }

    private void clickListener() {
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                switch (menuItem.getItemId()){


                    case R.id.nav_account :
                        setFragment(accountFragment, loggedInCustomerID);
                        return true;

                    case R.id.nav_search :
                        setFragment(MapsFragment, loggedInCustomerID);
                        return true;
                }

                return false;
            }
        });
    }

    private void setFragment(Fragment fragment,String Data) {
        Bundle bundle = new Bundle();
        bundle.putString("CUSTOMERID",Data);
        fragment.setArguments(bundle);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.framelayout,fragment);
        fragmentTransaction.commit();
    }

    private void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.framelayout,fragment);
        fragmentTransaction.commit();
    }

    private void initComponents(){
        bottomNavigationView = findViewById(R.id.bottom_nav);
        frameLayout = findViewById(R.id.framelayout);

        vehicleCategoryFragment = new VehicleCategoryFragment();
        ReservationFragment= new ReservationFragment();
        accountFragment = new AccountFragment();
        MapsFragment = new MapsFragment();
        loggedInCustomerID = getIntent().getStringExtra("CUSTOMERID");

    }
}
