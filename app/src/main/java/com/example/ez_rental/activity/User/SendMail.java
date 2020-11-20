package com.example.ez_rental.activity.User;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.example.ez_rental.activity.helper.Utils;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class SendMail extends AsyncTask<Void, Void, Void> {

    //Declaring Variables
    private Context context;
    private Session session;

    //Information to send email
    private String email;
    private String message;

    //Progressdialog to show while sending email
    private ProgressDialog progressDialog;

    //Class Constructor
    public SendMail(Context context, String email, String code){
        //Initializing variables
        this.context = context;
        this.email = email;
        this.message =code;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        //Showing progress dialog while sending email
        progressDialog = ProgressDialog.show(context,"Sending message","Please wait...",false,false);
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        //Dismissing the progress dialog
        progressDialog.dismiss();
        //Showing a success message
        Toast.makeText(context,"Message Sent", Toast.LENGTH_LONG).show();
    }

    @Override
    protected Void doInBackground(Void... params) {
        //Creating properties
        Properties props = new Properties();

        //Configuring properties for gmail
        //If you are not using gmail you may need to change the values
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");

        //Creating a new session
        session = Session.getDefaultInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(Utils.EMAIL, Utils.PASSWORD);
                    }
                });

        try {
            if(message.contains("Deactivate")){
                MimeMessage mm = new MimeMessage(session);
                mm.setFrom(new InternetAddress(Utils.EMAIL));
                mm.addRecipient(Message.RecipientType.TO, new InternetAddress(email));
                mm.setSubject("Deactivate Confirmation");

                mm.setText("Hi, Your account has been deactivated.\n" +
                        "If you need reactive your account, you need to proceed the forgot password to reset and reactivate your account.\n \n" +
                        "Thank You !!!");
                Transport.send(mm);

            }else
            {
                MimeMessage mm = new MimeMessage(session);
                mm.setFrom(new InternetAddress(Utils.EMAIL));
                mm.addRecipient(Message.RecipientType.TO, new InternetAddress(email));
                mm.setSubject("Password Reset Request");

                mm.setText("Hi, Your password reset code is ("+message +"). \n" +
                        "This code will expire in 120 seconds. \nEnter this code within 120 seconds to reset your password. \n" +
                        "Thank You !!!");
                Transport.send(mm);

            }


        } catch (MessagingException e) {
            e.printStackTrace();
        }
        return null;
    }
}