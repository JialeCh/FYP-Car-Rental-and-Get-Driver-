package com.example.ez_rental.activity.Payment;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ez_rental.Model.Payments;
import com.example.ez_rental.R;
import com.example.ez_rental.activity.User.UserViewActivity;
import com.example.ez_rental.activity.helper.SQLiteHelper;

import java.util.HashMap;

public class ViewTransactionActivity extends AppCompatActivity {

    private Button statusDot,back;
    private TextView name, user_email, user_phone;
    private TextView reserveID, card_no, payment_method, payment_Date, payment_status,totalCost,paymend_ID;
    private SQLiteHelper db;

    private Payments payment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_transaction);

        payment = (Payments) getIntent().getSerializableExtra("Payments");
        initComponents();
        listenHandler();
        displayCustomerInformation();
        displaySummary();
    }

    private void initComponents() {
       statusDot = findViewById(R.id.statusDot);
       name = findViewById(R.id.name);
       user_email=findViewById(R.id.user_email);
       user_phone=findViewById(R.id.user_phone);
       reserveID=findViewById(R.id.reserveID);
        card_no=findViewById(R.id.car_id);
       payment_method=findViewById(R.id.payment_method);
       payment_Date=findViewById(R.id.payment_Date);
       payment_status =findViewById(R.id.payment_status);
       totalCost=findViewById(R.id.totalCost);
       paymend_ID =findViewById(R.id.paymend_ID);
        back=findViewById(R.id.back);
    }

    private void listenHandler() {
        back.setOnClickListener(v -> finish());
    }

    private void displayCustomerInformation() {
        db = new SQLiteHelper(getApplicationContext());
        HashMap<String, String> user = db.getUserDetails();
        name.setText(user.get("Username"));
        user_email.setText( user.get("User_Email"));
        user_phone.setText(user.get("User_ContactNo"));
    }

    private void displaySummary()  {


        reserveID.setText("Reservation ID:" +payment.getReserve_ID());
        if(payment.getCard_No() == 0){
            card_no.setText("---");
        }else
        {
            card_no.setText(String.valueOf(payment.getCard_No()));
        }
        payment_method.setText(payment.getPayment_Method());
        payment_Date.setText(payment.getPayment_Date());
        totalCost.setText("RM :"+payment.getPayment_Amount());
        paymend_ID.setText(payment.getPayment_ID());
        payment_status.setText(payment.getPayment_Status());

        if(payment.getPayment_Status().contains("Pending")){

            statusDot.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.alert)));
        }else if(payment.getPayment_Status().contains("Paid")){
            statusDot.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.ic_launcher2_background)));
        }else {
            statusDot.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.tabSelectedTextColor)));
        }

    }


    public void onBackPressed(){
        super.onBackPressed();
        Intent homepage = new Intent(getApplicationContext(), UserViewActivity.class);
        homepage.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(homepage);
    }



}
