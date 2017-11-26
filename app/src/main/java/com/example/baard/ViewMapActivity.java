/*
 * Copyright (c) 2017. Team CMPUT301F17T02, CMPUT301, University of Alberta - All Rights Reserved. You may use, distribute, or modify this code under terms and conditions of the Code of Students Behaviour at University of Alberta.
 */

package com.example.baard;

import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class ViewMapActivity extends FragmentActivity implements OnMapReadyCallback {

    private static final String TAG = "ViewMapActivity";
    private GoogleMap mMap;
    private boolean mLocationPermissionGranted;
    private LatLng mDefaultLocation = new LatLng(53.5444, -113.490);
    private LatLng mLastKnownLocation = new LatLng(53.5444, -113.490);
    private Object mFusedLocationProviderClient;

    private static final String KEY_CAMERA_POSITION = "camera_position";
    private static final String KEY_LOCATION = "location";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_map);


//        if (savedInstanceState != null) {
//            mCurrentLocation = savedInstanceState.getParcelable(KEY_LOCATION);
//            mCameraPosition = savedInstanceState.getParcelable(KEY_CAMERA_POSITION);
//        }

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


//    @Override
//    protected void onSaveInstanceState(Bundle outState) {
//        if (mMap != null) {
//            outState.putParcelable(KEY_CAMERA_POSITION, mMap.getCameraPosition());
//            outState.putParcelable(KEY_LOCATION, mLastKnownLocation);
//            super.onSaveInstanceState(outState);
//        }
//    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Do other setup activities here too, as described elsewhere in this tutorial.
        mMap.addMarker(new MarkerOptions().position(mDefaultLocation).title("Marker in Edmonton"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(mDefaultLocation));
        mMap.setMinZoomPreference(2.0f);

    }

}
