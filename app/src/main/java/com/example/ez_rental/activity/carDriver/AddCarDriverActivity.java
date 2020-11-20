package com.example.ez_rental.activity.carDriver;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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


public class AddCarDriverActivity extends AppCompatActivity {

    private EditText brandname,phone,language,cdlicense,status;
    private Button back,addbrand;
    private RadioGroup bdesc;
    private Calendar current;
    private String CD_ID, CD_Name, CD_BOD, gender, phone_no, language_speak, CD_LicenseNo, CD_License_ExpiryDate, CD_Status, reason, Admin_Id;
    private TextView expirydate,cd_id, birthdate;

    private int colorCode = -1;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.CANADA);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_driver);
        gender = "Male";
        initComponent();
        listenHandler();

    }

    private void initComponent(){

        brandname = findViewById(R.id.brandname);
        phone = findViewById(R.id.phone);
        language = findViewById(R.id.language);
        cdlicense = findViewById(R.id.cdlicense);
        expirydate = findViewById(R.id.expirydate);
        status = findViewById(R.id.status);
        brandname = findViewById(R.id.brandname);
        cd_id = findViewById(R.id.cd_id);
        back = findViewById(R.id.back);
        addbrand = findViewById(R.id.addbrand);
        bdesc = findViewById(R.id.bdesc);
        CD_ID = generateID();
        birthdate = findViewById(R.id.birthdate);
        current = Calendar.getInstance();

        cd_id.setText("Driver ID:"+CD_ID);
        expirydate.setText(" "+dateFormat.format(current.getTime()));
        birthdate.setText(" "+dateFormat.format(current.getTime()));

        expirydate.setOnClickListener(v -> openCalendar(current, expirydate));
        birthdate.setOnClickListener(v -> openCalendar(current, birthdate));
        RadioButton radYes = findViewById(R.id.radioButton);
        radYes.setChecked(true);

    }

    public void listenHandler(){
        bdesc.setOnCheckedChangeListener((group, checkedId) -> {
            RadioButton radYes = findViewById(R.id.radioButton);
            if (radYes.isChecked()) {
                gender = "Male";
            } else {
                gender = "Female";
            }
        });
        back.setOnClickListener(v -> finish());
        addbrand.setOnClickListener((v -> {
            CD_Name = brandname.getText().toString();
            CD_BOD = birthdate.getText().toString();
            phone_no = phone.getText().toString();
            language_speak = language.getText().toString();
            CD_LicenseNo = cdlicense.getText().toString();
            CD_License_ExpiryDate =expirydate.getText().toString();
            CD_Status="Available";
            reason ="none";
            Admin_Id="1000001";

            registerCarDriver(CD_ID, CD_Name, CD_BOD, gender, phone_no, language_speak, CD_LicenseNo, CD_License_ExpiryDate, CD_Status, reason, Admin_Id);
            finish();
        }));
    }
    private String generateID(){
        Random rnd = new Random();
        int id = 4000 + rnd.nextInt(65)*3;
        String userid = String.valueOf(id);
        return userid;
    }
    private void openCalendar(final Calendar rentalDate, final TextView rentalDateText) {
        DatePickerDialog datePickerDialog = new DatePickerDialog(this);

        datePickerDialog.setOnDateSetListener((view, year, month, dayOfMonth) -> {
            rentalDate.set(year,month,dayOfMonth);
            rentalDateText.setText(dateFormat.format(rentalDate.getTime()));
        });
        datePickerDialog.show();
    }


    //DEBUGING
    private void toast(String txt){
        Toast toast = Toast.makeText(getApplicationContext(),txt,Toast.LENGTH_LONG);
        toast.show();
    }
    private void registerCarDriver(final String CD_ID,final String  CD_Name,final String  CD_BOD,final String  gender,final String  phone_no,final String  language_speak,final String  CD_LicenseNo,final String  CD_License_ExpiryDate,final String  CD_Status,final String  reason,final String  Admin_Id) {
        // Tag used to cancel the request
        String tag_string_req = "req_add";

        StringRequest strReq = new StringRequest(Request.Method.POST,
                "http://192.168.1.3/android_login_api/addCD.php", response -> {


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
                params.put("CD_ID", CD_ID);
                params.put("CD_Name", CD_Name);
                params.put("CD_BOD", CD_BOD);
                params.put("gender",gender);
                params.put("phone_no", phone_no);
                params.put("language_speak", language_speak);
                params.put("CD_LicenseNo", CD_LicenseNo);
                params.put("CD_License_ExpiryDate", CD_License_ExpiryDate);
                params.put("CD_Status",CD_Status);
                params.put("reason", reason);
                params.put("Admin_Id",  Admin_Id);

                return params;
            }

        };

        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }
}
