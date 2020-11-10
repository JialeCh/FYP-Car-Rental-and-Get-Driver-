package com.example.ez_rental.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class payment extends AppCompatActivity implements View.OnClickListener {

    private static final String ALLOWED_CHARACTERS ="0123456789qwertyuiopasdfghjklzxcvbnm";
    //The views
    private Button buttonPay;
    private TextView editTextAmount;
    private int Reservation_ID,Car_id;
    //Payment Amount
    private Double total =0.0;
    private Double paymentAmount;
    private String method ="Paypal";
    private RadioGroup paymentMethod;
    private String phone_no;
    String HttpURL = "http://192.168.1.3/android_login_api/UpdateReservationCash.php";

    String finalResult;
    HashMap<String, String> hashMap = new HashMap<>();
    HttpParse httpParse = new HttpParse();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        Intent intent2 = getIntent();;

        total = intent2.getDoubleExtra("Total", total);
        Reservation_ID = intent2.getIntExtra("Reservation_ID",Reservation_ID);
        Car_id = intent2.getIntExtra("Car_Id",Car_id);
        phone_no = intent2.getStringExtra("phone_no");
        buttonPay = (Button) findViewById(R.id.buttonPay);
        editTextAmount =  findViewById(R.id.editTextAmount);
        editTextAmount.setText("RM: "+total.toString());
        paymentMethod= findViewById(R.id.radMethod);
        paymentMethod.setOnCheckedChangeListener((group, checkedId) -> {
            RadioButton radYes = findViewById(R.id.radCash);
            if(radYes.isChecked()){
                method ="Cash";
            }

            else {
                method ="Paypal";}
        });
        buttonPay.setOnClickListener(v -> {
            if (method.contains("Cash")){
                java.util.Date dt = new java.util.Date();
                java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String currentTime = sdf.format(dt);
                String Payment_ID ="PAYID-"+ random();
                SQLiteHelper db;
                db = new SQLiteHelper(getApplicationContext());
                HashMap<String, String> user = db.getUserDetails();
                int uid = Integer.parseInt(user.get("User_ID"));
                makePayment(Payment_ID,  String.valueOf(total), currentTime, "Cash", "Pending", 0, Reservation_ID,uid);
                Toast.makeText(getApplicationContext(),"Pending Cash Payment",Toast.LENGTH_LONG).show();
                finish();
            }else
            {   getPayment();

            }
        });
       /* Intent intent = new Intent(this, PayPalService.class);

        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);

        startService(intent);*/


    }
    public static String random() {
        final Random random=new Random();
        final StringBuilder sb=new StringBuilder(10);
        for(int i=0;i<10;++i)
            sb.append(ALLOWED_CHARACTERS.charAt(random.nextInt(ALLOWED_CHARACTERS.length())));
        return sb.toString();
    }
    @Override
    public void onClick(View v) {
        getPayment();
    }
    public static final int PAYPAL_REQUEST_CODE = 123;


    //Paypal Configuration Object
    private static PayPalConfiguration config = new PayPalConfiguration()

            .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
            .clientId(AppConfig.PAYPAL_CLIENT_ID);


    private void getPayment() {

        paymentAmount =  total;
        String text = "Reservation ID: "+ Reservation_ID ;

        PayPalPayment payment = new PayPalPayment(new BigDecimal(paymentAmount), "MYR", text,
                PayPalPayment.PAYMENT_INTENT_SALE);
        Intent intent = new Intent(this, PaymentActivity.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payment);
        startActivityForResult(intent, PAYPAL_REQUEST_CODE);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //If the result is from paypal
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PAYPAL_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                PaymentConfirmation confirm = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                if (confirm != null) {
                    try {
                        String paymentDetails = confirm.toJSONObject().toString(4);
                        Intent Confirmation = new Intent(this,ConfirmationActivity.class);
                        Confirmation.putExtra("Reservation_ID",Reservation_ID);
                        Confirmation.putExtra("PaymentDetails", paymentDetails);
                        Confirmation.putExtra("PaymentAmount", paymentAmount);
                        Confirmation.putExtra("Car_Id", Car_id);
                        Confirmation.putExtra("phone_no", phone_no);
                        startActivity(Confirmation);
                        finish();
                    } catch (JSONException e) {
                        Log.e("paymentExample", "an extremely unlikely failure occurred: ", e);
                    }
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Log.i("paymentExample", "The user canceled.");
            } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
                Log.i("paymentExample", "An invalid Payment or PayPalConfiguration was submitted. Please see the docs.");
            }
        }
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
        ReservationUpdate(Reserve_ID,Car_id,phone_no);
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