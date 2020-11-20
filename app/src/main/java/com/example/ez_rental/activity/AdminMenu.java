package com.example.ez_rental.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.GridLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.ez_rental.R;
import com.example.ez_rental.activity.brand.ViewBrandActivity;
import com.example.ez_rental.activity.car.ViewCar;
import com.example.ez_rental.activity.carDriver.ViewCarDriver;

public class AdminMenu extends AppCompatActivity {
    GridLayout mainGrid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_menu);

        mainGrid = findViewById(R.id.gridlayout);

        setToggleEvent(mainGrid);

    }

    private void setToggleEvent(GridLayout mainGrid) {

        for (int i = 0; i < mainGrid.getChildCount(); i++) {

            final CardView cardView = (CardView) mainGrid.getChildAt(i);
            final int finalI = i;
            cardView.setOnClickListener(view -> {
                if (cardView.getCardBackgroundColor().getDefaultColor() == -1) {

                    switch (finalI) {
                        case 0: {
                            Intent AddVehiclePage = new Intent(getApplicationContext(), ViewUser.class);
                            startActivity(AddVehiclePage);
                        }
                        break;

                        case 1: {
                            Intent AddVehiclePage = new Intent(getApplicationContext(), ViewCar.class);
                            startActivity(AddVehiclePage);
                        }

                        break;
                        case 2: {
                            Intent AddVehiclePage = new Intent(getApplicationContext(), ViewCarDriver.class);
                            startActivity(AddVehiclePage);
                        }

                        break;
                        case 3: {
                            Intent AddVehiclePage = new Intent(getApplicationContext(), ViewBrandActivity.class);
                            startActivity(AddVehiclePage);
                            break;
                        }

                    }

                } else {
                    //Change background color
                    cardView.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
                    Toast.makeText(getApplicationContext(), "This is activity from card item index  " + finalI, Toast.LENGTH_LONG).show();
                }
            });
        }

    }
}
