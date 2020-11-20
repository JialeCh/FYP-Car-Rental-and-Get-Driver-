package com.example.ez_rental.activity.brand;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.ez_rental.Adapter.BrandAdapter;
import com.example.ez_rental.Model.Brand;
import com.example.ez_rental.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ViewBrandActivity extends AppCompatActivity implements BrandAdapter.onBrandListener {

    private RecyclerView recyclerView;
    private TextView txtResult;
    private ImageView btnBack;
    private ArrayList<Brand> list = new ArrayList<>();
    private ArrayList<Brand> newlist = new ArrayList<>();
    SwipeRefreshLayout swipeRefreshLayout;
    private BrandAdapter adapter;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_brand);
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

    private void checkAdapter(ArrayList<Brand> adapters){
        if(adapters.isEmpty()){
            txtResult.setVisibility(View.VISIBLE);
        }
        else {
            txtResult.setVisibility(View.INVISIBLE);
        }
    }
    private BrandAdapter loadProducts() {

        StringRequest stringRequest = new StringRequest(Request.Method.GET, "http://192.168.1.3/android_login_api/getBrand.php",
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
                    } catch (JSONException e) {
                        Toast.makeText( getApplicationContext(), e.toString() , Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                },
                error -> {

                });

        newlist.clear();
        for(int i=0; i<list.size();i++){
           /* String locate = list.get(i).getCD_Status();
            if(locate .contains("Available"))
            {*/
                newlist.add(list.get(i)) ;

        }
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(new LinearLayoutManager( getApplicationContext()));
        adapter = new BrandAdapter(newlist, getApplicationContext(), this );
        recyclerView.setAdapter(adapter);
        //adding our string request to queue
        Volley.newRequestQueue( getApplicationContext()).add(stringRequest);
        checkAdapter(newlist);
        return adapter;
    }
    @Override
    public void onClick(int position) {

        Intent CarInfoPage = new Intent(this, EditBrand.class);
        CarInfoPage.putExtra("Brand", newlist.get(position));

        startActivity(CarInfoPage);
    }



}
