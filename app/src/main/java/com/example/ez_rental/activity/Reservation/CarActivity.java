
package com.example.ez_rental.activity.Reservation;

import android.app.Activity;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.ez_rental.Model.Car;
import com.example.ez_rental.R;
import com.example.ez_rental.activity.Feedback.Feedback_List;
import com.example.ez_rental.activity.helper.SessionManager;

import java.util.ArrayList;
import java.util.List;


public class CarActivity extends Activity {

    private TextView carid, vehiclePrice,Overview,fuel_type,yearTxt,seatCap,brandId,brand_name,txtLocation,textView13,notAvailableText;

   private Button btnColor,btnViewRate,back;
    private ImageSlider  imageSlider;

    private Car car;

    private SessionManager session;
    List<SlideModel> slideModels=new ArrayList<>();

    private static final int IMAGE_PICKER_SELECT = 999;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_info);
        car = (Car) getIntent().getSerializableExtra("Car");
        init();
        listenHandler();


    }


    private void init(){
        imageSlider=findViewById(R.id.vehicleImage);
        back = findViewById(R.id.back);
        Overview = findViewById(R.id.Overview);
        vehiclePrice = findViewById(R.id.vehiclePrice);
        fuel_type = findViewById(R.id.fuel_type);
        yearTxt = findViewById(R.id.yearTxt);
        seatCap = findViewById(R.id.seatCap);
        brandId = findViewById(R.id.brandId);
        brand_name = findViewById(R.id.brand_name);
        txtLocation = findViewById(R.id.txtLocation);
        textView13= findViewById(R.id.textView13);
        btnColor = findViewById(R.id. btnColor);
        btnViewRate = findViewById(R.id.btnViewRate);
        carid = findViewById(R.id.carid);
        notAvailableText = findViewById(R.id.notAvailableText);


        int color = Integer.parseInt(car.getColor());
        /*color = color * -1;*/
        notAvailableText.setText("Color code:"+color);
        btnColor.setBackgroundTintList(ColorStateList.valueOf(color));
        if(car.getVImage1().compareTo("") !=0 ){
            slideModels.add(new SlideModel(car.getVImage1()));
        }
        if(car.getVImage2().compareTo("") !=0 ){
            slideModels.add(new SlideModel(car.getVImage2()));
        }
        if(car.getVImage3().compareTo("") !=0 ){
            slideModels.add(new SlideModel(car.getVImage3()));
        }

        imageSlider.setImageList(slideModels,true);
        Overview.setText(car.getCar_Overview());
        vehiclePrice.setText("Rm "+car.getPricePerDay()+"/day");
        fuel_type.setText(car.getFuel_Type());
        seatCap.setText(car.getSeating_Cap()+"");
        brandId.setText(car.getBrand_Id()+"");
        brand_name.setText(car.getBrand_Name());
        txtLocation.setText(car.getLocation());
        textView13.setText(car.getPlate_no()+"");
        carid.setText("Car ID:"+car.getCar_Id()+"("+car.getCar_Title()+")");

        btnViewRate.setOnClickListener(v -> {
            Feedback_List.init(CarActivity.this)
                    .setData(car.getCar_Id()).show();
        });
    }



    private void listenHandler(){
        back.setOnClickListener(v-> finish());

    }


    private void toast(String txt){
        Toast toast = Toast.makeText(getApplicationContext(),txt,Toast.LENGTH_LONG);
        toast.show();
    }



}
