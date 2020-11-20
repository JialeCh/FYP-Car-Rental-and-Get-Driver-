
package com.example.ez_rental.activity.car;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.ez_rental.Model.Brand;
import com.example.ez_rental.Model.Car;
import com.example.ez_rental.R;
import com.example.ez_rental.activity.helper.HttpParse;
import com.example.ez_rental.activity.helper.SessionManager;
import com.example.ez_rental.app.AppConfig;

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

import yuku.ambilwarna.AmbilWarnaDialog;


public class EditCarActivity extends Activity {
    private static final String TAG = AddCarActivity.class.getSimpleName();
    private EditText car_name,car_title,caroverview,priceDay, modelYear,location,plate_not,status;
    private LinearLayout linstatus;
    private Button addImage,colorDisplay,addthis_car,back,savecar;
    private TextView carid, text7,selectColor;
    private ImageView image18;
    private String Car_Id, Car_Title, Car_Overview, PricePerDay, Fuel_Type, ModelYear, Seating_Cap, VImage1, VImage2, VImage3, RegDate, UpdationDate, Car_Status, Admin_Id,
            Brand_Id, Brand_Name, Location, plate_no, color, rating, reason;
    private Spinner fueltype,brandid,seatcap;
    private  ImageSlider imageSlider;
    private int colorCode = -1;
    private Car car;
    private  boolean chk1,chk2,chk3 = true;
    private SessionManager session;
    List<SlideModel> slideModels=new ArrayList<>();
    HashMap<String, String> hashMap = new HashMap<>();
    String finalResult;
    HttpParse httpParse = new HttpParse();
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.CANADA);
    Bitmap FixBitmap;
    byte[] image1;
    byte[] image2;
    byte[] image3;
    private int count =0;
    private ArrayList<Brand> list = new ArrayList<>();
    private ArrayList<Brand> newlist = new ArrayList<>();
    private static final int IMAGE_PICKER_SELECT = 999;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_vehicle);
        car = (com.example.ez_rental.Model.Car) getIntent().getSerializableExtra("Car");
        init();
        listenHandler();
        session = new SessionManager(getApplicationContext());

        setData();
    }


    private void init(){
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
        carid = findViewById(R.id.vehicleTitle);
        addthis_car.setText("Edit");
        status = findViewById(R.id.status);
        linstatus = findViewById(R.id.linearStatus);
        savecar =findViewById(R.id.savecar);
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
    private void setData(){

        car_title.setText(car.getCar_Title());
        slideModels.add(new SlideModel(car.getVImage1()));
        slideModels.add(new SlideModel(car.getVImage2()));
        slideModels.add(new SlideModel(car.getVImage3()));
        imageSlider.setImageList(slideModels,true);
        colorDisplay.setBackgroundTintList(ColorStateList.valueOf(Integer.parseInt(car.getColor())));
        carid.setText("Car details ("+car.getCar_Id()+")");
        status.setText(car.getCar_Status());
        caroverview.setText(car.getCar_Overview());
        modelYear.setText(car.getModelYear()+"");

        plate_not.setText(car.getPlate_no()+"");

        location.setText(car.getLocation());

        priceDay.setText(car.getPricePerDay()+"");

        caroverview.setText(car.getCar_Overview());

        car_name.setText(car.getCar_Id()+"");

        linstatus.setVisibility(View.VISIBLE);
        addthis_car.setVisibility(View.GONE);
        savecar.setVisibility(View.VISIBLE);

    }

    private void getData(){
        java.util.Date dt = new java.util.Date();
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currentTime = sdf.format(dt);


        Car_Id = String.valueOf(car.getCar_Id());
        Car_Title = car_title.getText().toString();
        Car_Overview = caroverview.getText().toString();
        PricePerDay = priceDay.getText().toString();
        ModelYear = modelYear.getText().toString();
        RegDate = currentTime;
        UpdationDate = currentTime;
        Car_Status = status.getText().toString();
        Location = location.getText().toString();
        color = String.valueOf(colorCode);
        plate_no = plate_not.getText().toString();
        rating=" ";
        reason =" ";

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
            CarUpdate(Car_Id, Car_Title, Car_Overview, PricePerDay, Fuel_Type, ModelYear, Seating_Cap, UpdationDate, Car_Status,
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

    private void listenHandler(){
        back.setOnClickListener(v-> finish());

        colorDisplay.setOnClickListener(v -> openColorDialog());

        addImage.setOnClickListener(view -> {
            Intent i = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(i, IMAGE_PICKER_SELECT);

        });
        savecar.setOnClickListener(v -> getData());

    }
    public void openColorDialog(){
        AmbilWarnaDialog ambilWarnaDialog = new AmbilWarnaDialog(this, colorCode, new AmbilWarnaDialog.OnAmbilWarnaListener() {
            @Override
            public void onCancel(AmbilWarnaDialog dialog) {

            }

            @Override
            public void onOk(AmbilWarnaDialog dialog, int color) {
                colorCode = color;
                colorDisplay.setBackgroundTintList(ColorStateList.valueOf(-1487206));
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

    public void CarUpdate(final String Car_Id, final String  Car_Title,
                         final String  Car_Overview, final String  PricePerDay,
                         final String  Fuel_Type, final String  ModelYear,
                         final String  Seating_Cap, final String  UpdationDate,
                         final String  Car_Status, final String
            Brand_Id, final String  Brand_Name, final String  Location,
                         final String  plate_no, final String  color,
                         final String  rating, final String  reason) {
        class CDClass extends AsyncTask<String, Void, String> {

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
                hashMap.put("Car_Id", params[0]);
                hashMap.put("Car_Title", params[1]);
                hashMap.put("Car_Overview", params[2]);
                hashMap.put("PricePerDay", params[3]);
                hashMap.put("Fuel_Type", params[4]);
                hashMap.put("ModelYear", params[5]);
                hashMap.put("Seating_Cap", params[6]);
                hashMap.put("UpdationDate", params[7]);
                hashMap.put("Car_Status", params[8]);
                hashMap.put("Brand_Id", params[9]);
                hashMap.put("Brand_Name", params[10]);
                hashMap.put("Location", params[11]);
                hashMap.put("plate_no", params[12]);
                hashMap.put("color", params[13]);
                hashMap.put("rating", params[14]);
                hashMap.put("reason", params[15]);
                finalResult = httpParse.postRequest(hashMap, AppConfig.Url_updateCar);

                return finalResult;
            }
        }
        CDClass UpdateClass = new  CDClass();
        UpdateClass.execute(Car_Id,Car_Title,Car_Overview,PricePerDay,Fuel_Type,
                ModelYear,Seating_Cap,UpdationDate,Car_Status,Brand_Id,Brand_Name,Location,plate_no,color,rating,reason);
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
