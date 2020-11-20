package com.example.ez_rental.app;

public class AppConfig {

	private static String ipAddress="http://192.168.1.13/FYP/";
	public static double rate =0.0;
	public static String URL_UpReserve     = ipAddress+"UpdateReservation.php";
	public static String URL_UpReserveCash = ipAddress+"UpdateReservationCash.php";
	public static String URL_GetFeedback   = ipAddress+"getFeedback.php";
	public static String URL_makeFeedbck   = ipAddress+"makeFeedbck.php";
	public static String URL_deleteFeedback= ipAddress+"deleteFeedback.php";
	public static String URL_UpFeedback    = ipAddress+"UpdateFeedback.php";
	public static String URL_UpdateReserve = ipAddress+"UpdateReserve.php";
	public static String URL_LOGIN         = ipAddress+"login.php";
	public static String HttpURL_CarStatus = ipAddress+"UpdateCarStatus.php";
	public static String HttpUpdateRating  = ipAddress+"UpdateRating.php";
	public static String HttpURL_UpUser    = ipAddress+"update.php";
	public static String HttpURL_getUser   = ipAddress+"getUser.php";
	public static String HttpURL_UpPass    = ipAddress+"UpdatePassword.php";
	public static String URL_REGISTER      = ipAddress+"register.php";
	public static String URL_adLogin       = ipAddress+"AdminLogin.php";
	public static String URL_Cars          = ipAddress+"getCar.php";
	public static String URL_feedback      = ipAddress+"getCarRate.php";
	public static String URL_Reservation   = ipAddress+"getReservation.php";
	public static String URL_payment       = ipAddress+"getPaymentHistory.php";
	public static String URL_updateCd      = ipAddress+"UpCD.php";
	public static String URL_CD            = ipAddress+"getCD.php";
	public static String URL_makeReserve   = ipAddress+"makeReservation.php";
	public static String URL_makePayment   = ipAddress+"makePayment.php";
	public static String Url_UpdateUser    = ipAddress+"UpdateUser.php";
	public static String Url_addBrand      = ipAddress+"addBrand.php";
	public static String Url_editBrand     = ipAddress+"EditBrand.php";
	public static String Url_viewBrand     = ipAddress+"getBrand.php";
	public static String Url_addCar        = ipAddress+"addCar.php";
	public static String Url_updateCar     = ipAddress+"UpdateCar.php";
	public static String Url_addCd         = ipAddress+"addCD.php";



	private static String ipAddress2="http://192.168.1.13/Image/";
	public static String car_Image       = ipAddress2+"carImageUpload.php";
	public static String profile_Image   = ipAddress2+"upload-image-to-server.php";

	public static final String PAYPAL_CLIENT_ID = "AWq_91F-HxwEnCi_tRLl39YxQwpXCWnW7ILOkqTrg0IqPZG93n7tCn2eXXxNAViME_y_wNwbW5_BW3cC";
}
