package com.example.ez_rental.activity.carDriver;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.ez_rental.Adapter.Car_DriverAdapter;
import com.example.ez_rental.Model.Car_Driver;
import com.example.ez_rental.R;
import com.example.ez_rental.app.AppConfig;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ViewCarDriver extends AppCompatActivity implements Car_DriverAdapter.onCar_DriverListener{

    private RecyclerView recyclerView;
    private TextView txtResult;
    private ImageView btnBack;
    private ArrayList<Car_Driver> list = new ArrayList<>();
    private ArrayList<Car_Driver> newlist = new ArrayList<>();
    SwipeRefreshLayout swipeRefreshLayout;
    private Car_DriverAdapter adapter;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_car_driver);
        init();
        Intent intent = getIntent();

        adapter=loadProducts();
        checkAdapter(list);
        swipeRefreshLayout = findViewById(R.id.swiperefresh);
        swipeRefreshLayout.setOnRefreshListener(() -> {
            adapter=loadProducts();
            adapter.notifyDataSetChanged();
            swipeRefreshLayout.setRefreshing(false);
        });

        swipeRefreshLayout.setColorScheme(android.R.color.holo_blue_dark,
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
        btnBack.setOnClickListener(v -> finish());
    }

    private void checkAdapter(ArrayList<Car_Driver> adapters){
        if(adapters.isEmpty()){
            txtResult.setVisibility(View.VISIBLE);
        }
        else {
            txtResult.setVisibility(View.INVISIBLE);
        }
    }
    private Car_DriverAdapter loadProducts() {

        StringRequest stringRequest = new StringRequest(Request.Method.GET, AppConfig.URL_CD,
                response -> {
                    try {
                        //converting the string to json array object
                        JSONArray array = new JSONArray(response);
                        list.clear();
                        //traversing through all the object
                        for (int i = 0; i < array.length(); i++) {
                            //getting product object from json array
                            JSONObject product = array.getJSONObject(i);
                            //adding the product to product list
                            list.add(new Car_Driver(
                                    product.getInt("CD_ID"),
                                    product.getString("CD_Name"),
                                    product.getString("CD_BOD"),
                                    product.getString("gender"),
                                    product.getString("phone_no"),
                                    product.getString("language_speak"),
                                    product.getInt("CD_LicenseNo"),
                                    product.getString("CD_License_ExpiryDate"),
                                    product.getString("CD_Status"),
                                    product.getString("reason"),
                                    product.getInt("Admin_Id")
                            ));
                        }
                    } catch (JSONException e) {
                        Toast.makeText( getApplicationContext(), e.toString() , Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                },
                error -> {

                });

        newlist.clear();
        for(int i=0; i<list.size();i++){
                newlist.add(list.get(i)) ;
        }
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(new LinearLayoutManager( getApplicationContext()));
        adapter = new Car_DriverAdapter(newlist, getApplicationContext(), this );
        recyclerView.setAdapter(adapter);
        //adding our string request to queue
        Volley.newRequestQueue( getApplicationContext()).add(stringRequest);
        checkAdapter(newlist);
        return adapter;
    }
    @Override
    public void onClick(int position) {

        Intent AddVehiclePage = new Intent(getApplicationContext(), EditCarDriverActivity.class);
        AddVehiclePage.putExtra("Car Driver", newlist.get(position));
        AddVehiclePage.putExtra("Edit", "edit");

        startActivity(AddVehiclePage);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.car_search_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return true;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return true;
            }
        });
        return true;
    }
}
