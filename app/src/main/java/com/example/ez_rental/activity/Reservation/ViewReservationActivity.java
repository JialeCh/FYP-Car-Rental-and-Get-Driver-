package com.example.ez_rental.activity.Reservation;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ContextThemeWrapper;

import com.example.ez_rental.Model.Reservation;
import com.example.ez_rental.R;
import com.example.ez_rental.activity.Feedback.Rating_Feedback;
import com.example.ez_rental.activity.User.UserViewActivity;
import com.example.ez_rental.activity.helper.HttpParse;
import com.example.ez_rental.app.AppConfig;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.HashMap;

import static android.content.ContentValues.TAG;

public class ViewReservationActivity extends AppCompatActivity {

    private Button back, returnCar,btnreturn,btnpayment;
    private ImageView btnCancel;
    private TextView name, email, phoneNumber;
    private TextView vehicleName, rate, totalDays, _pickup, _return, insurance, insuranceRate, totalCost,reservation_id;

    private double rateInsurance;
    private Reservation reservation;
    private long daydiff;
    double totalRate;
    String finalResult;
    HashMap<String, String> hashMap = new HashMap<>();
    HttpParse httpParse = new HttpParse();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_booking);

       reservation = (Reservation) getIntent().getSerializableExtra("Reservation");

        Log.d(TAG, "Registration Error: " + rate);

        initComponents();
        listenHandler();
        displayCustomerInformation();
        try {
            displaySummary();
        } catch (ParseException e) {
            e.printStackTrace();
        }
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
        returnCar = findViewById(R.id.returnCar);
        totalCost = findViewById(R.id.totalCost);
        reservation_id = findViewById(R.id.bookingID);
        btnreturn=findViewById(R.id.btnReturnCar);
        btnpayment=findViewById(R.id.payment);
        btnCancel=findViewById(R.id.btnCancel);
    }

    private void listenHandler() {
        back.setOnClickListener(v -> finish());
        btnreturn.setOnClickListener( v -> {
            if(reservation.getStatus().contains("Successful Reserved(Cash)")) {
                final AlertDialog dialog = new AlertDialog.Builder(new ContextThemeWrapper(ViewReservationActivity.this, R.style.AlertDialogCustom))
                        .setTitle("Alert Message")
                        .setMessage("Cash Payment has been completed ?")
                        .setPositiveButton("Yes", null)
                        .setNegativeButton("No", null)
                        .setIcon(getResources().getDrawable(R.drawable.ic_warning))
                        .show();
                Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                Button negativeButton = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
                positiveButton.setOnClickListener(x -> {

                    dialog.dismiss();
                    carUpdate(reservation.getReserve_ID(),reservation.getCar_Id());
                    finish();
                });

                negativeButton.setOnClickListener(x -> {
                    dialog.dismiss();
                });
            }else{
                final AlertDialog dialog = new AlertDialog.Builder(new ContextThemeWrapper(ViewReservationActivity.this, R.style.AlertDialogCustom))
                        .setTitle("Alert Message")
                        .setMessage("Confirm return car ? (User would not get any refund even return earlier）")
                        .setPositiveButton("Yes", null)
                        .setNegativeButton("No", null)
                        .setIcon(getResources().getDrawable(R.drawable.ic_warning2))
                        .show();
                Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                Button negativeButton = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
                positiveButton.setOnClickListener(x -> {

                    dialog.dismiss();
                    carUpdate(reservation.getReserve_ID(),reservation.getCar_Id());
                    finish();
                });

                negativeButton.setOnClickListener(x -> {
                    dialog.dismiss();
                });
            }

        });
        returnCar.setOnClickListener(v -> {

            Rating_Feedback.init(ViewReservationActivity.this)
                .setData(reservation.getReserve_ID(),reservation.getUser_ID(),reservation.getCar_Id()).show();
            returnCar.setEnabled(false);
            returnCar.setText("Feedback Responded");
            returnCar.setBackgroundResource(R.drawable.round_corners_fill_gray);
        });

        btnpayment.setOnClickListener(v->{
            Intent payment = new Intent(ViewReservationActivity.this, com.example.ez_rental.activity.Payment.payment.class);
            payment.putExtra("Total",reservation.getTotal_Amount());
            payment.putExtra("Reservation_ID",reservation.getReserve_ID());
            payment.putExtra("Car_Id",reservation.getCar_Id());
            startActivity(payment);
            finish();
        });

        btnCancel.setOnClickListener( v -> {
            final AlertDialog dialog = new AlertDialog.Builder(new ContextThemeWrapper(ViewReservationActivity.this, R.style.AlertDialogCustom))
                    .setTitle("Warning ")
                    .setMessage("Cancel this reservation ? (Only valid for reservation made in 24 hours )")
                    .setPositiveButton("Ok", null)
                    .setNegativeButton("Cancel", null)
                    .setIcon(getResources().getDrawable(R.drawable.ic_warning2))
                    .show();
            Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            Button negativeButton = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
            positiveButton.setOnClickListener(x -> {
                if(daydiff > 24 ){
                    Toast.makeText( getApplicationContext(), "Failed to cancel because reservation has done "+daydiff+" hours ago"  , Toast.LENGTH_SHORT).show();
                }else{
                    reserveDelete(reservation.getReserve_ID(),reservation.getCar_Id());
                    Toast.makeText( getApplicationContext(), "Reservation and Payment ("+reservation.getReserve_ID()+") deleted"  , Toast.LENGTH_SHORT).show();
                    finish();
                }

                dialog.dismiss();
            });
            negativeButton.setOnClickListener(x -> {
                dialog.dismiss();
            });
        });
    }

    private void displayCustomerInformation() {

       name.setText(reservation.getDriver_Name());
        email.setText( reservation.getDriver_Email());
        phoneNumber.setText(reservation.getDriver_ContactNo());
    }

    private void displaySummary() throws ParseException {
        if(reservation.getStatus().contains("Successful Reserved")){
            btnreturn.setVisibility(View.VISIBLE);
            returnCar.setVisibility(View.INVISIBLE);
            btnpayment.setVisibility(View.INVISIBLE);
        }else if(reservation.getStatus().contains("Reservation Completed")){
            btnreturn.setVisibility(View.INVISIBLE);
            returnCar.setVisibility(View.VISIBLE);
            btnpayment.setVisibility(View.INVISIBLE);
            btnCancel.setVisibility(View.INVISIBLE);
        }else if(reservation.getStatus().contains("Pending Payment")){
            btnreturn.setVisibility(View.INVISIBLE);
            returnCar.setVisibility(View.INVISIBLE);
            btnpayment.setVisibility(View.VISIBLE);
        }else if(reservation.getStatus().contains("Feedback Responded")){
            btnreturn.setVisibility(View.INVISIBLE);
            returnCar.setVisibility(View.VISIBLE);
            btnpayment.setVisibility(View.INVISIBLE);
            returnCar.setEnabled(false);
            returnCar.setText("Feedback Responded");
            returnCar.setBackgroundResource(R.drawable.round_corners_fill_gray);
            btnCancel.setVisibility(View.INVISIBLE);
        }else if(reservation.getStatus().contains("Successful Reserved(Cash)")){
            btnreturn.setText("Pay and Return Car");
            btnreturn.setVisibility(View.VISIBLE);
            returnCar.setVisibility(View.INVISIBLE);
            btnpayment.setVisibility(View.INVISIBLE);
        }else if(reservation.getStatus().contains("Cancelled")){
            btnreturn.setVisibility(View.INVISIBLE);
            returnCar.setVisibility(View.VISIBLE);
            btnpayment.setVisibility(View.INVISIBLE);
            returnCar.setEnabled(false);
            returnCar.setText("Reservation Cancelled");
            returnCar.setBackgroundResource(R.drawable.round_corners_fill_gray);
            btnCancel.setVisibility(View.INVISIBLE);
        }
        vehicleName.setText(reservation.getCar_Name());
       reservation_id.setText("Reservation ID :"+reservation.getReserve_ID());
        _pickup.setText(reservation.getRent_Date() + "("+reservation.getRent_Place()+")");
        _return.setText(reservation.getReturn_Date()+ "("+reservation.getReturn_Place()+")");
        totalCost.setText("RM" + reservation.getTotal_Amount());

        insurance.setText(reservation.getInsurance());
        String chk = reservation.getInsurance().toLowerCase();
        java.util.Date dt = new java.util.Date();
        Calendar cal = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        Calendar cal3 = Calendar.getInstance();
        Calendar cal4 = Calendar.getInstance();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currentTime = sdf.format(dt);

        cal.setTime(sdf.parse(reservation.getRent_Date()));
        cal2.setTime(sdf.parse(reservation.getReturn_Date()));
        cal3.setTime(sdf.parse(reservation.getReserve_Date()));
        cal4.setTime(sdf.parse(currentTime));

        daydiff= getHrsDifference(cal3,cal4);

        long days= getDayDifference(cal,cal2);
        totalDays.setText(String.valueOf(days));
        if (chk.contains("premium")) {
            rateInsurance = 20.0;
        } else if (chk.contains("basic")) {
            rateInsurance = 10.0;
        } else {
            rateInsurance = 0.0;
        }
        insuranceRate.setText("RM " + rateInsurance);
        double ratePerDay = (reservation.getTotal_Amount()  - rateInsurance) /days;
        ratePerDay = round(ratePerDay,2);
        rate.setText("RM "+ ratePerDay + "/Day");
    }
    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }
    private long getDayDifference(Calendar start, Calendar end){
        return ChronoUnit.DAYS.between(start.toInstant(), end.toInstant());
    }
    private long getHrsDifference(Calendar start, Calendar end){
        return ChronoUnit.HOURS.between(start.toInstant(), end.toInstant());
    }
    public void onBackPressed(){
        super.onBackPressed();
        Intent homepage = new Intent(getApplicationContext(), UserViewActivity.class);
        homepage.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(homepage);
    }

    public void carUpdate( final int Reserve_ID, final int Car_Id) {
        class carUpdateClass extends AsyncTask<String, Void, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(String httpResponseMsg) {
                super.onPostExecute(httpResponseMsg);
            }
            @Override
            protected String doInBackground(String... params) {
                hashMap.put("Reserve_ID", params[0]);
                hashMap.put("Car_Id", params[1]);

                finalResult = httpParse.postRequest(hashMap, AppConfig.HttpURL_CarStatus);

                return finalResult;
            }
        }
        carUpdateClass UpdateClass = new  carUpdateClass();
        UpdateClass.execute(String.valueOf(Reserve_ID),String.valueOf(Car_Id));
    }





    public void reserveDelete( final int Reserve_Id, final int Car_Id) {
        class reserveDeleteClass extends AsyncTask<String, Void, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(String httpResponseMsg) {
                super.onPostExecute(httpResponseMsg);
            }
            @Override
            protected String doInBackground(String... params) {
                hashMap.put("Reserve_ID", params[0]);
                hashMap.put("Car_Id", params[1]);

                finalResult = httpParse.postRequest(hashMap, "http://192.168.1.3/android_login_api/Cancel_Reserve.php");

                return finalResult;
            }
        }
        reserveDeleteClass UpdateClass = new  reserveDeleteClass();
        UpdateClass.execute(String.valueOf(Reserve_Id),String.valueOf(Car_Id));
    }

}
