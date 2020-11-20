
package com.example.ez_rental.activity.car;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Request.Method;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.ez_rental.Model.Brand;
import com.example.ez_rental.R;
import com.example.ez_rental.app.AppConfig;
import com.example.ez_rental.app.AppController;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

import yuku.ambilwarna.AmbilWarnaDialog;


public class AddCarActivity extends Activity {
    private static final String TAG = AddCarActivity.class.getSimpleName();
    private EditText car_name,car_title,caroverview,priceDay, modelYear,location,plate_not;
    private Button addImage,colorDisplay,addthis_car,back;

    private String Car_Id, Car_Title, Car_Overview, PricePerDay, Fuel_Type, ModelYear, Seating_Cap, VImage1, VImage2, VImage3, RegDate, UpdationDate, Car_Status, Admin_Id,
            Brand_Id, Brand_Name, Location, plate_no, color, rating, reason;
    private Spinner fueltype,brandid,seatcap;
    private  ImageSlider imageSlider;
    private int colorCode = -1;
    private ArrayList<Brand> list = new ArrayList<>();
    private ArrayList<Brand> newlist = new ArrayList<>();
    private  boolean chk1,chk2,chk3 = true;
    List<SlideModel> slideModels=new ArrayList<>();

    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.CANADA);
    Bitmap FixBitmap;
    byte[] image1;
    byte[] image2;
    byte[] image3;
    private int count =0;
    private static final int IMAGE_PICKER_SELECT = 999;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_vehicle);
        init();
        listenHandler();

    }


    private void init(){
        Car_Id = generateID();
        imageSlider=findViewById(R.id.vehicleImage);
        back = findViewById(R.id.back);
        car_name = findViewById(R.id.car_name);
        car_title = findViewById(R.id.car_title);
        caroverview = findViewById(R.id.caroverview);
        priceDay = findViewById(R.id.priceDay);
        fueltype = findViewById(R.id.fueltype);
        modelYear = findViewById(R.id.modelYear);
        seatcap = findViewById(R.id.seatcap);
        brandid = findViewById(R.id.brandid);
        location= findViewById(R.id.location);
        plate_not = findViewById(R.id.plate_no);
        addImage = findViewById(R.id.addImage);
        colorDisplay = findViewById(R.id.colorDisplay);
        addthis_car = findViewById(R.id.addthis_car);
        car_name.setText(Car_Id);
        car_name.setEnabled(false);
        car_name.setBackgroundColor(getResources().getColor(android.R.color.transparent));
        List<String> fuelType = new ArrayList<String>();
        fuelType.add(" ");
        fuelType.add("Petroleum 95");
        fuelType.add("Petroleum 97");
        fuelType.add("Diesel");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, fuelType);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        fueltype.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                Fuel_Type=  fueltype.getSelectedItem().toString();

                }



            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                chk1 = false;
                toast("Nothing Selected");
            }
        });

        List<String> seatCap = new ArrayList<String>();
        seatCap.add("1");
        seatCap.add("2");
        seatCap.add("3");
        seatCap.add("4");
        seatCap.add("5");
        seatCap.add("6");
        seatCap.add("7");
        seatCap.add("8");
        seatCap.add("9");
        seatCap.add("10");
        ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, seatCap);
        dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        seatcap.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
               Seating_Cap=  seatcap.getSelectedItem().toString();

            }



            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                toast("Nothing Selected");
                chk2 = false;
            }
        });
        loadProducts();
        brandid.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String name  = brandid.getSelectedItem().toString();
                   Brand_Name =name.substring(name.indexOf("-")+1);
                   Brand_Id= name.substring(0,name.indexOf("-"));
            }



            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                chk3 = false;
                toast("Nothing Selected");

            }
        });
        fueltype.setAdapter(dataAdapter);
        seatcap.setAdapter(dataAdapter2);

    }


    private void getData(){
        java.util.Date dt = new java.util.Date();
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currentTime = sdf.format(dt);


        Log.e(TAG,Brand_Name);
        Car_Title = car_title.getText().toString();
        Car_Overview = caroverview.getText().toString();
        PricePerDay = priceDay.getText().toString();
        ModelYear = modelYear.getText().toString();
        RegDate = currentTime;
        UpdationDate = currentTime;
        Car_Status = "Good";
        Admin_Id = "1000001";
        Location = location.getText().toString();
        color ="123";
        plate_no = plate_not.getText().toString();
        rating="0";
        reason =" ";
        VImage1="";
        VImage2="";
        VImage3="";

        if(Integer.parseInt(ModelYear) > 2020){
            toast("Invalid Model Year");
        }else if (chk1 = false){
            toast("Please select a fuel type");
        }else if(chk2 = false){
            toast("Please provide the seating cap");
        }else if(chk3 = false){
            toast("Please select the brand ID");
        }
        else{
            registerCar(Car_Id, Car_Title, Car_Overview, PricePerDay, Fuel_Type, ModelYear, Seating_Cap, VImage1, VImage2, VImage3, RegDate, UpdationDate, Car_Status, Admin_Id,
                    Brand_Id, Brand_Name, Location, plate_no, String.valueOf(colorCode), rating, reason);

            for(int i =0 ; i < count ; i++){
                if (i == 0){
                    image1 =  image1;
                }else if( i ==1 ){
                    image1 = image2;
                }else
                {
                    image1 = image3;
                }
                Intent Update = new Intent(getApplicationContext(), CarImageUploadActivity.class);
                Update.putExtra("Image1", image1);
                Update.putExtra("ImageLocation", i);
                Update.putExtra("Car_Id", Car_Id);
                startActivity(Update);
            }
            finish();
        }

    }
    private String generateID(){
        Random rnd = new Random();
        int id = 3000 + rnd.nextInt(65)*3;
        String userid = String.valueOf(id);
        return userid;
    }
    private void listenHandler(){
        back.setOnClickListener(v-> finish());
        addthis_car.setOnClickListener(view -> getData());
        colorDisplay.setOnClickListener(v -> openColorDialog());

        addImage.setOnClickListener(view -> {
            Intent i = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(i, IMAGE_PICKER_SELECT);

        });
    }
    public void openColorDialog(){
        AmbilWarnaDialog ambilWarnaDialog = new AmbilWarnaDialog(this, colorCode, new AmbilWarnaDialog.OnAmbilWarnaListener() {
            @Override
            public void onCancel(AmbilWarnaDialog dialog) {

            }

            @Override
            public void onOk(AmbilWarnaDialog dialog, int color) {
                colorCode = color;
                colorDisplay.setBackgroundTintList(ColorStateList.valueOf(color));
                toast(""+color);
            }
        });
        ambilWarnaDialog.show();
    }
    private void toast(String txt){
        Toast toast = Toast.makeText(getApplicationContext(),txt,Toast.LENGTH_LONG);
        toast.show();
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == IMAGE_PICKER_SELECT && resultCode == RESULT_OK && data != null){
            Uri uri = data.getData();

            try {
                ContentResolver resolver = getApplication().getContentResolver();
                FixBitmap = MediaStore.Images.Media.getBitmap(resolver, uri);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                FixBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                count = count + 1;
                switch (count){
                    case 1:
                        image1 = stream.toByteArray(); slideModels.add(new SlideModel(String.valueOf(uri)));
                        imageSlider.setImageList(slideModels,true);
                        break;
                    case 2:
                        image2 = stream.toByteArray();
                        slideModels.add(new SlideModel(String.valueOf(uri)));
                        imageSlider.setImageList(slideModels,true);break;
                    case 3:
                        image3 = stream.toByteArray();
                        slideModels.add(new SlideModel(String.valueOf(uri)));
                        imageSlider.setImageList(slideModels,true);break;
                    default:
                        toast("Maximum Pictures can be uploaded only 3");break;
                }


            } catch (IOException e) {
                e.printStackTrace();
            }


        }

        super.onActivityResult(requestCode, resultCode, data);
    }
    private void registerCar(final String Car_Id, final String  Car_Title, final String  Car_Overview, final String  PricePerDay, final String  Fuel_Type, final String  ModelYear, final String  Seating_Cap, final String  VImage1, final String  VImage2, final String  VImage3, final String  RegDate, final String  UpdationDate, final String  Car_Status, final String  Admin_Id, final String
            Brand_Id, final String  Brand_Name, final String  Location, final String  plate_no, final String  color, final String  rating, final String  reason) {
        // Tag used to cancel the request
        String tag_string_req = "req_add";

        StringRequest strReq = new StringRequest(Method.POST,
                AppConfig.Url_addCar, response -> {


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
                params.put("Car_Id", Car_Id);
                params.put("Car_Title", Car_Title);
                params.put("Car_Overview", Car_Overview);
                params.put("PricePerDay",PricePerDay);
                params.put("Fuel_Type", Fuel_Type);
                params.put("ModelYear", ModelYear);
                params.put("Seating_Cap", Seating_Cap);
                params.put("VImage1", VImage1);
                params.put("VImage2", VImage2);
                params.put("VImage3", VImage3);
                params.put("RegDate",  RegDate);
                params.put("UpdationDate", UpdationDate);
                params.put("Car_Status",Car_Status);
                params.put("Admin_Id", Admin_Id);
                params.put("Brand_Id", Brand_Id);
                params.put("Brand_Name", Brand_Name);
                params.put("Location", Location);
                params.put("plate_no", plate_no);
                params.put("color", color);
                params.put("rating", rating);
                params.put("reason", reason);
                return params;
            }

        };

        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void loadProducts() {

        StringRequest stringRequest = new StringRequest(Request.Method.GET, AppConfig.Url_viewBrand,
                response -> {
                    try {

                        JSONArray array = new JSONArray(response);
                        list.clear();
                        //traversing through all the object
                        for (int i = 0; i < array.length(); i++) {
                            //getting product object from json array
                            JSONObject product = array.getJSONObject(i);
                            //adding the product to product list
                            list.add(new Brand(

                                    product.getString("Brand_Id"),
                                    product.getString("Brand_Name"),
                                    product.getString("description"),
                                    product.getString("brand_status"),
                                    product.getString("reason"),
                                    product.getString("Creation_Date"),
                                    product.getString("Updated_Date"),
                                    product.getString("Admin_Id")
                            ));
                        }
                        newlist.clear();
                        List<String> BrandId = new ArrayList<String>();

                        for (int i = 0; i < list.size(); i++) {
                            BrandId.add(list.get(i).getBrand_Id()+"-"+list.get(i).getBrand_Name());
                        }

                        ArrayAdapter<String> dataAdapter3 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, BrandId);
                        dataAdapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        brandid.setAdapter(dataAdapter3);
                    } catch (JSONException e) {
                        Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                },
                error -> {

                });


        Volley.newRequestQueue(getApplicationContext()).add(stringRequest);

    }
}
