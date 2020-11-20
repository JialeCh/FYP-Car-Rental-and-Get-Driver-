package com.example.ez_rental.activity.Feedback;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.ez_rental.Adapter.RatingAdapter;
import com.example.ez_rental.Model.Feedback;
import com.example.ez_rental.R;
import com.example.ez_rental.app.AppConfig;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class Feedback_List {


    private Context context;

    private Dialog d;
    private RecyclerView recyclerView;
    private ImageView  closeBtn;
    private TextView  appTitle;
    private RatingAdapter adapter;
    private int rid,uid,cid;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ArrayList<Feedback> list = new ArrayList<>();
    private ArrayList<Feedback> newlist = new ArrayList<>();
    public static Feedback_List init(Context context) {
        Feedback_List feedback = new Feedback_List();
        feedback.context = context;
        return  feedback;
    }
    public void show() {
        d = new Dialog(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        if (inflater == null)
            return;
        View view = inflater.inflate(R.layout.car_feedback, new LinearLayout(context), false);

        bindViews(view);

        initViews();
        d.setContentView(view);
        adapter=loadProducts(view);
        swipeRefreshLayout = view.findViewById(R.id.swiperefresh);
        swipeRefreshLayout.setOnRefreshListener(() -> {
            adapter=loadProducts(view);
            adapter.notifyDataSetChanged();
            swipeRefreshLayout.setRefreshing(false);
        });

        swipeRefreshLayout.setColorScheme(android.R.color.holo_blue_dark,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = d.getWindow();
        if (window != null) {
            window.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            lp.copyFrom(window.getAttributes());
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = WindowManager.LayoutParams.MATCH_PARENT;
            window.setAttributes(lp);
        }
        d.show();
    }


    private void bindViews(View view) {

        appTitle = view.findViewById(R.id.love_app);
        closeBtn = view.findViewById(R.id.close_btn);
        recyclerView = view.findViewById(R.id.recyleFeedback);
    }



    private void initViews() {
        String loveThisApp = "Overall Rating";
        appTitle.setText(loveThisApp);

        closeBtn.setOnClickListener(v -> {
            d.dismiss();
        });


    }


    private RatingAdapter loadProducts(View view) {
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
            String locate = String.valueOf(list.get(i).getCar_Id());
            if(locate.contains(String.valueOf(cid))){
                newlist.add(list.get(i)) ;
            }
        }
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        adapter = new RatingAdapter( newlist, view.getContext());
        recyclerView.setAdapter(adapter);
        Volley.newRequestQueue(view.getContext()).add(stringRequest);
      return adapter;
    }


    public Feedback_List setData( int cid) {
        this.cid = cid;
        return this;
    }











}
