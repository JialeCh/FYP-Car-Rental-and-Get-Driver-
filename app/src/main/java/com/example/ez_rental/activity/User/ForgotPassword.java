package com.example.ez_rental.activity.User;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.InputType;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.example.ez_rental.R;
import com.example.ez_rental.app.AppConfig;
import com.example.ez_rental.app.AppController;
import com.example.ez_rental.activity.helper.HttpParse;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static android.content.ContentValues.TAG;

public class ForgotPassword extends AppCompatActivity implements View.OnClickListener{

    private static final int MAX_LENGTH =6;
    private static final String ALLOWED_CHARACTERS ="0123456789qwertyuiopasdfghjklzxcvbnm";
    private AppCompatButton btn_reset, btnVerify, btnSave;
    private EditText et_email,et_code,et_password;
    private TextView tv_timer;
    private ProgressBar progress;
    private String code;
    private TextView btnClose;
    private boolean isResetInitiated = false;
    private String email;
    private CountDownTimer countDownTimer;
    private int count =0;


    String finalResult;
    HashMap<String, String> hashMap = new HashMap<>();
    HttpParse httpParse = new HttpParse();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_password_reset);
        initViews();
    }
    @SuppressLint("ClickableViewAccessibility")
    private void initViews(){
        code = random();
        btn_reset = findViewById(R.id.btn_reset);
        tv_timer = findViewById(R.id.timer);
        et_code = findViewById(R.id.et_code);
        et_email = findViewById(R.id.et_email);
        et_password = findViewById(R.id.et_password);
        et_password.setVisibility(View.GONE);
        et_code.setVisibility(View.GONE);
        tv_timer.setVisibility(View.GONE);
        btn_reset.setOnClickListener(this);
        progress = findViewById(R.id.progress);
        btnVerify = findViewById(R.id.btnCheck);
        btnVerify.setOnClickListener(this);
        btnClose = findViewById(R.id.imageView16);
        btnSave= findViewById(R.id.btnSavepass);
        btnSave.setOnClickListener(this);
        btnClose.setOnClickListener(v3-> finish());
        et_password.setOnTouchListener((v2, event) -> {
            final int DRAWABLE_RIGHT = 2;
            if(event.getAction() == MotionEvent.ACTION_UP) {
                if(event.getRawX() >= ( et_password.getRight() -  et_password.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                    if (count == 0){
                        et_password.setInputType(InputType.TYPE_CLASS_TEXT);
                        et_password.setCompoundDrawablesWithIntrinsicBounds(null, null, ContextCompat.getDrawable(getApplicationContext(),R.drawable.ic_offeye), null);
                        count = 1;
                    }else{
                        count = 0;
                        et_password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                        et_password.setCompoundDrawablesWithIntrinsicBounds(null, null, ContextCompat.getDrawable(getApplicationContext(),R.drawable.ic_eye), null);
                    }
                    return true;
                }
            }
            return false;
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.btn_reset:
            {
                if(!isResetInitiated) {

                    email = et_email.getText().toString();
                    if (!email.isEmpty()) {
                        progress.setVisibility(View.VISIBLE);
                        initiateResetPasswordProcess(email);

                    } else {
                        Toast.makeText(getApplicationContext(), "Fields are empty !", Toast.LENGTH_LONG).show();

                    }
                }

                break;
            }
            case R.id.btnCheck:
            {

                String codeChk = et_code.getText().toString();
                if (codeChk.contains(code)) {
                    btnVerify.setVisibility(View.GONE);
                    btnSave.setVisibility(View.VISIBLE);
                    et_password.setVisibility(View.VISIBLE);
                    Boolean check = false;
                    startCountdownTimer(check);
                }
                else {
                    Toast.makeText(getApplicationContext(), "Invalid Code !", Toast.LENGTH_LONG).show();

                }

                break;
            }

            case R.id.btnSavepass:
            {
               String password = et_password.getText().toString();
               PasswordUpdate(email, password);
                final AlertDialog dialog = new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.AlertDialogCustom))
                        .setTitle("Password Updated")
                        .setMessage("Confirmed your new password ?")
                        .setIcon(getResources().getDrawable(R.drawable.ic_warning2))
                        .setPositiveButton("Ok", null)
                        .setNegativeButton("Cancel", null)
                        .show();
                Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                Button negativeButton = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
                positiveButton.setOnClickListener(x -> {
                    dialog.dismiss();
                    Intent homepage = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(homepage);
                    finish();
                    Toast.makeText(getApplicationContext(), "Password Updated", Toast.LENGTH_LONG).show();
                    Boolean check = false;
                    startCountdownTimer(check);
                });
                negativeButton.setOnClickListener(x -> {
                    dialog.dismiss();
                });
                break;
            }
        }
    }


    private void startCountdownTimer(Boolean check){
        if (check == false){

            countDownTimer.cancel();
        }
        else{
            countDownTimer = new CountDownTimer(200000, 1000) {

                public void onTick(long millisUntilFinished) {
                    tv_timer.setText("Time remaining : " + millisUntilFinished / 1000);
                }

                public void onFinish() {
                    Toast.makeText(getApplicationContext(), "Time Out ! Request again to reset password.", Toast.LENGTH_LONG).show();
                    goToLogin();
                }
            }.start();
        }


    }
    private void initiateResetPasswordProcess(final String email) {
        String tag_string_req = "req_login";
        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.Url_CHEK, response -> {
            Log.d(TAG, "Login Response: " + response);
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

                    progress.setVisibility(View.INVISIBLE);
                    et_code.setVisibility(View.VISIBLE);
                    btn_reset.setVisibility(View.GONE);
                    btnVerify.setVisibility(View.VISIBLE);
                    sendEmail(email,code);
                    Boolean check = true;
                    startCountdownTimer(check);
                    tv_timer.setVisibility(View.VISIBLE);
                } else {
                    String errorMsg = jObj.getString("error_msg");
                    Toast.makeText(getApplicationContext(),
                            errorMsg, Toast.LENGTH_LONG).show();
                    progress.setVisibility(View.INVISIBLE);
                }
            } catch (JSONException e) {
                // JSON error
                e.printStackTrace();

                Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }

        }, error -> {
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("email", email);
                return params;
            }

        };
      AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void goToLogin(){
        Intent intent= getIntent();
        finish();
        startActivity(intent);
    }

    private void sendEmail(String User_Email, String code) {

        SendMail sm = new SendMail(this, User_Email, code);

        sm.execute();
    }

    public static String random() {
        final Random random=new Random();
        final StringBuilder sb=new StringBuilder(MAX_LENGTH);
        for(int i=0;i<MAX_LENGTH;++i)
            sb.append(ALLOWED_CHARACTERS.charAt(random.nextInt(ALLOWED_CHARACTERS.length())));
        return sb.toString();
    }


    public void PasswordUpdate(final String User_Email, final String newPass) {
        class PasswordUpdateClass extends AsyncTask<String, Void, String> {

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
                hashMap.put("User_Email", params[0]);
                hashMap.put("User_Password", params[1]);

                finalResult = httpParse.postRequest(hashMap, AppConfig.HttpURL_UpPass);

                return finalResult;
            }
        }
        PasswordUpdateClass UpdateClass = new  PasswordUpdateClass();
        UpdateClass.execute(User_Email,newPass);
    }
}