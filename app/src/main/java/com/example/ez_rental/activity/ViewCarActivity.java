package com.example.ez_rental.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.ez_rental.ActivityPages.CarInfoActivity;
import com.example.ez_rental.Adapter.CarsAdapter;
import com.example.ez_rental.Model.Car;
import com.example.ez_rental.R;
import com.example.ez_rental.app.AppConfig;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class ViewCarActivity extends AppCompatActivity implements CarsAdapter.onCarsListener {

    private RecyclerView recyclerView;
    private TextView txtResult;
    private ImageView btnBack,btnSort;
    private ArrayList<Car> list = new ArrayList<>();
    private ArrayList<Car> newlist = new ArrayList<>();
    SwipeRefreshLayout swipeRefreshLayout;
    private CarsAdapter adapter;
    String location = "";
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_vehicle);
        init();
        Intent intent = getIntent();
        location = intent.getStringExtra("location");
        adapter =loadProducts(location);

        checkAdapter(adapter);
        swipeRefreshLayout.setOnRefreshListener(() -> {
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
        btnBack.setOnClickListener(v -> finish());
        btnSort.setOnClickListener(v->adapter.sortNameByAsc());

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
            if(location .contains(locate)){
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
        startActivity(CarInfoPage);
    }
    public void sortNameByAsc() {
        Comparator<Car> comparator = (object1, object2) -> object1.getCar_Title().compareToIgnoreCase(object2.getCar_Title());
        Collections.sort(newlist, comparator);

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

/*    @Override
    public Filter getFilter() {
        return carFilter;
    }
    private Filter carFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            ArrayList<Car> filteredList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(newlist);
            } else {
                String filterPattern = constraint.toString().toLowerCase();
                for (Car item : newlist) {
                    if (item.getCar_Title().toLowerCase().contains(filterPattern)) {
                        filteredList.add(item);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            newlist.clear();
            newlist.addAll((ArrayList) results.values);

        }
    };*/
}
