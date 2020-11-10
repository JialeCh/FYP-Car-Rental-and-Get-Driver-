package com.example.ez_rental.app;

public class AppConfig {
	// Server user login url
	public static String URL_LOGIN = "http://192.168.1.3/android_login_api/login.php";
	public static double rate =0.0;

	// Server user register url
	public static String URL_REGISTER = "http://192.168.1.3/android_login_api/register.php";
	// Server admin login url
	public static String URL_adLogin = "http://192.168.1.3/android_login_api/AdminLogin.php";

	public static String URL_Cars ="http://192.168.1.3/android_login_api/getCar.php";
	public static String URL_feedback ="http://192.168.1.3/android_login_api/getCarRate.php";
	public static String URL_Reservation ="http://192.168.1.3/android_login_api/getReservation.php";
	public static String URL_EDIT ="http://192.168.1.3/android_login_api/update.php";
	public static String URL_payment ="http://192.168.1.3/android_login_api/getPaymentHistory.php";
	public static String URL_CD ="http://192.168.1.3/android_login_api/getCD.php";
	public static String URL_makeReserve ="http://192.168.1.3/android_login_api/makeReservation.php";
	public static String URL_makePayment ="http://192.168.1.3/android_login_api/makePayment.php";
	public static final String PAYPAL_CLIENT_ID = "AWq_91F-HxwEnCi_tRLl39YxQwpXCWnW7ILOkqTrg0IqPZG93n7tCn2eXXxNAViME_y_wNwbW5_BW3cC";
}
