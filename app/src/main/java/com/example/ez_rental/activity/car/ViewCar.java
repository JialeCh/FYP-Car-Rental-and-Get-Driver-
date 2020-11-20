package com.example.ez_rental.activity.car;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.ez_rental.Adapter.CarsAdapter;
import com.example.ez_rental.Model.Car;
import com.example.ez_rental.Model.Feedback;
import com.example.ez_rental.R;
import com.example.ez_rental.activity.helper.HttpParse;
import com.example.ez_rental.app.AppConfig;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ViewCar extends AppCompatActivity implements CarsAdapter.onCarsListener {
    private RecyclerView recyclerView;
    private ArrayList<Car> list = new ArrayList<>();
    private ArrayList<Car> newlist = new ArrayList<>();
    private ArrayList<Feedback> list2 = new ArrayList<>();
    private ArrayList<Feedback> newlist2 = new ArrayList<>();
    private double totalRate;
    String finalResult;
    HashMap<String, String> hashMap = new HashMap<>();
    HttpParse httpParse = new HttpParse();
    SwipeRefreshLayout swipeRefreshLayout;
    private CarsAdapter adapter;
    private boolean check = true;
    private TextView txtResult,show;
    private ImageView btnBack,btnList,btnAdd;
    private Spinner btnSort;
    private int count = 0;






    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_vehicle);

        init();
        adapter=loadProducts();
        checkAdapter(adapter);
        swipeRefreshLayout = findViewById(R.id.swiperefresh);
        swipeRefreshLayout.setOnRefreshListener(() -> {
            adapter=loadProducts();
            checkAdapter(adapter);
            adapter.notifyDataSetChanged();
            swipeRefreshLayout.setRefreshing(false);
        });

        swipeRefreshLayout.setColorScheme(android.R.color.background_dark,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);


    }

    private void init(){
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setNestedScrollingEnabled(false);
        swipeRefreshLayout = findViewById(R.id.swiperefresh);
        btnBack = findViewById(R.id.btnBack);
        txtResult = findViewById(R.id.textView2);
        btnAdd = findViewById(R.id.btnAdd);
        btnAdd.setVisibility(View.VISIBLE);
        btnBack.setOnClickListener(v -> finish());
        btnAdd.setOnClickListener(v -> {
            Intent CarInfoPage = new Intent(getApplicationContext(), AddCarActivity.class);
            startActivity(CarInfoPage);
        });
        btnSort = findViewById(R.id.btnSort);
        btnList =findViewById(R.id.imageView17);
        show = findViewById(R.id.choose);
        btnList.setOnClickListener(v->{
            if(check){
                btnList.setImageDrawable(getResources().getDrawable(R.drawable.ic_down));
                toast("Descending Order");
                check = false;
            }else{
                btnList.setImageDrawable(getResources().getDrawable(R.drawable.ic_up));
                toast("Ascending Order");
                check = true;
            }
        });

        List<String> categories = new ArrayList<String>();
        categories.add(" ");
        categories.add("Car Name");
        categories.add("Car Rating");
        categories.add("Car Brand");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, categories);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        btnSort.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String value= btnSort.getSelectedItem().toString();

                if (value.contains("Name")){
                    if(check){
                        toast("Car List (Sort by Name :ASC)");
                        adapter.sortNameByAsc();
                    }else{
                        toast("Car List (Sort by Name :DESC)");
                        adapter.sortNameByDesc();
                    }
                }else if (value.contains("Brand")){
                    if(check){
                        toast("Car List (Sort by Brand :ASC)");
                        adapter.sortBrandByAsc();
                    }else{
                        toast("Car List (Sort by Brand :DESC)");
                        adapter.sortBrandByDesc();
                    }
                }else if (value.contains("Rating")) {
                    if (check) {
                        toast("Car List (Sort by Rating :ASC)");
                        adapter.sortRateByAsc();
                    } else {
                        toast("Car List (Sort by Rating :DESC)");
                        adapter.sortRateByDesc();
                    }
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                toast("Nothing Selected");
            }
        });
        btnSort.setAdapter(dataAdapter);
    }
    private void checkAdapter(CarsAdapter adapters){
        if(adapters == null){
            txtResult.setVisibility(View.VISIBLE);
        }
        else {
            txtResult.setVisibility(View.INVISIBLE);
        }
    }
    private CarsAdapter loadProducts() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, AppConfig.URL_Cars,
                response -> {
                    try {
                        JSONArray array = new JSONArray(response);
                        list.clear();

                        for (int i = 0; i < array.length(); i++) {
                            JSONObject product = array.getJSONObject(i);
                            list.add(new Car(
                                    product.getInt("Car_Id"),
                                    product.getString("Car_Title"),
                                    product.getString("Car_Overview"),
                                    product.getDouble("PricePerDay"),
                                    product.getString("Fuel_Type"),
                                    product.getInt("ModelYear"),
                                    product.getInt("Seating_Cap"),
                                    product.getString("VImage1"),
                                    product.getString("VImage2"),
                                    product.getString("VImage3"),
                                    product.getString("RegDate"),
                                    product.getString("UpdationDate"),
                                    product.getString("Car_Status"),
                                    product.getInt("Admin_Id"),
                                    product.getInt("Brand_Id"),
                                    product.getString("Brand_Name"),
                                    product.getString("Location"),
                                    product.getString("plate_no"),
                                    product.getString("color"),
                                    product.getInt("rating"),
                                    product.getString("reason")
                            ));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> {

                });
        newlist.clear();
        for(int i=0; i<list.size();i++){
            newlist.add(list.get(i));
        }
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        adapter = new CarsAdapter( newlist, getApplicationContext(), this);
        recyclerView.setAdapter(adapter);

        Volley.newRequestQueue(getApplicationContext()).add(stringRequest);

        return adapter;
    }
    @Override
    public void onClick(int position) {

        Intent CarInfoPage = new Intent(getApplicationContext(), EditCarActivity.class);
        CarInfoPage.putExtra("Car", newlist.get(position));
        startActivity(CarInfoPage);
    }

    private void toast(String txt){
        Toast toast = Toast.makeText(getApplicationContext(),txt,Toast.LENGTH_LONG);
        toast.show();
    }



}