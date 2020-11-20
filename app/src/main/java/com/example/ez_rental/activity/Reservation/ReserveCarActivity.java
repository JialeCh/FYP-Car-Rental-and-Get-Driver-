package com.example.ez_rental.activity.Reservation;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ez_rental.Model.Car;
import com.example.ez_rental.R;
import com.example.ez_rental.activity.helper.SQLiteHelper;

import java.text.SimpleDateFormat;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import static android.content.ContentValues.TAG;

public class ReserveCarActivity extends AppCompatActivity  {

    private TextView pickupDate, returnDate;
    private TextView pickupTime, returnTime;
    private TextView driverDetailText;
    private Calendar _pickup;
    private Calendar _return;
    private static final int SECOND_ACTIVITY_REQUEST_CODE = 0;
    private EditText firstName, lastName, email, phoneNumber;
    private RadioGroup serviceTitle;
    private Car car;
    private String Insurance;
    private String rent_place, return_place;
    String User_ID;
    String Username ;
    String User_Email;
    String User_ContactNo ;
    private Button getDriver;
    private SQLiteHelper db;
    private String name;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.CANADA);
    private SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss", Locale.CANADA);
    private Button back, continueBooking, continueBooking2;
    private String startdate, enddate;
    private long daysDIff;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_car);
        car = (com.example.ez_rental.Model.Car) getIntent().getSerializableExtra("Car");

        Intent intent = getIntent();
        Insurance = intent.getStringExtra("Insurance");
        rent_place= intent.getStringExtra("Rent_Place");
        return_place= intent.getStringExtra("Return_Place");
        GetDataFromSQLite();
        initComponents();
        listenHandler();


    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SECOND_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                firstName.setText("Company Driver");
                lastName.setText(data.getStringExtra("name"));
                email.setText("-");
                phoneNumber.setText(data.getStringExtra("phone"));
                driverDetailText.setText("Driver Service (RM 20/ day)");
            }
        }
    }
    private void initComponents() {
        driverDetailText = findViewById(R.id.driverDetailText);
        back = findViewById(R.id.back);
        continueBooking2 = findViewById(R.id.continueBooking);
        continueBooking = findViewById(R.id.continueBooking2);
        continueBooking.setVisibility(View.INVISIBLE);
        getDriver = findViewById(R.id.btnGetDriver);
        pickupDate = findViewById(R.id.pickupDate);
        pickupTime = findViewById(R.id.pickupTime);
        returnDate = findViewById(R.id.returnDate);
        returnTime = findViewById(R.id.returnTime);
        serviceTitle = findViewById(R.id.mrMsTitle);
        firstName = findViewById(R.id.User_ID);
        lastName = findViewById(R.id.lastName);
        email = findViewById(R.id.adminID);
        phoneNumber = findViewById(R.id.addressText);
        _pickup = Calendar.getInstance();
        _return = Calendar.getInstance();
        pickupDate.setText(dateFormat.format(_pickup.getTime()));
        pickupTime.setText(timeFormat.format(_pickup.getTime()));
        returnDate.setText(dateFormat.format(_return.getTime()));
        returnTime.setText(timeFormat.format(_return.getTime()));
    }

    public void GetDataFromSQLite(){
        db = new SQLiteHelper(getApplicationContext());
        HashMap<String, String> user = db.getUserDetails();
        User_ID = user.get("User_ID");
        Username =user.get("Username");
        User_Email= user.get("User_Email");
        User_ContactNo = user.get("User_ContactNo");
    }
    private void listenHandler() {
        back.setOnClickListener(v -> finish());
        continueBooking2.setOnClickListener(v -> validate());
        pickupDate.setOnClickListener(v -> openCalendar(_pickup,pickupDate));
        pickupTime.setOnClickListener(v -> openTimePicker(_pickup, pickupTime));

        returnDate.setOnClickListener(v -> openCalendar(_return,returnDate));
        returnTime.setOnClickListener(v -> openTimePicker(_return, returnTime));

        continueBooking.setOnClickListener(v -> validate());
        getDriver.setOnClickListener(v -> {
            Intent intent = new Intent(this, ViewCDActivity.class);
            startActivityForResult(intent, SECOND_ACTIVITY_REQUEST_CODE);
        });

        serviceTitle.setOnCheckedChangeListener((group, checkedId) -> {
            RadioButton radYes = findViewById(R.id.mr);
            if(radYes.isChecked()){getDriver.setVisibility(View.VISIBLE);
                firstName.setText("Company Driver");
                lastName.setText("");
                email.setText("--");
                phoneNumber.setText("-");
                driverDetailText.setText("Driver Service (RM 20/ day)");
            }

            else {getDriver.setVisibility(View.INVISIBLE);
            firstName.setText("User");
            lastName.setText(Username);
            email.setText(User_Email);
            phoneNumber.setText(User_ContactNo);
            driverDetailText.setText("Driver Service");}
        });

    }


    private void validate() {
        startdate =  pickupDate.getText().toString() +" "+ pickupTime.getText().toString();
        enddate =  returnDate.getText().toString() +" "+ returnTime.getText().toString();
        daysDIff= getDayDifference(_pickup,_return);
        Log.d(TAG,"RESPONSE "+ daysDIff );
        String _firstName = firstName.getText().toString().toLowerCase();
        String _lastName = lastName.getText().toString().toLowerCase();
        String _email= email.getText().toString().toLowerCase();
        String _phoneNumber = phoneNumber.getText().toString();

        if(!fieldCheck(_firstName,_lastName,_email,_phoneNumber)  ) {
            Toast.makeText(getApplicationContext(), "Please complete the form", Toast.LENGTH_LONG).show();
        }
        else if(daysDIff < 1){
            Toast.makeText(getApplicationContext(), "Please provide proper duration of reservation", Toast.LENGTH_LONG).show();
        }
        else
        {
            int days = Math.toIntExact(daysDIff);
            Intent informationPage = new Intent(ReserveCarActivity.this, ReservationSummaryActivity.class);
            informationPage.putExtra("Car",car);
            informationPage.putExtra("Insurance",Insurance+"");
            informationPage.putExtra("Rent_Place",rent_place+"");
            informationPage.putExtra("Return_Place",return_place+"");
            informationPage.putExtra("Rent_Date",startdate+"");
            informationPage.putExtra("Return_Date",enddate+"");
            informationPage.putExtra("Driver_Name",lastName.getText().toString());
            informationPage.putExtra("Driver_ContactNo",phoneNumber.getText().toString());
            informationPage.putExtra("Driver_Email",email.getText().toString());
            informationPage.putExtra("Duration",days);
            startActivity(informationPage);
            finish();
        }


    }
    private boolean fieldCheck(String _firstName, String _lastName, String _email, String _phoneNumber) {
        return  !_firstName.equals("") && !_lastName.equals("") &&
                !_email.equals("") && !_phoneNumber.equals("");
    }

    private void openCalendar(final Calendar rentalDate, final TextView rentalDateText) {
        DatePickerDialog datePickerDialog = new DatePickerDialog(this);

        datePickerDialog.setOnDateSetListener((view, year, month, dayOfMonth) -> {
            rentalDate.set(year,month,dayOfMonth);
            rentalDateText.setText(dateFormat.format(rentalDate.getTime()));
        });
        datePickerDialog.show();
    }

    private Date openTimePicker(final Calendar rentalTime, final TextView rentalTimeText){
        final Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int min = calendar.get(Calendar.MINUTE);



        TimePickerDialog timePickerDialog = new TimePickerDialog(this, (view, hourOfDay, minute) -> {
            rentalTime.set(Calendar.HOUR_OF_DAY,hourOfDay);
            rentalTime.set(Calendar.MINUTE,minute);

            rentalTimeText.setText(timeFormat.format(rentalTime.getTime()));
        },hour,min,false);

        timePickerDialog.show();

        return calendar.getTime();
    }
    private void toast(String txt){
        Toast toast = Toast.makeText(getApplicationContext(),txt,Toast.LENGTH_SHORT);
        toast.show();
    }

    private long getDayDifference(Calendar start, Calendar end){
        return ChronoUnit.DAYS.between(start.toInstant(), end.toInstant());
    }
}
