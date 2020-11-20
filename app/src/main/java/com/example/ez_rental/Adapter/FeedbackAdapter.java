package com.example.ez_rental.Adapter;

import android.content.Context;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ez_rental.Model.Feedback;
import com.example.ez_rental.R;
import com.example.ez_rental.activity.helper.HttpParse;
import com.example.ez_rental.app.AppConfig;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;


public class FeedbackAdapter extends RecyclerView.Adapter<FeedbackAdapter.FeedbackViewHolder> {


    private FeedbackAdapter.onFeedbackListener onFeedbackListener;
    private Context mCtx;
    private ArrayList<Feedback> FeedbackList;

    String finalResult;
    HashMap<String, String> hashMap = new HashMap<>();
    HttpParse httpParse = new HttpParse();


    class FeedbackViewHolder extends RecyclerView.ViewHolder implements  View.OnClickListener{

        TextView feedbackID, rateCar, FeedbackTxtx,user_Id,detail;
        Button book,btndelete;
        ImageView feebbackImg;
        ConstraintLayout card;

        FeedbackAdapter.onFeedbackListener onFeedbackListener;
        public FeedbackViewHolder(@NonNull View itemView, onFeedbackListener onFeedbackListener) {
            super(itemView);
            feedbackID = itemView.findViewById(R.id.feedbackID);
            rateCar = itemView.findViewById(R.id.rateCar);
            detail = itemView.findViewById(R.id.detail);
            card = itemView.findViewById(R.id.card);
            FeedbackTxtx = itemView.findViewById(R.id.FeedbackTxtx);
            user_Id = itemView.findViewById(R.id.user_Id);
            book = itemView.findViewById(R.id.book);
            feebbackImg = itemView.findViewById(R.id.feebbackImg);
            this. onFeedbackListener =  onFeedbackListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onFeedbackListener.onClick(getAdapterPosition());
        }
    }

    @NonNull
    @Override
    public FeedbackViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.feedbackcard, null);
        return new FeedbackViewHolder(view,onFeedbackListener);
    }


    public FeedbackAdapter(ArrayList<Feedback> FeedbackList, Context mCtx, onFeedbackListener onFeedbackListener) {
        this.onFeedbackListener = onFeedbackListener;
        this.mCtx = mCtx;
        this.FeedbackList = FeedbackList;
        notifyDataSetChanged();
    }

    public void sortDateByAsc() {
        Comparator<Feedback> comparator = new Comparator<Feedback>() {

            @Override
            public int compare(Feedback object1, Feedback object2) {
                return object1.getFeedback_Date().compareToIgnoreCase(object2.getFeedback_Date());
            }
        };
        Collections.sort(FeedbackList, comparator);
        notifyDataSetChanged();

    }

    @Override
    public void onBindViewHolder(FeedbackViewHolder holder, int position) {

        Feedback feedback =  FeedbackList.get(position);
        holder.feedbackID.setText("Feedback ID:"+feedback.getFeedback_ID());
        holder.user_Id.setText("User ID:"+feedback.getUser_ID() + "(Car ID: "+feedback.getCar_Id() +")");
        if(feedback.getUser_Feedback().compareTo("")== 0){
            holder.FeedbackTxtx.setText("No comment");
        }else
        {
            holder.FeedbackTxtx.setText(feedback.getUser_Feedback());
        }
        holder.book.setText("Reserve ID:"+feedback.getReserve_Id());
        holder.rateCar.setText(" "+feedback.getUser_Rating()+".0");

        holder.detail.setText(feedback.getFeedback_Date());

        switch(feedback.getUser_Rating()){
            case 1:
            case 2:
                holder.feebbackImg.setImageResource(R.drawable.emoji_crying) ;break;
            case 3:
            case 4:
                holder.feebbackImg.setImageResource(R.drawable.emoji_angry) ;break;
            case 5:
            case 6:
                holder.feebbackImg.setImageResource(R.drawable.emoji_thinking) ;break;
            case 7:
            case 8:
                holder.feebbackImg.setImageResource(R.drawable.emoji_happy) ;break;
            case 9:
            case 10:
                holder.feebbackImg.setImageResource(R.drawable.emoji_in_love) ;break;
            default:
                holder.feebbackImg.setImageResource(R.drawable.ic_star) ;break;

        }



    }

    @Override
    public int getItemCount() {
        return FeedbackList.size();
    }

    public interface onFeedbackListener{
        void onClick(int position);
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
}