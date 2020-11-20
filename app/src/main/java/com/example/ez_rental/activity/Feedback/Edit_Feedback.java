package com.example.ez_rental.activity.Feedback;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
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

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.view.ContextThemeWrapper;

import com.example.ez_rental.Model.Feedback;
import com.example.ez_rental.R;
import com.example.ez_rental.activity.helper.HttpParse;
import com.example.ez_rental.app.AppConfig;

import java.util.HashMap;


public class Edit_Feedback {


    private Context context;
    private Dialog d;
    private int uRate;
    private ImageView imgRev1, imgRev2, imgRev3, imgRev4, imgRev5, avatarImg, closeBtn;
    private TextView  appTitle;
    private Button ctaBtn,btnDelete;
    private Feedback feedback;
    private EditText avatarMsg;

    String finalResult;
    HashMap<String, String> hashMap = new HashMap<>();
    HttpParse httpParse = new HttpParse();
    public static Edit_Feedback init(Context context) {
        Edit_Feedback feedback = new Edit_Feedback();
        feedback.context = context;
        return  feedback;
    }
    public void show() {
        d = new Dialog(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        if (inflater == null)
            return;
        View view = inflater.inflate(R.layout.view_editrate_dialog, new LinearLayout(context), false);

        bindViews(view);
        initViews();
        avatarMsg.setText(feedback.getUser_Feedback());

        switch(feedback.getUser_Rating()) {
            case 1:
            case 2:
                removeFilter(imgRev1);
                avatarImg.setImageDrawable(context.getResources().getDrawable(R.drawable.avatar_man_1));
                setFilter(imgRev2, imgRev3, imgRev4, imgRev5);
                break;
            case 3:
            case 4:
                removeFilter(imgRev1, imgRev2);

                setSameImg(context.getResources().getDrawable(R.drawable.emoji_angry), imgRev1, imgRev2);
                setFilter(imgRev3, imgRev4, imgRev5);break;
            case 5:
            case 6:
                removeFilter(imgRev1, imgRev2, imgRev3);
                setSameImg(context.getResources().getDrawable(R.drawable.emoji_thinking), imgRev1, imgRev2, imgRev3);
                setFilter(imgRev4, imgRev5);break;
            case 7:
            case 8:
                removeFilter(imgRev1, imgRev2, imgRev3, imgRev4);
                setSameImg(context.getResources().getDrawable(R.drawable.emoji_happy), imgRev1, imgRev2, imgRev3, imgRev4);
                setFilter(imgRev5);break;
            case 9:
            case 10:
                removeFilter(imgRev1, imgRev2, imgRev3, imgRev4, imgRev5);
                setSameImg(context.getResources().getDrawable(R.drawable.emoji_in_love), imgRev1, imgRev2, imgRev3, imgRev4, imgRev5);break;
        }
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
        btnDelete = view.findViewById(R.id.button);


    }



    @SuppressLint("SetTextI18n")
    private void initViews() {
        String loveThisApp = "How is your reservation experience ?";
        appTitle.setText(loveThisApp);
        setFilter(imgRev1, imgRev2, imgRev3, imgRev4, imgRev5);

        imgRev1.setOnClickListener(v -> {
            removeFilter(imgRev1);
            initIcons(context);
            setFilter(imgRev2, imgRev3, imgRev4, imgRev5);

            ctaBtn.setText("Update Feedback");
            uRate=2;
            ctaBtn.setEnabled(true);
            ctaBtn.setBackground(context.getResources().getDrawable(R.drawable.round_corners_fill));
        });

        imgRev2.setOnClickListener(v -> {
            removeFilter(imgRev1, imgRev2);
            initIcons(context);
            setSameImg(context.getResources().getDrawable(R.drawable.emoji_angry), imgRev1, imgRev2);
            setFilter(imgRev3, imgRev4, imgRev5);

            ctaBtn.setText("Update Feedback");
            uRate=4;
            ctaBtn.setEnabled(true);
            ctaBtn.setBackground(context.getResources().getDrawable(R.drawable.round_corners_fill));
        });

        imgRev3.setOnClickListener(v -> {
            removeFilter(imgRev1, imgRev2, imgRev3);
            initIcons(context);
            setSameImg(context.getResources().getDrawable(R.drawable.emoji_thinking), imgRev1, imgRev2, imgRev3);
            setFilter(imgRev4, imgRev5);

            uRate=6;
            ctaBtn.setText("Update Feedback");
            ctaBtn.setEnabled(true);
            ctaBtn.setBackground(context.getResources().getDrawable(R.drawable.round_corners_fill));
        });

        imgRev4.setOnClickListener(v -> {
            removeFilter(imgRev1, imgRev2, imgRev3, imgRev4);
            initIcons(context);
            setSameImg(context.getResources().getDrawable(R.drawable.emoji_happy), imgRev1, imgRev2, imgRev3, imgRev4);
            setFilter(imgRev5);
            uRate=8;

            ctaBtn.setText("Update Feedback");
            ctaBtn.setEnabled(true);
            ctaBtn.setBackground(context.getResources().getDrawable(R.drawable.round_corners_fill));
        });

        imgRev5.setOnClickListener(v -> {
            removeFilter(imgRev1, imgRev2, imgRev3, imgRev4, imgRev5);
            initIcons(context);
            setSameImg(context.getResources().getDrawable(R.drawable.emoji_in_love), imgRev1, imgRev2, imgRev3, imgRev4, imgRev5);

            ctaBtn.setText("Update Feedback");
            uRate=10;
            ctaBtn.setEnabled(true);
            ctaBtn.setBackground(context.getResources().getDrawable(R.drawable.round_corners_fill));
        });

        closeBtn.setOnClickListener(v -> {
            d.dismiss();
        });

        ctaBtn.setOnClickListener(v-> {

            java.util.Date dt = new java.util.Date();
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String currentTime = sdf.format(dt);
            String feedID = String.valueOf(feedback.getFeedback_ID());

            String msg = avatarMsg.getText().toString();
            FeedUpdate(feedID,feedback.getReserve_Id(),msg,uRate,currentTime);
            Toast.makeText(v.getContext(),"Feedback Updated !",Toast.LENGTH_LONG).show();
            d.dismiss();

        });
        btnDelete.setOnClickListener(v -> {
            final AlertDialog dialog = new AlertDialog.Builder(new ContextThemeWrapper(v.getContext(), R.style.AlertDialogCustom))
                    .setTitle("Alert Message")
                    .setMessage("Confirm delete ?")
                    .setPositiveButton("Yes", null)
                    .setNegativeButton("No", null)
                    .setIcon(v.getResources().getDrawable(R.drawable.ic_warning))
                    .show();
            Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            Button negativeButton = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
            positiveButton.setOnClickListener(x -> {

                dialog.dismiss();
                FeedbackDelete(feedback.getReserve_Id());
                Toast.makeText(v.getContext(),"Feedback Deleted !",Toast.LENGTH_LONG).show();
                d.dismiss();
            });

            negativeButton.setOnClickListener(x -> {
                dialog.dismiss();
            });

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


    public Edit_Feedback setData(Feedback feedback) {
       this.feedback = feedback;
        return this;
    }
    public void FeedbackDelete(final int Reserve_ID) {
        class FeedbackDeleteClass extends AsyncTask<String, Void, String> {

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

                finalResult = httpParse.postRequest(hashMap, AppConfig.URL_deleteFeedback);

                return finalResult;
            }
        }
        FeedbackDeleteClass UpdateClass = new  FeedbackDeleteClass();
        UpdateClass.execute(String.valueOf(Reserve_ID));
    }

    public void FeedUpdate( final String Feedback_ID,
                            final int Reserve_Id,
                            final String User_Feedback,
                            final int User_Rating,
                            final String Feedback_Date) {
        class FeedbackDeleteClass extends AsyncTask<String, Void, String> {

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
                hashMap.put("Feedback_ID", params[0]);
                hashMap.put("Reserve_Id", params[1]);
                hashMap.put("User_Feedback", params[2]);
                hashMap.put("User_Rating", params[3]);
                hashMap.put("Feedback_Date", params[4]);



                finalResult = httpParse.postRequest(hashMap, AppConfig.URL_UpFeedback);

                return finalResult;
            }
        }
        FeedbackDeleteClass UpdateClass = new  FeedbackDeleteClass();
        UpdateClass.execute(String.valueOf(Feedback_ID),String.valueOf(Reserve_Id),User_Feedback,
                 String.valueOf(User_Rating),Feedback_Date);
    }



}
