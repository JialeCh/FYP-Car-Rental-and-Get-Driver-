package com.example.ez_rental.FragmentPages;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.ez_rental.R;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.location.places.ui.SupportPlaceAutocompleteFragment;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import static android.content.ContentValues.TAG;

/*import com.google.android.libraries.places.compat.Place;
import com.google.android.libraries.places.compat.ui.PlaceSelectionListener;
import com.google.android.libraries.places.compat.ui.SupportPlaceAutocompleteFragment;*/

public class MapsFragment extends Fragment implements OnMapReadyCallback {


    private static final View TODO = null;
    private GoogleMap mMap;
    private LatLng locationLat;
    private MapFragment mapFragment;
    private String locationName;
    private FusedLocationProviderClient fusedLocationClient;
    Marker marker_1;
    String placeName;
    private ImageView btn;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_maps, container, false);

        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
        SupportMapFragment smf = new SupportMapFragment();
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.map, smf);
        ft.commit();

        setupAutoCompleteFragment(smf);
        return view;
    }

    private void setupAutoCompleteFragment(SupportMapFragment smf) {
        SupportPlaceAutocompleteFragment autocompleteFragment = new SupportPlaceAutocompleteFragment();
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.fragment_content, autocompleteFragment);
        ft.commit();

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {

            @Override
            public void onPlaceSelected(com.google.android.gms.location.places.Place place) {
                // TODO: Get info about the selected place.
                Log.i(TAG, "Place: " + place.getName());
                locationLat = place.getLatLng();
                locationName = place.getName().toString();
                smf.getMapAsync(MapsFragment.this);
                placeName= place.getName().toString();
                final AlertDialog dialog = new AlertDialog.Builder(getContext())
                        .setTitle("Rent Place")
                        .setMessage("Confirmed " + placeName+ " as your rent place ?")
                        .setPositiveButton("Ok", null)
                        .setNegativeButton("Cancel", null)
                        .show();
                Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                Button negativeButton = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
                positiveButton.setOnClickListener(v -> {
                    dialog.dismiss();
                    Bundle bundle = new Bundle();
                    bundle.putString("location",placeName);
                    FragmentManager fm =getFragmentManager();
                    FragmentTransaction ft = fm.beginTransaction();
                    CarFragment carFragment = new CarFragment();
                    carFragment.setArguments(bundle);
                    ft.replace(R.id.framelayout,carFragment);
                    ft.commit();
                });
                negativeButton.setOnClickListener(v -> {
                    Toast.makeText(getContext(), "Close", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                });
            }


            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Log.i(TAG, "An error occurred: " + status);
            }

        });

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(locationLat, 8.5f));
        mMap.addMarker(new MarkerOptions()
                .position(locationLat)
                .title(locationName)
                .snippet("Your Location")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));

        mMap.setOnMarkerClickListener(marker -> {

            if (marker.equals(marker_1)) {
                Log.w("Click", "test");
                Toast.makeText(getActivity(), "place.getName()", Toast.LENGTH_LONG).show();

                return true;
            }
            return false;
        });


    }

    @Override
    public void onResume() {
        super.onResume();
        if (mMap != null) {
            mMap.clear();
        }
    }
}



