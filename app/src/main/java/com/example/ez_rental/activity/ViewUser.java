package com.example.ez_rental.activity;

import android.content.Intent;
import android.os.Bundle;
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
import com.example.ez_rental.Adapter.UserAdapter;
import com.example.ez_rental.Model.User;
import com.example.ez_rental.R;
import com.example.ez_rental.app.AppConfig;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ViewUser extends AppCompatActivity implements UserAdapter.onUserListener {

    private RecyclerView recyclerView;
    private TextView txtResult;
    private ImageView btnBack;
    private ArrayList<User> list = new ArrayList<>();
    private ArrayList<User> newlist = new ArrayList<>();
    SwipeRefreshLayout swipeRefreshLayout;
    private UserAdapter adapter;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_view);
        init();
        Intent intent = getIntent();

        adapter=loadProducts();

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


    private UserAdapter loadProducts() {

        StringRequest stringRequest = new StringRequest(Request.Method.GET, AppConfig.HttpURL_getUser,
                response -> {
                    try {

                        JSONArray array = new JSONArray(response);
                        list.clear();
                        //traversing through all the object
                        for (int i = 0; i < array.length(); i++) {
                            //getting product object from json array
                            JSONObject user = array.getJSONObject(i);
                            //adding the product to product list
                            list.add(new User(
                           user.getString("User_ID"),
                           user.getString("Username"),
                           user.getString("User_Email"),
                            user.getString("User_ContactNo"),
                            user.getString("Address"),
                            user.getString("User_Password"),
                             user.getString("User_Profile"),
                                    user.getString("Driver_license"),
                            user.getString("License_ExpiryDate")
                            ));
                        }
                    } catch (JSONException e) {
                        Toast.makeText( getApplicationContext(), e.toString() , Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                },
                error -> {

                });


        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(new LinearLayoutManager( getApplicationContext()));
        adapter = new UserAdapter(list, getApplicationContext(), this );
        recyclerView.setAdapter(adapter);
        //adding our string request to queue
        Volley.newRequestQueue( getApplicationContext()).add(stringRequest);

        return adapter;
    }
    @Override
    public void onClick(int position) {

    }



}
