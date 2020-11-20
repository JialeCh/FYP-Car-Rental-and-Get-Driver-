package com.example.ez_rental.activity.car;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.net.ssl.HttpsURLConnection;

public class CarImageUploadActivity extends AppCompatActivity {


    String profileID;
    Bitmap FixBitmap;
    String imageLocation;
    byte[] byteArray;
    String ConvertImage;
    HttpURLConnection httpURLConnection ;
    URL url;
    OutputStream outputStream;
    BufferedWriter bufferedWriter ;
    int RC ;
    BufferedReader bufferedReader ;
    StringBuilder stringBuilder;
    boolean check = true;

    String cid;
    private String Url;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);


        byteArray = getIntent().getByteArrayExtra("Image1");
        int i = getIntent().getIntExtra("ImageLocation", 0);

        if (i == 0){
            imageLocation = "VImage1";
        }else if( i== 1){
            imageLocation = "VImage2";
        }
        else{
            imageLocation = "VImage3";
        }
        cid=getIntent().getStringExtra("Car_Id");
        FixBitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
        ConvertImage = Base64.encodeToString(byteArray, Base64.DEFAULT);


        Url ="http://192.168.1.3/xx/carImageUpload.php";

        UploadImageToServer();
        finish();
    }

  public void setData( byte[] image1){
  byteArray =image1;
  }
    public void UploadImageToServer(){

        class AsyncTaskUploadClass extends AsyncTask<Void, Void, String> {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }
            @Override
            protected void onPostExecute(String string1) {
                super.onPostExecute(string1);
                finish();
            }

            @Override
            protected String doInBackground(Void... params) {
                 profileID = generateID();
                ImageProcessClass imageProcessClass = new ImageProcessClass();
                HashMap<String, String> HashMapParams = new HashMap<String, String>();
                HashMapParams.put("image_tag", profileID + cid);
                HashMapParams.put("Car_Id",  cid);
                HashMapParams.put("image_data", ConvertImage);
                HashMapParams.put("imageLocation", imageLocation);
                String FinalData = imageProcessClass.ImageHttpRequest(Url, HashMapParams);
                return FinalData;
            }
        }
        AsyncTaskUploadClass AsyncTaskUploadClassOBJ = new AsyncTaskUploadClass();
        AsyncTaskUploadClassOBJ.execute();
    }

    public class ImageProcessClass{

        public String ImageHttpRequest(String requestURL, HashMap<String, String> PData) {
            StringBuilder stringBuilder = new StringBuilder();
            try {
                url = new URL(requestURL);
                httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setReadTimeout(20000);
                httpURLConnection.setConnectTimeout(20000);
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoInput(true);
                httpURLConnection.setDoOutput(true);
                outputStream = httpURLConnection.getOutputStream();
                bufferedWriter = new BufferedWriter(
                        new OutputStreamWriter(outputStream, "UTF-8"));
                bufferedWriter.write(bufferedWriterDataFN(PData));
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                RC = httpURLConnection.getResponseCode();
                if (RC == HttpsURLConnection.HTTP_OK) {
                    bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                    stringBuilder = new StringBuilder();
                    String RC2;
                    while ((RC2 = bufferedReader.readLine()) != null){
                        stringBuilder.append(RC2);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return stringBuilder.toString();
        }

        private String bufferedWriterDataFN(HashMap<String, String> HashMapParams) throws UnsupportedEncodingException {
            stringBuilder = new StringBuilder();
            for (Map.Entry<String, String> KEY : HashMapParams.entrySet()) {
                if (check)
                    check = false;
                else
                    stringBuilder.append("&");
                stringBuilder.append(URLEncoder.encode(KEY.getKey(), "UTF-8"));
                stringBuilder.append("=");
                stringBuilder.append(URLEncoder.encode(KEY.getValue(), "UTF-8"));
            }
            return stringBuilder.toString();
        }
    }
    private String generateID(){
        Random rnd = new Random();
        int id = 202000 + rnd.nextInt(65)*3;
        String userid = String.valueOf(id);
        return userid;
    }

}
