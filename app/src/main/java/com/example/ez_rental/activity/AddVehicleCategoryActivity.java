package com.example.ez_rental.activity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ez_rental.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Random;


public class AddVehicleCategoryActivity extends AppCompatActivity {

    private EditText brandname,phone,language,cdlicense,status;
    private Button back,addbrand;
    private RadioGroup bdesc;
    private Calendar current;
    private String CD_ID, CD_Name, CD_BOD, gender, phone_no, language_speak, CD_LicenseNo, CD_License_ExpiryDate, CD_Status, reason, Admin_Id;
    private TextView expirydate,cd_id;

    private int colorCode = -1;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.CANADA);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_driver);

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
        current = Calendar.getInstance();
        cd_id.setText("Driver ID:"+CD_ID);
        expirydate.setText(" "+dateFormat.format(current.getTime()));
        expirydate.setOnClickListener(v -> openCalendar(current, expirydate));
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
/*
        add.setOnClickLi
}stener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VehicleCategory vehicleCategory = createVehicleCategory();

                if(vehicleCategory != null){
                    vehicleCategoryDao.insert(vehicleCategory);
                    Log.d("MainActivity",vehicleCategory.getObject());
                    toast("Vehicle Category Added");
                }
            }
        });*/
/*
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vehicleCategoryDao.deleteAll();
                vehicleDao.deleteAll();
                toast("RESET");
            }
        });*/

        /*addVehicle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addVehiclePage = new Intent(AddVehicleCategoryActivity.this,AddVehicleActivity.class);
                startActivity(addVehiclePage);
            }
        });

        loadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!imageURL.getText().toString().equals("")){
                    Picasso.get().load(imageURL.getText().toString()).into(viewImage);
                }
            }
        });
*/

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
    /*
    * Category should not exist
    * ID should be unique
    * Color has been chosen => default white color
    * */
/*    private VehicleCategory createVehicleCategory(){

        String categoryName = category.getText().toString();
        String category_ID = categoryID.getText().toString();
        String image_URL = imageURL.getText().toString();

        if(!category_ID.equals("") && !vehicleCategoryDao.exists(Integer.valueOf(category_ID)) && !vehicleCategoryDao.exits(categoryName)) {
            return new VehicleCategory(categoryName,Integer.valueOf(category_ID),colorCode,image_URL);
        }

        if(category_ID.equals(""))
            toast("CategoryID is blank");
        else if(categoryName.equals(""))
            toast("Category name is blank");
        else if(vehicleCategoryDao.exists(Integer.valueOf(category_ID)))
            toast("CategoryID already exists");
        else if(image_URL.equals(""))
            toast("Please enter image URL");
        else
            toast("Category already exists");
        return null;
    }*/



    //DEBUGING
    private void toast(String txt){
        Toast toast = Toast.makeText(getApplicationContext(),txt,Toast.LENGTH_LONG);
        toast.show();
    }
}
