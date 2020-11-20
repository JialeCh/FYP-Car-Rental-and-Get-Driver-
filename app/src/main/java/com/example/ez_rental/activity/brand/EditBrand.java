package com.example.ez_rental.activity.brand;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.example.ez_rental.Model.Brand;
import com.example.ez_rental.R;
import com.example.ez_rental.app.AppConfig;
import com.example.ez_rental.app.AppController;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


public class EditBrand extends AppCompatActivity {

    private EditText brandname,bdesc,status,reasonBrand;
    private Button back,addbrand,save;

    private Calendar current;
    private String Brand_Id, Brand_Name, description, brand_status, reason, Creation_Date, Updated_Date, Admin_Id;
    private TextView bid,txt1,txt2,vehicleTitle;
    private Brand brand;
    private int colorCode = -1;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.CANADA);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_brand);
        brand = (com.example.ez_rental.Model.Brand) getIntent().getSerializableExtra("Brand");
        initComponent();
        listenHandler();

    }

    private void initComponent(){
       brandname = findViewById(R.id.brandname);
        vehicleTitle = findViewById(R.id.vehicleTitle);
       bdesc = findViewById(R.id.bdesc);
       txt1 = findViewById(R.id.txt1);
        txt2 = findViewById(R.id.txt2);
       back = findViewById(R.id.back);
       bid = findViewById(R.id.bid);
       addbrand = findViewById(R.id.addbrand);
       status=findViewById(R.id.status);
       reasonBrand = findViewById(R.id.reasonbrand);
       save = findViewById(R.id.savebrand);
       addbrand.setText("Edit");
        vehicleTitle.setText("");
       bid.setText(brand.getBrand_Id());
        bid.setEnabled(false);
        bid.setBackgroundResource(android.R.color.transparent);
       brandname.setText(brand.getBrand_Name());
        brandname.setEnabled(false);
        brandname.setBackgroundResource(android.R.color.transparent);
        bdesc.setText(brand.getDescription());
        bdesc.setEnabled(false);
        bdesc.setBackgroundResource(android.R.color.transparent);
    }

    public void listenHandler(){
       back.setOnClickListener(v -> finish());
       addbrand.setOnClickListener(v -> {
           txt1.setVisibility(View.VISIBLE);
           txt2.setVisibility(View.VISIBLE);
           status.setVisibility(View.VISIBLE);
           reasonBrand.setVisibility(View.VISIBLE);
           addbrand.setVisibility(View.GONE);
           save.setVisibility(View.VISIBLE);
           status.setEnabled(true);
           status.setBackgroundResource(android.R.drawable.editbox_background);
           vehicleTitle.setText("Edit Brand");
           reasonBrand.setEnabled(true);
           reasonBrand.setBackgroundResource(android.R.drawable.editbox_background);
           brandname.setEnabled(true);
           brandname.setBackgroundResource(android.R.drawable.editbox_background);

           bdesc.setEnabled(true);
           bdesc.setBackgroundResource(android.R.drawable.editbox_background);

           bid.setEnabled(true);
           bid.setBackgroundResource(android.R.drawable.editbox_background);
       });
       save.setOnClickListener(v -> {
           Brand_Id = brand.getBrand_Id();
           Brand_Name = brandname.getText().toString();
           description = bdesc.getText().toString();
           brand_status =status.getText().toString();
           reason=reasonBrand.getText().toString();

           Admin_Id="1000001";
           java.util.Date dt = new java.util.Date();
           SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
           String currentTime = sdf.format(dt);

           Creation_Date =brand.getCreation_Date();
           Updated_Date=currentTime;
           Updatebrand(Brand_Id, Brand_Name, description, brand_status, reason, Creation_Date, Updated_Date, Admin_Id);
           finish();
       });

    }

    private void Updatebrand(final String Brand_Id,final String Brand_Name,final String description,final String brand_status,final String reason,final String Creation_Date,final String Updated_Date,final String Admin_Id) {
        // Tag used to cancel the request
        String tag_string_req = "req_update";

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.Url_editBrand, response -> {


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
