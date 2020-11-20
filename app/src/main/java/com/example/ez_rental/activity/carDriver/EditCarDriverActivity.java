package com.example.ez_rental.activity.carDriver;

import android.app.DatePickerDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ez_rental.Model.Car_Driver;
import com.example.ez_rental.R;
import com.example.ez_rental.activity.helper.HttpParse;
import com.example.ez_rental.app.AppConfig;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

import static com.example.ez_rental.app.AppController.TAG;


public class EditCarDriverActivity extends AppCompatActivity {

    private EditText brandname,phone,language,cdlicense,status;
    private Button back,addbrand;
    private RadioGroup bdesc;
    private Calendar current;
    private String CD_ID, CD_Name, CD_BOD, gender, phone_no, language_speak, CD_LicenseNo, CD_License_ExpiryDate, CD_Status, reason, Admin_Id;
    private TextView expirydate,cd_id, birthdate,title;
    Car_Driver cd;
    HashMap<String, String> hashMap = new HashMap<>();
    String finalResult;
    HttpParse httpParse = new HttpParse();
    String activity;
    private int colorCode = -1;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.CANADA);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_driver);

        cd =  (Car_Driver) getIntent().getSerializableExtra("Car Driver");
        activity = getIntent().getStringExtra("Edit");
        initComponent();
        listenHandler();

    }

    private void initComponent(){
        title = findViewById(R.id.vehicleTitle);
        title.setText("Edit Car Driver");
        brandname = findViewById(R.id.brandname);
        phone = findViewById(R.id.phone);
        language = findViewById(R.id.language);
        cdlicense = findViewById(R.id.cdlicense);
        expirydate = findViewById(R.id.expirydate);
        status = findViewById(R.id.status);
        cd_id = findViewById(R.id.cd_id);
        back = findViewById(R.id.back);
        addbrand = findViewById(R.id.addbrand);
        bdesc = findViewById(R.id.bdesc);

        birthdate = findViewById(R.id.birthdate);
        current = Calendar.getInstance();
        if(activity.contains("edit")){
            addbrand.setText("Update");
        }
        cd_id.setText("Driver ID:"+cd.getCD_ID());
        expirydate.setText(" "+cd.getCD_License_ExpiryDate());
        birthdate.setText(" "+cd.getCD_BOD());
        brandname.setText(cd.getCD_Name());
        phone.setText(cd.getPhone_no()+"");
        language.setText(cd.getLanguage_speak());
        cdlicense.setText(cd.getCD_LicenseNo()+"");
        status.setText(cd.getCD_Status());
        status.setEnabled(true);
        expirydate.setOnClickListener(v -> openCalendar(current, expirydate));
        birthdate.setOnClickListener(v -> openCalendar(current, birthdate));
        RadioButton radYes = findViewById(R.id.radioButton);
        RadioButton radYes2 = findViewById(R.id.radioButton2);
        if(cd.getGender().contains("Male")){
            radYes.setChecked(true);
            gender ="Male";
        }else{
            radYes2.setChecked(true);
            gender ="Female";
        }


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
            CD_Status= status.getText().toString();
            reason ="none";


            CDUpdate(String.valueOf(cd.getCD_ID()), CD_Name, CD_BOD, gender, phone_no, language_speak, CD_LicenseNo, CD_License_ExpiryDate, CD_Status, reason);
            finish();
        }));
    }

    private void openCalendar(final Calendar rentalDate, final TextView rentalDateText) {
        DatePickerDialog datePickerDialog = new DatePickerDialog(this);

        datePickerDialog.setOnDateSetListener((view, year, month, dayOfMonth) -> {
            rentalDate.set(year,month,dayOfMonth);
            rentalDateText.setText(dateFormat.format(rentalDate.getTime()));
        });
        datePickerDialog.show();
    }




    public void CDUpdate(final String CD_ID,final String  CD_Name,final String  CD_BOD,
                         final String  gender,final String  phone_no,
                         final String  language_speak,final String  CD_LicenseNo,
                         final String  CD_License_ExpiryDate,
                         final String  CD_Status,final String  reason) {
        class CDClass extends AsyncTask<String, Void, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(String httpResponseMsg) {
                super.onPostExecute(httpResponseMsg);
                Log.e(TAG,CD_ID+CD_Name+CD_BOD+gender+phone_no+language_speak+CD_LicenseNo+CD_License_ExpiryDate+CD_Status+reason);
            }
            @Override
            protected String doInBackground(String... params) {
                hashMap.put("CD_ID", params[0]);
                hashMap.put("CD_Name", params[1]);
                hashMap.put("CD_BOD", params[2]);
                hashMap.put("gender", params[3]);
                hashMap.put("phone_no", params[4]);
                hashMap.put("language_speak", params[5]);
                hashMap.put("CD_LicenseNo", params[6]);
                hashMap.put("CD_License_ExpiryDate", params[7]);
                hashMap.put("CD_Status", params[8]);
                hashMap.put("reason", params[9]);

                finalResult = httpParse.postRequest(hashMap, AppConfig.URL_updateCd);

                return finalResult;
            }
        }
        CDClass UpdateClass = new  CDClass();
        UpdateClass.execute(CD_ID,CD_Name,CD_BOD,gender,phone_no,language_speak,CD_LicenseNo,CD_License_ExpiryDate,CD_Status,reason);
    }
}
