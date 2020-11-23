package com.example.ez_rental.activity.Reservation;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ez_rental.Model.Car;
import com.example.ez_rental.Model.Reservation;
import com.example.ez_rental.R;
import com.example.ez_rental.activity.User.UserViewActivity;



public class ReservationCompleteActivity extends AppCompatActivity {

    private Button back;
    private int days;
    private TextView name, email, phoneNumber;
    private String dname,dcontact,demail;
    private double  rateInsurance;
    private TextView bookingID, vehicleName, rate, totalDays, _pickup, _return, insurance, insuranceRate, totalCost;
    private Car car;
    private Reservation reservation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_complete);
        car = (com.example.ez_rental.Model.Car) getIntent().getSerializableExtra("Car");
        reservation = (com.example.ez_rental.Model.Reservation) getIntent().getSerializableExtra("Reservation");
        Intent intent = getIntent();
        days= intent.getIntExtra("Duration", days);
        dname=intent.getStringExtra("Driver_Name");
        dcontact=intent.getStringExtra("Driver_ContactNo");
        demail=intent.getStringExtra("Driver_Email");
        initComponents();
        listenHandler();
        displayCustomerInformation();
        displaySummary();
        displayTotalCost();
    }

    private void initComponents() {
        back = findViewById(R.id.back);
        name = findViewById(R.id.name);
        email = findViewById(R.id.adminID);
        phoneNumber = findViewById(R.id.addressText);
        vehicleName = findViewById(R.id.vehicleName);
        rate = findViewById(R.id.rate);
        totalDays = findViewById(R.id.totalDays);
        _pickup = findViewById(R.id.pickup);
        _return = findViewById(R.id.dropoff);
        insurance = findViewById(R.id.insurance);
        insuranceRate = findViewById(R.id.insuranceRate);
        totalCost = findViewById(R.id.totalCost);
        bookingID = findViewById(R.id.bookingID);
    }

    private void listenHandler() {
        back.setOnClickListener(v -> {
            Intent homePage = new Intent(ReservationCompleteActivity.this, UserViewActivity.class);
            homePage.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(homePage);
            finish();
        });
    }

    private void displayCustomerInformation() {

        name.setText(dname);
        email.setText( demail);
        phoneNumber.setText(dcontact);
    }

    private void displaySummary(){
        bookingID.setText("Reservation ID: "+ reservation.getReserve_ID());
        vehicleName.setText(car.getCar_Title());
        rate.setText("RM"+car.getPricePerDay()+"/Day");
        totalDays.setText(days+" Days");
        _pickup.setText(reservation.getRent_Date());
        _return.setText(reservation.getReturn_Date());
        insurance.setText(reservation.getInsurance());
        String chk = reservation.getInsurance();
        if (chk.contains("premium")){
            rateInsurance =20.0;
        }else if(chk.contains("basic")){
            rateInsurance =10.0;
        }
        else {
            rateInsurance =0.0;
        }
        insuranceRate.setText("RM "+ rateInsurance);
    }

    private void displayTotalCost(){
        totalCost.setText("RM "+ reservation.getTotal_Amount());
    }

    public void onBackPressed(){
        super.onBackPressed();
        Intent homepage = new Intent(getApplicationContext(), UserViewActivity.class);
        homepage.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // Will clear out your activity history stack till now
        startActivity(homepage);
        finish();
    }
}
