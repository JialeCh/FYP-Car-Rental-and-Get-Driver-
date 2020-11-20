package com.example.ez_rental.activity.Feedback;

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
import com.example.ez_rental.Adapter.FeedbackAdapter;
import com.example.ez_rental.Model.Feedback;
import com.example.ez_rental.R;
import com.example.ez_rental.activity.helper.SQLiteHelper;
import com.example.ez_rental.app.AppConfig;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


public class ViewFeedbackActivity extends AppCompatActivity implements FeedbackAdapter.onFeedbackListener {

    private RecyclerView recyclerView;
    private SQLiteHelper db;
   private TextView txtResult;
   private ImageView btnclosing;
    private ArrayList<Feedback> list = new ArrayList<>();
    private ArrayList<Feedback> newlist = new ArrayList<>();
    SwipeRefreshLayout swipeRefreshLayout;
    private FeedbackAdapter adapter;
    private boolean check = true;
    String id = "";
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feedback_list);
        init();
        adapter =loadProducts(id);
        adapter.notifyDataSetChanged();
        checkAdapter(adapter);
        swipeRefreshLayout.setOnRefreshListener(() -> {
            init();
            adapter =loadProducts(id);
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
        db = new SQLiteHelper(getApplicationContext());
        HashMap<String, String> user = db.getUserDetails();
        id = user.get("User_ID");
        recyclerView = findViewById(R.id.feedbackList);
        recyclerView.setNestedScrollingEnabled(false);
        swipeRefreshLayout = findViewById(R.id.swiperefresh);
        txtResult = findViewById(R.id.textViewNofound);
        btnclosing = findViewById(R.id.btnclosing);
        btnclosing.setOnClickListener(v -> finish());
    }

    private void checkAdapter(FeedbackAdapter adapters){
        if(adapters == null){
            txtResult.setVisibility(View.VISIBLE);
        }
        else {
            txtResult.setVisibility(View.INVISIBLE);
        }
    }
    private FeedbackAdapter loadProducts(String Id) {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, AppConfig.URL_GetFeedback,
                response -> {
                    try {
                        JSONArray array = new JSONArray(response);
                        list.clear();

                        for (int i = 0; i < array.length(); i++) {
                            JSONObject product = array.getJSONObject(i);
                            list.add(new Feedback(
                                    product.getInt("Feedback_ID"),
                                      product.getInt("Reserve_Id"),
                                       product.getInt("Car_Id"),
                            product.getString("User_Feedback"),
                            product.getInt("User_Rating"),
                            product.getString("Feedback_Date"),
                            product.getInt("User_ID"),
                            product.getInt("Admin_Id")
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
            String locate = String.valueOf(list.get(i).getUser_ID());
            if(Id.contains(locate)){
                newlist.add(list.get(i)) ;
            }
        }
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new FeedbackAdapter( newlist, this, this);
        recyclerView.setAdapter(adapter);
        Volley.newRequestQueue(this).add(stringRequest);
        return  adapter;
    }

    private void toast(String txt){
        Toast toast = Toast.makeText(getApplicationContext(),txt,Toast.LENGTH_LONG);
        toast.show();
    }

    @Override
    public void onClick(int position) {
        Edit_Feedback.init(ViewFeedbackActivity.this)
                .setData(newlist.get(position)).show();
    }
}
