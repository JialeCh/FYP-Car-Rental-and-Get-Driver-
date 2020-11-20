package com.example.ez_rental.activity.Feedback;

import android.app.Dialog;
import android.content.Context;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.res.ResourcesCompat;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.example.ez_rental.Model.Feedback;
import com.example.ez_rental.R;
import com.example.ez_rental.activity.helper.HttpParse;
import com.example.ez_rental.app.AppConfig;
import com.example.ez_rental.app.AppController;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.content.ContentValues.TAG;


public class Rating_Feedback {


    private Context context;

    private Dialog d;
    private int uRate;
    private ImageView imgRev1, imgRev2, imgRev3, imgRev4, imgRev5, avatarImg, closeBtn;
    private TextView  appTitle;
    private Button ctaBtn;
    private int rid,uid,cid;
    private EditText avatarMsg;
    private ArrayList<Feedback> list = new ArrayList<>();
    private ArrayList<Feedback> newlist = new ArrayList<>();
    String finalResult;

    HashMap<String, String> hashMap = new HashMap<>();
    HttpParse httpParse = new HttpParse();
    public static Rating_Feedback init(Context context) {
        Rating_Feedback feedback = new Rating_Feedback();
        feedback.context = context;
        return  feedback;
    }
    public void show() {
        d = new Dialog(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        if (inflater == null)
            return;
        View view = inflater.inflate(R.layout.view_rate_dialog, new LinearLayout(context), false);

        bindViews(view);
        initViews();
        d.setContentView(view);
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
        imgRev1 = view.findViewById(R.id.rev1_img);
        imgRev2 = view.findViewById(R.id.rev2_img);
        imgRev3 = view.findViewById(R.id.rev3_img);
        imgRev4 = view.findViewById(R.id.rev4_img);
        imgRev5 = view.findViewById(R.id.rev5_img);
        avatarImg = view.findViewById(R.id.avatar_img);
         avatarMsg = view.findViewById(R.id.avatar_msg);
        ctaBtn = view.findViewById(R.id.cta_btn);
        closeBtn = view.findViewById(R.id.close_btn);
        appTitle = view.findViewById(R.id.love_app);
    }



    private void initViews() {
        String loveThisApp = "How is your reservation experience ?";
        appTitle.setText(loveThisApp);
        setFilter(imgRev1, imgRev2, imgRev3, imgRev4, imgRev5);

        imgRev1.setOnClickListener(v -> {
            removeFilter(imgRev1);
            initIcons(context);
            setFilter(imgRev2, imgRev3, imgRev4, imgRev5);
            avatarImg.setImageDrawable(context.getResources().getDrawable(R.drawable.avatar_man_1));
            avatarImg.setImageDrawable(ResourcesCompat.getDrawable(context.getResources(), R.drawable.avatar_man_1, null));
            ctaBtn.setText(R.string.GiveFeeback);
            uRate=2;
            ctaBtn.setEnabled(true);
            ctaBtn.setBackground(context.getResources().getDrawable(R.drawable.round_corners_fill));
        });

        imgRev2.setOnClickListener(v -> {
            removeFilter(imgRev1, imgRev2);
            initIcons(context);
            setSameImg(context.getResources().getDrawable(R.drawable.emoji_angry), imgRev1, imgRev2);
            setFilter(imgRev3, imgRev4, imgRev5);
            avatarImg.setImageDrawable(context.getResources().getDrawable(R.drawable.avatar_girl_1));
            ctaBtn.setText(R.string.GiveFeeback);
            uRate=4;
            ctaBtn.setEnabled(true);
            ctaBtn.setBackground(context.getResources().getDrawable(R.drawable.round_corners_fill));
        });

        imgRev3.setOnClickListener(v -> {
            removeFilter(imgRev1, imgRev2, imgRev3);
            initIcons(context);
            setSameImg(context.getResources().getDrawable(R.drawable.emoji_thinking), imgRev1, imgRev2, imgRev3);
            setFilter(imgRev4, imgRev5);
            avatarImg.setImageDrawable(context.getResources().getDrawable(R.drawable.avatar_boy));
            uRate=6;
            ctaBtn.setText(R.string.GiveFeeback);
            ctaBtn.setEnabled(true);
            ctaBtn.setBackground(context.getResources().getDrawable(R.drawable.round_corners_fill));
        });

        imgRev4.setOnClickListener(v -> {
            removeFilter(imgRev1, imgRev2, imgRev3, imgRev4);
            initIcons(context);
            setSameImg(context.getResources().getDrawable(R.drawable.emoji_happy), imgRev1, imgRev2, imgRev3, imgRev4);
            setFilter(imgRev5);
            uRate=8;
            avatarImg.setImageDrawable(context.getResources().getDrawable(R.drawable.avatar_man));
            ctaBtn.setText(R.string.GiveFeeback);
            ctaBtn.setEnabled(true);
            ctaBtn.setBackground(context.getResources().getDrawable(R.drawable.round_corners_fill));
        });

        imgRev5.setOnClickListener(v -> {
            removeFilter(imgRev1, imgRev2, imgRev3, imgRev4, imgRev5);
            initIcons(context);
            setSameImg(context.getResources().getDrawable(R.drawable.emoji_in_love), imgRev1, imgRev2, imgRev3, imgRev4, imgRev5);
            avatarImg.setImageDrawable(context.getResources().getDrawable(R.drawable.avatar_girl));
            ctaBtn.setText(R.string.GiveFeeback);
            uRate=10;
            ctaBtn.setEnabled(true);
            ctaBtn.setBackground(context.getResources().getDrawable(R.drawable.round_corners_fill));
        });

        closeBtn.setOnClickListener(v -> {
            d.dismiss();
        });

        ctaBtn.setOnClickListener(v-> {

            Log.e(TAG, "inside" +cid);
            ReservationUpdate( rid);
            java.util.Date dt = new java.util.Date();
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String currentTime = sdf.format(dt);
            String feedID = String.valueOf(uid+rid);
            String msg = avatarMsg.getText().toString();
            InsertFeedback(feedID,rid,cid,msg,uRate,currentTime,uid,1000001);

        });
    }

    private void setFilter(ImageView... imageViews) {
        ColorMatrix matrix = new ColorMatrix();
        matrix.setSaturation(0);
        ColorMatrixColorFilter filter = new ColorMatrixColorFilter(matrix);
        for (ImageView imgv : imageViews) {
            imgv.setColorFilter(filter);
        }
    }

    private void setSameImg(Drawable image, ImageView... imageViews) {
        for (ImageView imgV : imageViews) {
            imgV.setImageDrawable(image);
        }
    }

    private void removeFilter(ImageView... imageViews) {
        for (ImageView imgV : imageViews) {
            imgV.setColorFilter(null);
        }
    }

    private void initIcons(Context context) {
        imgRev1.setImageDrawable(context.getResources().getDrawable(R.drawable.emoji_crying));
        imgRev2.setImageDrawable(context.getResources().getDrawable(R.drawable.emoji_angry));
        imgRev3.setImageDrawable(context.getResources().getDrawable(R.drawable.emoji_thinking));
        imgRev4.setImageDrawable(context.getResources().getDrawable(R.drawable.emoji_happy));
        imgRev5.setImageDrawable(context.getResources().getDrawable(R.drawable.emoji_in_love));
    }


    public Rating_Feedback  setData(int rid, int uid, int cid) {
        this.rid = rid;
        this.uid = uid;
        this.cid = cid;
        return this;
    }


    public void InsertFeedback( final String Feedback_ID,
                                final int Reserve_Id,
                                final int Car_Id,
                                final String User_Feedback,
                                final int User_Rating,
                                final String Feedback_Date,
                                final int User_ID,
                                final int Admin_Id) {
        String tag_string_req = "req_makeFeedback";
        StringRequest strReq = new StringRequest(Request.Method.POST, AppConfig.URL_makeFeedbck, response -> {
            int jsonStart = response.indexOf("{");
            int jsonEnd = response.lastIndexOf("}");

            if (jsonStart >= 0 && jsonEnd >= 0 && jsonEnd > jsonStart) {
                response = response.substring(jsonStart, jsonEnd + 1);
            } else {
            }

            response = response.replaceAll("<.*?>", "");
            try {
                JSONObject jObj = new JSONObject(response);
                boolean error = jObj.getBoolean("error");
                if (!error) {
                    Toast.makeText(context.getApplicationContext(), "Feedback successfully response", Toast.LENGTH_LONG).show();
                    d.dismiss();


                } else {
                    String errorMsg = jObj.getString("error_msg");
                    Toast.makeText(context.getApplicationContext(),  errorMsg, Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(context.getApplicationContext(),   e.toString(), Toast.LENGTH_LONG).show();
            }

        }, error -> {
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("Feedback_ID", Feedback_ID);
                params.put("Reserve_Id", String.valueOf(Reserve_Id));
                params.put("Car_Id",String.valueOf(Car_Id));
                params.put("User_Feedback",User_Feedback);
                params.put("User_Rating",String.valueOf(User_Rating));
                params.put("Feedback_Date",Feedback_Date);
                params.put("User_ID", String.valueOf(User_ID));
                params.put("Admin_Id", String.valueOf(Admin_Id));
                return params;
            }
        };

        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
        Log.e(TAG, "hi" );

    }

    public void ReservationUpdate( final int Reserve_ID) {
        class ReservationUpdateClass extends AsyncTask<String, Void, String> {

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
                hashMap.put("Reserve_ID", params[0]);
                finalResult = httpParse.postRequest(hashMap, AppConfig.URL_UpdateReserve);

                return finalResult;
            }
        }
        ReservationUpdateClass UpdateClass = new  ReservationUpdateClass();
        UpdateClass.execute(String.valueOf(Reserve_ID));
    }






}
