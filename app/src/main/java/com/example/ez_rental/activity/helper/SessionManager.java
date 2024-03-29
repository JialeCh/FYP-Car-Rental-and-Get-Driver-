package com.example.ez_rental.activity.helper;


import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

public class SessionManager {
	// LogCat tag
	private static String TAG = SessionManager.class.getSimpleName();

	// Shared Preferences
	SharedPreferences pref,pref2;

	Editor editor;
	Context _context;

	// Shared pref mode
	int PRIVATE_MODE = 0;

	// Shared preferences file name
	private static final String PREF_NAME = "Login";

	public static final String User_Email = "User_Email";
	public static final String User_ID = "User_ID";

	private static final String KEY_IS_LOGGED_IN_User= "isLoggedIn";

	public SessionManager(Context context) {
		this._context = context;
		pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);

		editor = pref.edit();

	}

	public void setLogin(boolean isLoggedIn) {

		editor.putBoolean(KEY_IS_LOGGED_IN_User, isLoggedIn);

		// commit changes
		editor.commit();

		Log.d(TAG, "User login session modified!");
	}



	public boolean isLoggedIn(){
		return pref.getBoolean(KEY_IS_LOGGED_IN_User, false);
	}




}

