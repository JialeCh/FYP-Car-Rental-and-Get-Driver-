package com.example.ez_rental.activity.brand;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.example.ez_rental.R;
import com.example.ez_rental.app.AppController;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Random;


public class addBrand extends AppCompatActivity {

    private EditText brandname,bdesc;
    private Button back,addbrand;

    private Calendar current;
    private String Brand_Id, Brand_Name, description, brand_status, reason, Creation_Date, Updated_Date, Admin_Id;
    private TextView bid;

    private int colorCode = -1;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.CANADA);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_brand);

        initComponent();
        listenHandler();

    }

    private void initComponent(){
       brandname = findViewById(R.id.brandname);
       bdesc = findViewById(R.id.bdesc);
       back = findViewById(R.id.back);
       bid = findViewById(R.id.bid);
       addbrand = findViewById(R.id.addbrand);
        Brand_Id = generateID();
       bid.setText("Brand ID:"+Brand_Id);

    }

    public void listenHandler(){
       back.setOnClickListener(v -> finish());
       addbrand.setOnClickListener(v -> {

           Brand_Name = brandname.getText().toString();
           description = bdesc.getText().toString();
           brand_status ="Normal";
           reason="none";

           Admin_Id="1000001";
           java.util.Date dt = new java.util.Date();
           java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
           String currentTime = sdf.format(dt);

           Creation_Date =currentTime;
           Updated_Date=currentTime;
           registerbrand(Brand_Id, Brand_Name, description, brand_status, reason, Creation_Date, Updated_Date, Admin_Id);
           finish();
       });

    }
    private String generateID(){
        Random rnd = new Random();
        int id = 2000 + rnd.nextInt(65)*3;
        String userid = String.valueOf(id);
        return userid;
    }



    //DEBUGING
    private void toast(String txt){
        Toast toast = Toast.makeText(getApplicationContext(),txt,Toast.LENGTH_LONG);
        toast.show();
    }
    private void registerbrand(final String Brand_Id,final String Brand_Name,final String description,final String brand_status,final String reason,final String Creation_Date,final String Updated_Date,final String Admin_Id) {
        // Tag used to cancel the request
        String tag_string_req = "req_add";

        StringRequest strReq = new StringRequest(Request.Method.POST,
                "http://192.168.1.3/android_login_api/addBrand.php", response -> {


            int jsonStart = response.indexOf("{");
            int jsonEnd = response.lastIndexOf("}");

            if (jsonStart >= 0 && jsonEnd >= 0 && jsonEnd > jsonStart) {
                response = response.substring(jsonStart, jsonEnd + 1);
            } else {
                // deal with the absence of JSON content here
            }

            response = response.replaceAll("<.*?>", "");
            try {
                JSONObject jObj = new JSONObject(response);
                boolean error = jObj.getBoolean("error");
                if (!error) {

                } else {


                    String errorMsg = jObj.getString("error_msg");
                    Toast.makeText(getApplicationContext(),
                            errorMsg, Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();

            }

        }, error -> {
        }) {
            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("Brand_Id", Brand_Id);
                params.put("Brand_Name", Brand_Name);
                params.put("description", description);
                params.put("brand_status",brand_status);
                params.put("reason", reason);
                params.put("Creation_Date", Creation_Date);
                params.put("Updated_Date", Updated_Date);
                params.put("Admin_Id", Admin_Id);

                return params;
            }

        };

        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }
}
