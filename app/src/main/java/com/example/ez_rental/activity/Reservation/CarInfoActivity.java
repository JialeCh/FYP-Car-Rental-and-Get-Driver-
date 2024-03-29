package com.example.ez_rental.activity.Reservation;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.ez_rental.Model.Car;
import com.example.ez_rental.R;

import java.util.ArrayList;
import java.util.List;

public class CarInfoActivity extends AppCompatActivity {


    private Car car;
    private TextView vehicleTitle;
    private TextView vehiclePrice;
    private ConstraintLayout available;
    private ConstraintLayout notAvailable;
    private Button back;
    private Button book;
    private TextView year, Brand_Name, model, mileage, seats, type;
    private RadioGroup insuranceOption;
    private String chosenInsurance = "none";
    private String rent_place, return_place;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle_info);

        initComponents();
        listenHandler();
        displayVehicleInfo();

    }

    private void initComponents() {
        car = (com.example.ez_rental.Model.Car) getIntent().getSerializableExtra("Car");
        Intent intent = getIntent();
        rent_place= intent.getStringExtra("Rent_Place");
        return_place= intent.getStringExtra("Return_Place");
        back = findViewById(R.id.back);
        vehicleTitle = findViewById(R.id.vehicleTitle);
        available = findViewById(R.id.available);
        notAvailable = findViewById(R.id.notAvailable);
        year = findViewById(R.id.year);
        Brand_Name = findViewById(R.id.manufacturer);
        model = findViewById(R.id.model);
        mileage = findViewById(R.id.mileage);
        seats = findViewById(R.id.seats);
        type = findViewById(R.id.type);
        vehiclePrice = findViewById(R.id.vehiclePrice);
        insuranceOption = findViewById(R.id.insuranceOption);
        book = findViewById(R.id.book_this_car);

    }

    private void listenHandler() {


        back.setOnClickListener(v -> finish());


        book.setOnClickListener(v -> {
            Intent informationPage = new Intent(CarInfoActivity.this, ReserveCarActivity.class);
            informationPage.putExtra("Car",car);
            informationPage.putExtra("Insurance",chosenInsurance+"");
            informationPage.putExtra("Rent_Place",rent_place+"");
            informationPage.putExtra("Return_Place",return_place+"");
            startActivity(informationPage);

        });

        insuranceOption.setOnCheckedChangeListener((group, checkedId) -> {
            RadioButton option = findViewById(checkedId);
            chosenInsurance = option.getText().toString().toLowerCase();

        });


    }


    private void displayVehicleInfo() {
        vehicleTitle.setText(car.getCar_Title() +" "+ car.getModelYear());
        ImageSlider imageSlider=findViewById(R.id.vehicleImage);
        String id = String.valueOf(car.getCar_Id());
        List<SlideModel> slideModels=new ArrayList<>();
        slideModels.add(new SlideModel("http://192.168.1.13/Image/Upload/"+car.getVImage1()));
        slideModels.add(new SlideModel("http://192.168.1.13/Image/Upload/"+car.getVImage2()));
        slideModels.add(new SlideModel("http://192.168.1.13/Image/Upload/"+car.getVImage3()));
       ;
        imageSlider.setImageList(slideModels,true);

        if(car.getCar_Status().contains("Not")){

            available.setVisibility(ConstraintLayout.INVISIBLE);
            notAvailable.setVisibility(ConstraintLayout.VISIBLE);
            book.setEnabled(false);
            book.setBackground(ContextCompat.getDrawable(CarInfoActivity.this,R.drawable.disable_button));
            book.setText("Car Not Available");
        }else{
            available.setVisibility(ConstraintLayout.VISIBLE);
            notAvailable.setVisibility(ConstraintLayout.INVISIBLE);
            book.setEnabled(true);
            book.setBackground(ContextCompat.getDrawable(CarInfoActivity.this,R.drawable.round_button));
            book.setText("Reserve This Car");
        }
        vehiclePrice.setText("RM" + car.getPricePerDay()+"/Day");
        year.setText(car.getBrand_Name()+"");
        Brand_Name.setText( car.getColor());
        model.setText(id);
        mileage.setText(car.getLocation()+"");
        seats.setText(car.getSeating_Cap()+"");
        type.setText(car.getFuel_Type());
    }




}
