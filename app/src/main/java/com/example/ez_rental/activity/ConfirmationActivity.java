package com.example.ez_rental.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.example.ez_rental.R;
import com.example.ez_rental.app.AppConfig;
import com.example.ez_rental.app.AppController;
import com.example.ez_rental.helper.HttpParse;
import com.example.ez_rental.helper.SQLiteHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static android.content.ContentValues.TAG;

public class ConfirmationActivity extends AppCompatActivity {
    private String Payment_ID;
    private int Card_No;
    private int Reserve_ID;
    private int Car_Id;
    private String phone_no;
    private String Payment_Amount;
    String HttpURL = "http://192.168.1.3/android_login_api/UpdateReservation.php";

    String finalResult;
    HashMap<String, String> hashMap = new HashMap<>();
    HttpParse httpParse = new HttpParse();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmation);

        //Getting Intent
        Intent intent = getIntent();
        Reserve_ID = intent.getIntExtra("Reservation_ID",Reserve_ID);
        Car_Id = intent.getIntExtra("Car_Id",Car_Id);
        phone_no = intent.getStringExtra("phone_no");
        Log.d(TAG,"Car ID" + Car_Id);

        try {
            JSONObject jsonDetails = new JSONObject(intent.getStringExtra("PaymentDetails"));
            showDetails(jsonDetails.getJSONObject("response"), intent.getStringExtra("PaymentAmount"));
            finish();
        } catch (JSONException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void showDetails(JSONObject jsonDetails, String paymentAmount) throws JSONException {
        //Views
        SQLiteHelper db;
        db = new SQLiteHelper(getApplicationContext());
        HashMap<String, String> user = db.getUserDetails();
        int uid = Integer.parseInt(user.get("User_ID"));
        TextView textViewId = findViewById(R.id.paymentId);
        TextView textViewStatus= findViewById(R.id.paymentStatus);
        TextView textViewAmount = findViewById(R.id.paymentAmount);
        Payment_ID =jsonDetails.getString("id");
        Payment_Amount= paymentAmount;
        java.util.Date dt = new java.util.Date();
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currentTime = sdf.format(dt);

        Card_No = 0;

        makePayment(Payment_ID, Payment_Amount, currentTime, "PayPal", "Paid", Card_No, Reserve_ID,uid);

        textViewId.setText(jsonDetails.getString("id"));
        textViewStatus.setText(jsonDetails.getString("state"));
        textViewAmount.setText(" RM " +paymentAmount);
    }

    private void makePayment(final String Payment_ID,
                                 final String Payment_Amount,
                                 final String Payment_Date,
                                 final String Payment_Method,
                                 final String Payment_Status, final int Card_No,
                                 final int Reserve_ID,
                             final int User_ID
                                 ) {
        String tag_string_req = "req_payment";
        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_makePayment, response -> {
            int jsonStart = response.indexOf("{");
            int jsonEnd = response.lastIndexOf("}");

            if (jsonStart >= 0 && jsonEnd >= 0 && jsonEnd > jsonStart) {
                response = response.substring(jsonStart, jsonEnd + 1);
            } else {

            }
            response = response.replaceAll("<.*?>", "");
            try {
                JSONObject jObj = new JSONObject(response);
                boolean error = jObj.getBoolean("error");
                if (!error) {
                    Toast.makeText(getApplicationContext(), "Payment successfully make", Toast.LENGTH_LONG).show();
                } else {
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> Toast.makeText(getApplicationContext(),
                error.getMessage(), Toast.LENGTH_LONG).show()) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("Payment_ID", Payment_ID);
                params.put("Payment_Amount",Payment_Amount);
                params.put("Payment_Date",Payment_Date);
                params.put("Payment_Method",Payment_Method);
                params.put("Payment_Status",Payment_Status);
                params.put("Card_No", String.valueOf(Card_No));
                params.put("Reserve_ID", String.valueOf(Reserve_ID));
                params.put("User_ID", String.valueOf(User_ID));
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
        ReservationUpdate(Reserve_ID,Car_Id,phone_no);
    }
    public void ReservationUpdate(final int Reserve_ID, final int Car_Id, final String phone_no) {
        class ReservationUpdateClass extends AsyncTask<String, Void, String> {

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
                hashMap.put("phone_no", params[2]);
                finalResult = httpParse.postRequest(hashMap, HttpURL);

                return finalResult;
            }
        }
        ReservationUpdateClass UpdateClass = new  ReservationUpdateClass();
        UpdateClass.execute(String.valueOf(Reserve_ID),String.valueOf(Car_Id),phone_no);
    }

}

