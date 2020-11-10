package com.example.ez_rental.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.HashMap;

public class SQLiteHelper  extends SQLiteOpenHelper {
    private static final String TAG = SQLiteHelper.class.getSimpleName();

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "my_db";
    private static final String TABLE_USER = "user";

    // Login Table Columns names
    private static final String KEY_ID = "User_ID";
    private static final String KEY_NAME = "Username";
    private static final String KEY_EMAIL = "User_Email";
    private static final String KEY_ContactNo = "User_ContactNo";
    private static final String KEY_Address = "Address";
    private static final String KEY_Password = "User_Password";
    private static final String KEY_Profile = "User_Profile";
    private static final String KEY_license = "Driver_license";
    private static final String KEY_Expiry = "Expiry_Date";

    public SQLiteHelper( Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }



    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_LOGIN_TABLE = "CREATE TABLE " + TABLE_USER + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT,"
                + KEY_EMAIL + " TEXT UNIQUE,"  + KEY_ContactNo + " TEXT,"
                + KEY_Address + " TEXT,"+ KEY_Password + " TEXT," + KEY_Profile + " TEXT,"+KEY_license + " TEXT," +KEY_Expiry +")";
        db.execSQL(CREATE_LOGIN_TABLE);
        Log.d(TAG, "Database tables created");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        onCreate(db);
    }

    public void addUser(String User_ID, String Username, String User_Email,
                        String User_ContactNo, String Address, String User_Password, String User_Profile,
                        String Driver_license, String Expiry_Date) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_ID, User_ID);
        values.put(KEY_NAME, Username);
        values.put(KEY_EMAIL, User_Email);
        values.put(KEY_ContactNo, User_ContactNo);
        values.put(KEY_Address, Address);
        values.put(KEY_Password, User_Password);
        values.put(KEY_Profile,User_Profile);
        values.put(KEY_license,Driver_license);
        values.put(KEY_Expiry,Expiry_Date);
        // Inserting Row
        long id = db.insert(TABLE_USER, null, values);
        db.close();

        Log.d(TAG, "New user inserted into sqlite: " + id);
    }
    public HashMap<String, String> getUserDetails() {
        HashMap<String, String> user = new HashMap<String, String>();
        String selectQuery = "SELECT  * FROM " + TABLE_USER;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                user.put("User_ID", cursor.getString(0));
                user.put("Username", cursor.getString(1));
                user.put("User_Email", cursor.getString(2));

                user.put("User_ContactNo", cursor.getString(3));
                user.put("Address", cursor.getString(4));
                user.put("User_Password", cursor.getString(5));
                user.put("User_Profile", cursor.getString(6));
                user.put("Driver_license", cursor.getString(7));
                user.put("Expiry_Date", cursor.getString(8));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        // return user
        Log.d(TAG, "Fetching user from Sqlite: " + user.toString());

        return user;
    }
    public void deleteUsers() {
        SQLiteDatabase db = this.getWritableDatabase();
        // Delete All Rows
        db.delete(TABLE_USER, null, null);
        db.close();

        Log.d(TAG, "Deleted all user info from sqlite");
    }
}
