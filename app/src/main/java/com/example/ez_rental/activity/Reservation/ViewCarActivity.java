package com.example.ez_rental.activity.Reservation;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.SearchView;
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
import com.example.ez_rental.R;
import com.example.ez_rental.app.AppConfig;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class ViewCarActivity extends AppCompatActivity implements CarsAdapter.onCarsListener {

    private RecyclerView recyclerView;
    private TextView txtResult,show,rent,returnplace,date;
    private ImageView btnBack,btnList;
    private Spinner btnSort;
    private ArrayList<Car> list = new ArrayList<>();
    private ArrayList<Car> newlist = new ArrayList<>();
    SwipeRefreshLayout swipeRefreshLayout;
    private CarsAdapter adapter;
    private boolean check = true;
    private Calendar _pickup;
    String location = "";
    private SimpleDateFormat dateFormat2 = new SimpleDateFormat("EE", Locale.ENGLISH);
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.CANADA);
    String location2 = "";
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.car_listing);
        init();
        Intent intent = getIntent();
        location = intent.getStringExtra("location");
        location2 = intent.getStringExtra("location2");
        adapter =loadProducts(location);
        adapter.notifyDataSetChanged();
        checkAdapter(adapter);
        swipeRefreshLayout.setOnRefreshListener(() -> {
            init();
            adapter =loadProducts(location);
            checkAdapter(adapter);
            swipeRefreshLayout.setRefreshing(false);
            adapter.notifyDataSetChanged();
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
        btnSort = findViewById(R.id.btnSort);
        btnList =findViewById(R.id.imageView17);
        show = findViewById(R.id.choose);
        rent = findViewById(R.id.rent);
        returnplace = findViewById(R.id.returnplace);
        date = findViewById(R.id.date);
        rent.setText(location);
        returnplace.setText(location2);
        _pickup = Calendar.getInstance();

        date.setText(dateFormat.format(_pickup.getTime()) +" | "+dateFormat2.format(_pickup.getTime()));
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
        btnBack.setOnClickListener(v ->  {
            Intent homepage = new Intent(getApplicationContext(), MapActivity.class);
            startActivity(homepage);
            finish();
        } );
        List<String> categories = new ArrayList<String>();
        categories.add(" ");
        categories.add("Car Name");
        categories.add("Car Rating");
        categories.add("Car Brand");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);
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
    private CarsAdapter loadProducts(String location) {
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
            String locate = String.valueOf(list.get(i).getLocation());
            if(location.contains(locate)){
                newlist.add(list.get(i)) ;
            }
        }
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CarsAdapter( newlist, this, this);
        recyclerView.setAdapter(adapter);
        Volley.newRequestQueue(this).add(stringRequest);
        return  adapter;
    }
    @Override
    public void onClick(int position) {
        Intent CarInfoPage = new Intent(this, CarInfoActivity.class);
        CarInfoPage.putExtra("Car", newlist.get(position));
        CarInfoPage.putExtra("Rent_Place", location);
        CarInfoPage.putExtra("Return_Place", location2);
        startActivity(CarInfoPage);


    }
    private void toast(String txt){
        Toast toast = Toast.makeText(getApplicationContext(),txt,Toast.LENGTH_LONG);
        toast.show();
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
