package com.example.ez_rental.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.example.ez_rental.R;
import com.example.ez_rental.helper.SQLiteHelper;
import com.example.ez_rental.helper.SessionManager;


public class MainActivity extends Activity {

	private TextView txtName;
	private TextView txtEmail;
	private Button btnLogout;

	private SQLiteHelper db;
	private SessionManager session;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.profile_main);

		/*txtName = (TextView) findViewById(R.id.name);
		txtEmail = (TextView) findViewById(R.id.adminID);
		btnLogout = (Button) findViewById(R.id.logout);

		// SqLite database handler
		db = new SQLiteHandler(getApplicationContext());

		// session manager
		session = new SessionManager(getApplicationContext());

		if (!session.isLoggedIn()) {
			db.deleteUsers();
			logoutUser();
		}

		// Fetching user details from SQLite
		HashMap<String, String> user = db.getUserDetails();


		String name = user.get("Username");
		String email = user.get("User_Email");

		// Displaying the user details on the screen
		txtName.setText(name);
		txtEmail.setText(email);

		// Logout button click event
		btnLogout.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				logoutUser();
			}
		});
	}

	*//**
		 * Logging out the user. Will set isLoggedIn flag to false in shared
		 * preferences Clears the user data from sqlite users table
		 * *//*
	private void logoutUser() {
		session.setLogin(false);

		db.deleteUsers();

		// Launching the login activity
		Intent intent = new Intent(MainActivity.this, LoginActivity.class);
		startActivity(intent);
		finish();
	}*/
	}
}
