/*
 * Copyright (c) 2017. Team CMPUT301F17T02, CMPUT301, University of Alberta - All Rights Reserved. You may use, distribute, or modify this code under terms and conditions of the Code of Students Behaviour at University of Alberta.
 */

package com.example.baard;

import android.Manifest;
import android.content.SharedPreferences;
import android.location.Location;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;

import static android.view.View.VISIBLE;

public class AddLocationActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private static final float DEFAULT_ZOOM = 14.0f;
    private GoogleMap mMap;
    private LatLng mDefaultLocation = new LatLng(53.5444, -113.490);
    private LatLng pinPosition, currentPosition;
    private Gson gson;
    private SharedPreferences sharedPrefs;
    private SharedPreferences.Editor sharedPrefsEditor;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private Location mLastKnownLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_location);

        gson = new Gson();
        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        sharedPrefsEditor = sharedPrefs.edit();

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) == -1) { // not allowed, so request
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != -1) {
        //    SingleShotLocationProvider.requestSingleUpdate(this,
        //            new SingleShotLocationProvider.LocationCallback() {
        //                @Override
        //                public void onNewLocationAvailable(LatLng location) {
        //                    currentPosition = location;
//      //                      mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentPosition, 14.0f));
//      //                      marker.position(currentPosition);
        //                }
        //            });
        //}

        //// Obtain the SupportMapFragment and get notified when the map is ready to be used.
        //SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
        //        .findFragmentById(R.id.map);
        //mapFragment.getMapAsync(this);
    }

//    @Override
//    protected void onStart() {
//        super.onStart();
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
//    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    public void onMapReady(GoogleMap googleMap) {
        //final MarkerOptions marker = new MarkerOptions()
        //        .title("Habit Event Location")
        //        .snippet("Is this the right location?")
        //        .position(mDefaultLocation);
        mMap = googleMap;

        //mMap.addMarker(marker).setDraggable(true);
        //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mDefaultLocation, DEFAULT_ZOOM));
        //pinPosition = marker.getPosition();

        //Log.d("Add_Location", "FLAG0");
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION)  != -1){
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(true);
        }

        //mMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
        //    @Override
        //    public boolean onMyLocationButtonClick() {
        //        getDeviceLocation();
        //        return true;
        //    }
        //}) ;

        //mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
        //    @Override
        //    public void onMarkerDragStart(Marker marker) {
        //        pinPosition = marker.getPosition();
        //    }

        //    @Override
        //    public void onMarkerDrag(Marker marker) {

        //    }

        //    @Override
        //    public void onMarkerDragEnd(Marker marker) {
        //        pinPosition = marker.getPosition();
        //    }
        //});
    }

    private void getDeviceLocation() {
    /*
     * Get the best and most recent location of the device, which may be null in rare
     * cases when a location is not available.
     */
        try {
            if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != -1) {
                Task locationResult = mFusedLocationProviderClient.getLastLocation();
                locationResult.addOnCompleteListener(this, new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()) {
                            // Set the map's camera position to the current location of the device.
                            mLastKnownLocation = (Location) task.getResult();
                            mMap.addMarker(new MarkerOptions().position(new LatLng(mLastKnownLocation.getLatitude(),
                                    mLastKnownLocation.getLongitude())).title("My Location"));
                            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                                    new LatLng(mLastKnownLocation.getLatitude(),
                                            mLastKnownLocation.getLongitude()), DEFAULT_ZOOM));
                        } else {
                            Log.d("Add_Location", "Current location is null. Using defaults.");
                            Log.e("Add_Location", "Exception: %s", task.getException());
                            mMap.addMarker(new MarkerOptions().position(mDefaultLocation).title("My Location"));
                            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(mDefaultLocation, DEFAULT_ZOOM));
                            mMap.getUiSettings().setMyLocationButtonEnabled(false);
                        }
                    }
                });
            }
        } catch(SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
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
