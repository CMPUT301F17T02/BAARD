/*
 * Copyright (c) 2017. Team CMPUT301F17T02, CMPUT301, University of Alberta - All Rights Reserved. You may use, distribute, or modify this code under terms and conditions of the Code of Students Behaviour at University of Alberta.
 */

package com.example.baard;

import android.Manifest;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;

import static android.view.View.VISIBLE;

public class AddLocationActivity extends FragmentActivity implements OnMapReadyCallback {

    private static final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private GoogleMap mMap;
    private LatLng mDefaultLocation = new LatLng(53.5444, -113.490);
    private LatLng pinPosition, currentPosition;
    private Gson gson;
    private SharedPreferences sharedPrefs;
    private SharedPreferences.Editor sharedPrefsEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_location);

//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        toolbar.setVisibility(VISIBLE);

        gson = new Gson();
        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        sharedPrefsEditor = sharedPrefs.edit();

        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) == -1) { // not allowed, so request
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }

        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != -1) {
            SingleShotLocationProvider.requestSingleUpdate(this,
                    new SingleShotLocationProvider.LocationCallback() {
                        @Override
                        public void onNewLocationAvailable(LatLng location) {
                            currentPosition = location;
//                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentPosition, 14.0f));
//                            marker.position(currentPosition);
                        }
                    });
        }

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
//        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != -1) {
//            SingleShotLocationProvider.requestSingleUpdate(this,
//                    new SingleShotLocationProvider.LocationCallback() {
//                        @Override public void onNewLocationAvailable(LatLng location) {
//                            currentPosition = location;
//                        }
//                    });

//            mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
//            mFusedLocationClient.getLastLocation()
//                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
//                        @Override
//                        public void onSuccess(Location location) {
//                            // Got last known location. In some rare situations this can be null.
//                            if (location != null) {
//                                // Logic to handle location object
//                                currentPosition = new LatLng(location.getLatitude(), location.getLongitude());
//                            }
//                        }
//                    });
//        }
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        final MarkerOptions marker = new MarkerOptions()
                .title("Habit Event Location")
                .snippet("Is this the right location?")
                .position(mDefaultLocation);
        mMap = googleMap;
        mMap.getUiSettings().setZoomControlsEnabled(true);

        mMap.addMarker(marker).setDraggable(true);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mDefaultLocation, 14.0f));
        pinPosition = marker.getPosition();

        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION)  != -1){
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(true);
        }
        //mMap.setOnMyLocationButtonClickListener(this);


        mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(Marker marker) {
                pinPosition = marker.getPosition();
            }

            @Override
            public void onMarkerDrag(Marker marker) {

            }

            @Override
            public void onMarkerDragEnd(Marker marker) {
                pinPosition = marker.getPosition();
            }
        });
    }

    public void cancel(View view) {
        sharedPrefsEditor.remove("locationPosition");
        sharedPrefsEditor.commit();
        finish();
    }

    public void save(View view) {
        String json = gson.toJson(pinPosition);
        sharedPrefsEditor.putString("locationPosition", json);
        sharedPrefsEditor.commit();
        finish();
    }

}
