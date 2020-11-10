package com.example.ez_rental.helper;


import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

public class SessionManager {
	// LogCat tag
	private static String TAG = SessionManager.class.getSimpleName();

	// Shared Preferences
	SharedPreferences pref;

	Editor editor;
	Context _context;

	// Shared pref mode
	int PRIVATE_MODE = 0;

	// Shared preferences file name
	private static final String PREF_NAME = "Login";
	private static final String LOGIN = "IS_LOGIN";
	public static final String User_Name = "User_Name";
	public static final String User_Email = "User_Email";
	public static final String User_ID = "User_ID";
	public static final String User_Profile = "User_Profile";
	public static final String User_Contact = "User_Contact";
	public static final String Address = "Address";
	public static final String Driver_license = "Driver_license";
	private static final String KEY_IS_LOGGED_IN = "isLoggedIn";

	public SessionManager(Context context) {
		this._context = context;
		pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
		editor = pref.edit();
	}

	public void setLogin(boolean isLoggedIn) {

		editor.putBoolean(KEY_IS_LOGGED_IN, isLoggedIn);

		// commit changes
		editor.commit();

		Log.d(TAG, "User login session modified!");
	}
	public void createSession(String name, String email, String id, String contact, String profile, String address, String license){

		editor.putBoolean(LOGIN, true);
		editor.putString(User_Name, name);
		editor.putString(User_Email, email);
		editor.putString(User_ID, id);
		editor.putString(User_Contact, contact);
		editor.putString(User_Profile, profile);
		editor.putString(Address, address);
		editor.putString(Driver_license, license);
		editor.apply();

	}
	public boolean isLoggedIn(){
		return pref.getBoolean(KEY_IS_LOGGED_IN, false);
	}
}

