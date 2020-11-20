package com.example.ez_rental.activity.Reservation;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.IntentSender;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ContextThemeWrapper;

import com.example.ez_rental.R;
import com.example.ez_rental.activity.User.UserViewActivity;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.AutocompletePrediction;
import com.google.android.libraries.places.api.model.AutocompleteSessionToken;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.FetchPlaceResponse;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsResponse;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.mancj.materialsearchbar.MaterialSearchBar;
import com.mancj.materialsearchbar.adapter.SuggestionsAdapter;
import com.skyfishjy.library.RippleBackground;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private PlacesClient placesClient;
    private List<AutocompletePrediction> predictionList;
    private String result;
    private Location mLastKnownLocation;
    private LocationCallback locationCallback;

    private MaterialSearchBar materialSearchBar;
    private View mapView;
    private Button btnFind;
    private RippleBackground rippleBg;
    private String location;
    private final float DEFAULT_ZOOM = 15;
    private Button btnBck;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        materialSearchBar = findViewById(R.id.searchBar);
        btnBck= findViewById(R.id.btn_bck);
        btnFind = findViewById(R.id.btn_find);
        rippleBg = findViewById(R.id.ripple_bg);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        mapView = mapFragment.getView();

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(MapActivity.this);
        Places.initialize(MapActivity.this, getString(R.string.google_maps_api));
        placesClient = Places.createClient(this);
        final AutocompleteSessionToken token = AutocompleteSessionToken.newInstance();

        materialSearchBar.setOnSearchActionListener(new MaterialSearchBar.OnSearchActionListener() {

            @Override
            public void onSearchStateChanged(boolean enabled) {

            }

            @Override
            public void onSearchConfirmed(CharSequence text) {
                startSearch(text.toString(), true, null, true);
            }

            @Override
            public void onButtonClicked(int buttonCode) {
                if (buttonCode == MaterialSearchBar.BUTTON_NAVIGATION) {
                    //opening or closing a navigation drawer
                } else if (buttonCode == MaterialSearchBar.BUTTON_BACK) {

                    materialSearchBar.disableSearch();

                }
            }
        });

        materialSearchBar.addTextChangeListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                FindAutocompletePredictionsRequest predictionsRequest = FindAutocompletePredictionsRequest.builder()
                        .setSessionToken(token)
                        .setQuery(s.toString())
                        .build();
                placesClient.findAutocompletePredictions(predictionsRequest).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FindAutocompletePredictionsResponse predictionsResponse = task.getResult();
                        if (predictionsResponse != null) {
                            predictionList = predictionsResponse.getAutocompletePredictions();
                            List<String> suggestionsList = new ArrayList<>();
                            for (int i = 0; i < predictionList.size(); i++) {
                                AutocompletePrediction prediction = predictionList.get(i);
                                suggestionsList.add(prediction.getFullText(null).toString());
                            }
                            materialSearchBar.updateLastSuggestions(suggestionsList);
                            if (!materialSearchBar.isSuggestionsVisible()) {
                                materialSearchBar.showSuggestionsList();
                            }
                        }
                    } else {
                        Log.i("mytag", "prediction fetching task unsuccessful");
                    }
                });
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        btnBck.setOnClickListener(v -> {

            Intent intent = new Intent(MapActivity.this, UserViewActivity.class);
            startActivity(intent);
            finish();

        });
        materialSearchBar.setSuggstionsClickListener(new SuggestionsAdapter.OnItemViewClickListener() {
            @Override
            public void OnItemClickListener(int position, View v) {
                if (position >= predictionList.size()) {
                    return;
                }
                AutocompletePrediction selectedPrediction = predictionList.get(position);
                String suggestion = materialSearchBar.getLastSuggestions().get(position).toString();
                materialSearchBar.setText(suggestion);
                Log.i("mytag", "Place found: " +suggestion);
                location = suggestion;
                new Handler().postDelayed(() -> materialSearchBar.clearSuggestions(), 1000);
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                if (imm != null)
                    imm.hideSoftInputFromWindow(materialSearchBar.getWindowToken(), InputMethodManager.HIDE_IMPLICIT_ONLY);
                final String placeId = selectedPrediction.getPlaceId();
                List<Place.Field> placeFields = Arrays.asList(Place.Field.LAT_LNG);

                FetchPlaceRequest fetchPlaceRequest = FetchPlaceRequest.builder(placeId, placeFields).build();
                placesClient.fetchPlace(fetchPlaceRequest).addOnSuccessListener(new OnSuccessListener<FetchPlaceResponse>() {
                    @Override
                    public void onSuccess(FetchPlaceResponse fetchPlaceResponse) {
                        Place place = fetchPlaceResponse.getPlace();


                        LatLng latLngOfPlace = place.getLatLng();

                        if (latLngOfPlace != null) {
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLngOfPlace, DEFAULT_ZOOM));
                        }
                    }
                }).addOnFailureListener(e -> {
                    if (e instanceof ApiException) {
                        ApiException apiException = (ApiException) e;
                        apiException.printStackTrace();
                        int statusCode = apiException.getStatusCode();
                        Log.i("mytag", "place not found: " + e.getMessage());
                        Log.i("mytag", "status code: " + statusCode);
                    }
                });
            }

            @Override
            public void OnItemDeleteListener(int position, View v) {

            }
        });
        btnFind.setOnClickListener(v -> {
            LatLng currentMarkerLocation = mMap.getCameraPosition().target;
            rippleBg.startRippleAnimation();
            new Handler().postDelayed(() -> {
                rippleBg.stopRippleAnimation();
                if(materialSearchBar.getText().isEmpty()){
                    location = result;

                    final AlertDialog dialog = new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.AlertDialogCustom))
                            .setTitle("Rent Place ")
                            .setMessage("Confirmed use current location (" + result + ") as your rent place ?")
                            .setPositiveButton("Ok", null)
                            .setNegativeButton("Cancel", null)
                            .setIcon(getResources().getDrawable(R.drawable.ic_locate))
                            .show();
                    Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                    Button negativeButton = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
                    positiveButton.setOnClickListener(x -> {

                        dialog.dismiss();

                        final AlertDialog dialog2 = new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.AlertDialogCustom))
                                .setTitle("Return place")
                                .setMessage("Use location (" + result + ") as your return place ?")
                                .setPositiveButton("Ok", null)
                                .setNegativeButton("Cancel", null)
                                .setIcon(getResources().getDrawable(R.drawable.ic_locate))
                                .show();
                        Button positiveButton2 = dialog2.getButton(AlertDialog.BUTTON_POSITIVE);
                        Button negativeButton2 = dialog2.getButton(AlertDialog.BUTTON_NEGATIVE);
                        positiveButton2.setOnClickListener(x2 -> {
                            Intent homepage = new Intent(getApplicationContext(), ViewCarActivity.class);
                            homepage.putExtra("location", location);
                            homepage.putExtra("location2", location);
                            startActivity(homepage);
                            finish();
                            dialog2.dismiss();
                        });
                        negativeButton2.setOnClickListener(x2 -> {
                            Intent homepage = new Intent(getApplicationContext(), ReturnPlaceActivity.class);
                            homepage.putExtra("Rent Place", location);
                            startActivity(homepage);
                            Toast.makeText(getApplicationContext(), "Close", Toast.LENGTH_SHORT).show();
                            dialog2.dismiss();
                            finish();
                        });


                    });
                    negativeButton.setOnClickListener(x -> {
                        Toast.makeText(getApplicationContext(), "Close", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    });

                }
                else{
                    final AlertDialog dialog = new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.AlertDialogCustom))
                            .setTitle("Rent Place")
                            .setMessage("Confirmed " + location + " as your rent place ?")
                            .setPositiveButton("Yes", null)
                            .setNegativeButton("Cancel", null)
                            .setIcon(getResources().getDrawable(R.drawable.ic_locate))
                            .show();
                    Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                    Button negativeButton = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
                    positiveButton.setOnClickListener(x -> {
                        dialog.dismiss();
                        final AlertDialog dialog2 = new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.AlertDialogCustom))
                                .setTitle("Return Place")
                                .setMessage("Confirmed " + location + " as your return place ?")
                                .setPositiveButton("Yes", null)
                                .setNegativeButton("Cancel", null)
                                .setIcon(getResources().getDrawable(R.drawable.ic_locate))
                                .show();
                        Button positiveButton2 = dialog2.getButton(AlertDialog.BUTTON_POSITIVE);
                        Button negativeButton2 = dialog2.getButton(AlertDialog.BUTTON_NEGATIVE);
                        positiveButton2.setOnClickListener(x2 -> {
                            Intent homepage = new Intent(getApplicationContext(), ViewCarActivity.class);
                            homepage.putExtra("location",location);
                            homepage.putExtra("location2",location);
                            startActivity(homepage);
                            finish();
                                    dialog2.dismiss();
                                });
                        negativeButton2.setOnClickListener(x2 -> {
                            Intent homepage = new Intent(getApplicationContext(), ReturnPlaceActivity.class);
                            homepage.putExtra("Rent Place", location);
                            startActivity(homepage);
                            finish();
                            Toast.makeText(getApplicationContext(), "Close", Toast.LENGTH_SHORT).show();
                            dialog2.dismiss();
                        });

                    });
                    negativeButton.setOnClickListener(x -> {
                        Toast.makeText(getApplicationContext(), "Close", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    });
                }

            }, 2000);

        });
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);

        if (mapView != null && mapView.findViewById(Integer.parseInt("1")) != null) {
            View locationButton = ((View) mapView.findViewById(Integer.parseInt("1")).getParent()).findViewById(Integer.parseInt("2"));
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) locationButton.getLayoutParams();
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
            layoutParams.setMargins(0, 0, 40, 180);
        }

        //check if gps is enabled or not and then request user to enable it
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);

        SettingsClient settingsClient = LocationServices.getSettingsClient(MapActivity.this);
        Task<LocationSettingsResponse> task = settingsClient.checkLocationSettings(builder.build());

        task.addOnSuccessListener(MapActivity.this, locationSettingsResponse -> getDeviceLocation());

        task.addOnFailureListener(MapActivity.this, e -> {
            if (e instanceof ResolvableApiException) {
                ResolvableApiException resolvable = (ResolvableApiException) e;
                try {
                    resolvable.startResolutionForResult(MapActivity.this, 51);
                } catch (IntentSender.SendIntentException e1) {
                    e1.printStackTrace();
                }
            }
        });

        mMap.setOnMyLocationButtonClickListener(() -> {
            if (materialSearchBar.isSuggestionsVisible())
                materialSearchBar.clearSuggestions();
            if (materialSearchBar.isSearchEnabled())
                materialSearchBar.disableSearch();
            return false;
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 51) {
            if (resultCode == RESULT_OK) {
                getDeviceLocation();
            }
        }
    }

    @SuppressLint("MissingPermission")
    private void getDeviceLocation() {
        mFusedLocationProviderClient.getLastLocation()
                .addOnCompleteListener(new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful()) {
                            mLastKnownLocation = task.getResult();
                            Location location = mLastKnownLocation;
                            double latitude = location.getLatitude();
                            double longitude = location.getLongitude();
                            Geocoder geocoder = new Geocoder(getApplicationContext());
                            try {
                                List<Address> addresses =
                                        geocoder.getFromLocation(latitude, longitude, 1);
                                result = addresses.get(0).getLocality();


                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            if (mLastKnownLocation != null) {
                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude()), DEFAULT_ZOOM));
                            } else {
                                final LocationRequest locationRequest = LocationRequest.create();
                                locationRequest.setInterval(10000);
                                locationRequest.setFastestInterval(5000);
                                locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                                locationCallback = new LocationCallback() {
                                    @Override
                                    public void onLocationResult(LocationResult locationResult) {
                                        super.onLocationResult(locationResult);
                                        if (locationResult == null) {
                                            return;
                                        }
                                        mLastKnownLocation = locationResult.getLastLocation();
                                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude()), DEFAULT_ZOOM));
                                        mFusedLocationProviderClient.removeLocationUpdates(locationCallback);
                                    }
                                };
                                mFusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, null);

                            }
                        } else {
                            Toast.makeText(MapActivity.this, "unable to get last location", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

}
