package com.example.ez_rental.activity.Reservation;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.example.ez_rental.Model.Car;
import com.example.ez_rental.Model.Reservation;
import com.example.ez_rental.R;
import com.example.ez_rental.app.AppConfig;
import com.example.ez_rental.app.AppController;
import com.example.ez_rental.activity.helper.SQLiteHelper;
import com.github.ybq.android.spinkit.style.Wave;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class ReservationSummaryActivity extends AppCompatActivity {

    private Button back, book, payNow;
    private TextView name, email, phoneNumber;
    private TextView vehicleName, rate, totalDays, _pickup, _return, insurance, insuranceRate, totalCost;
    private ImageView vehicleImage;

    private SQLiteHelper db;
    private String startdate, enddate;
    private Car car;
    private String Insurance;
    private String rent_place, return_place,dname,dcontact,demail;
    private int Reservation_ID;
    private String status = "Pending Payment";
    private double rateInsurance,total;
    private boolean check = true;
    private int days;
    private ProgressBar paidLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_summary);
        car = (com.example.ez_rental.Model.Car) getIntent().getSerializableExtra("Car");
        Intent intent = getIntent();

        Insurance = intent.getStringExtra("Insurance");
        rent_place= intent.getStringExtra("Rent_Place");
        return_place= intent.getStringExtra("Return_Place");
        Log.e(TAG,"Return Place4"+return_place);
        startdate=intent.getStringExtra("Rent_Date");
        enddate= intent.getStringExtra("Return_Date");
        days= intent.getIntExtra("Duration", days);
        dname=intent.getStringExtra("Driver_Name");
        dcontact=intent.getStringExtra("Driver_ContactNo");
        demail=intent.getStringExtra("Driver_Email");
        initComponents();


        Wave wave = new Wave();
        paidLoading.setIndeterminateDrawable(wave);
        generateID();
        listenHandler();
        displayInformation();
        displaySummary();
        displayTotalCost();

    }

    private void initComponents() {
        back = findViewById(R.id.back);
        book = findViewById(R.id.book);
        payNow = findViewById(R.id.payNow);

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

        vehicleImage = findViewById(R.id.vehicleImage);

        paidLoading = findViewById(R.id.paidLoading);
        paidLoading.setVisibility(View.INVISIBLE);
    }

    private void listenHandler() {
        back.setOnClickListener(v -> finish());

        book.setOnClickListener(v -> {
            db = new SQLiteHelper(getApplicationContext());
            HashMap<String, String> user = db.getUserDetails();
            int uid = Integer.parseInt(user.get("User_ID"));

            java.util.Date dt = new java.util.Date();
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String currentTime = sdf.format(dt);

            Reservation newReserve = new Reservation(Reservation_ID,currentTime,startdate,
                    enddate,rent_place,return_place,total,uid,car.getCar_Id(), car.getCar_Title(),1000001,status,Insurance,dname,dcontact,demail);
           if(check == true){

                   makeReservation(Reservation_ID,currentTime,startdate,
                           enddate,rent_place,return_place,total,uid,car.getCar_Id(),car.getCar_Title(),1000001,status,Insurance,dname,dcontact,demail);
               }



            Intent reservationCompletePage = new Intent(ReservationSummaryActivity.this, ReservationCompleteActivity.class);
            reservationCompletePage.putExtra("Reservation",newReserve);
            reservationCompletePage.putExtra("Car",car);
            reservationCompletePage.putExtra("Duration",days);
            reservationCompletePage.putExtra("Driver_Name",dname);
            reservationCompletePage.putExtra("Driver_ContactNo",dcontact);
            reservationCompletePage.putExtra("Driver_Email",demail);
            startActivity(reservationCompletePage);
            finish();
        });

        payNow.setOnClickListener(v -> {
            paidLoading.setVisibility(View.VISIBLE);

            new Handler().postDelayed(() -> {
                paidLoading.setVisibility(View.INVISIBLE);
                payNow.setText("Paid");
                payNow.setEnabled(false);
                check=false;
                db = new SQLiteHelper(getApplicationContext());
                HashMap<String, String> user = db.getUserDetails();
                java.util.Date dt = new java.util.Date();
                java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String currentTime = sdf.format(dt);
                int uid = Integer.parseInt(user.get("User_ID"));
                makeReservation(Reservation_ID,currentTime,startdate,
                        enddate,rent_place,return_place,total,uid,car.getCar_Id(),car.getCar_Title(),1000001,status,Insurance,dname,dcontact,demail);

                Intent payment = new Intent(ReservationSummaryActivity.this, com.example.ez_rental.activity.Payment.payment.class);
                payment.putExtra("Total",total);
                payment.putExtra("Reservation_ID",Reservation_ID);
                payment.putExtra("Car_Id",car.getCar_Id());
                payment.putExtra("phone_no",dcontact);
                startActivity(payment);

            },4000);
        }

        );

    }
    private void displayInformation() {

       name.setText(dname);
        email.setText(demail);
        phoneNumber.setText(dcontact);
    }

    private void displaySummary(){

        vehicleName.setText(car.getCar_Title());
        rate.setText("RM"+car.getPricePerDay()+"/Day");
        totalDays.setText(days+" Days");
        _pickup.setText(startdate);
        _return.setText(enddate);
        insurance.setText(Insurance);
        String chk = Insurance.toLowerCase();
        if (Insurance.contains("premium")){
            rateInsurance =20.0;
        }else if(Insurance.contains("basic")){
            rateInsurance =10.0;
        }
        else {
            rateInsurance =0.0;
        }
        insuranceRate.setText("RM "+ rateInsurance);
        Picasso.get().load(car.getVImage1()).into(vehicleImage);
    }

    private void displayTotalCost(){

       double cost = calculateTotalCost();
       total=cost;
        totalCost.setText("RM "+cost);
    }



    private double calculateTotalCost(){
        long _days = days;
        double _vehicleRate = car.getPricePerDay();
        double _insuranceRate = rateInsurance;
        if(demail.contains("-")){
            _vehicleRate = _vehicleRate + 20.00;
        }
        double finalRate = Math.round(((_days*_vehicleRate) + _insuranceRate)*100.00 ) /100.00;

        return finalRate;
    }

    private void generateID(){
        Random r = new Random();
        int start = r.nextInt(1000 + 1);
        Reservation_ID= start;
    }
    private void toast(String txt){
        Toast toast = Toast.makeText(getApplicationContext(),txt,Toast.LENGTH_SHORT);
        toast.show();
    }

    private void makeReservation(final int Reserve_ID,
                                 final String Reserve_Date,
                                 final String Rent_Date,
                                 final String Return_Date,
                                 final String Rent_Place,
                                 final String Return_Place,
                                 final double Total_Amount,
                                 final int User_ID,
                                 final int Car_Id,
                                 final String Car_Name,
                                 final int Admin_Id,
                                 final String Status, final String Insurance,
                                 final String Driver_Name,
                                 final String Driver_ContactNo,
                                 final String Driver_Email) {
        // Tag used to cancel the request
        String tag_string_req = "req_register";
        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_makeReserve, response -> {

                    try {
                        JSONObject jObj = new JSONObject(response);
                        boolean error = jObj.getBoolean("error");
                        if (!error) {
                            Toast.makeText(getApplicationContext(), "Reservation successfully make. Try login now!", Toast.LENGTH_LONG).show();
                        } else {
                            String errorMsg = jObj.getString("error_msg");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }, error -> {
        }) {
            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<>();
                params.put("Reserve_ID", String.valueOf(Reserve_ID));
                params.put("Reserve_Date",Reserve_Date);
                params.put("Rent_Date",Rent_Date);
                params.put("Return_Date",Return_Date);
                params.put("Rent_Place",Rent_Place);
                params.put("Return_Place",Return_Place);
                params.put("Total_Amount", String.valueOf(Total_Amount));
                params.put("User_ID", String.valueOf(User_ID));
                params.put("Car_Id", String.valueOf(Car_Id));
                params.put("Car_Name", Car_Name);
                params.put("Admin_Id", String.valueOf(Admin_Id));
                params.put("Status",Status);
                params.put("Insurance",Insurance);
                params.put("Driver_Name", Driver_Name);
                params.put("Driver_ContactNo",Driver_ContactNo);
                params.put("Driver_Email",Driver_Email);
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

}
