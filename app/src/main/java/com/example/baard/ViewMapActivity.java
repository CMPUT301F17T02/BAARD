/*
 * Copyright (c) 2017. Team CMPUT301F17T02, CMPUT301, University of Alberta - All Rights Reserved. You may use, distribute, or modify this code under terms and conditions of the Code of Students Behaviour at University of Alberta.
 */

package com.example.baard;

import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.function.BiConsumer;

public class ViewMapActivity extends AppCompatActivity
        implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private static final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private GoogleMap mMap;
    private HashMap<LatLng, Marker> myMarkers = new HashMap<>();
    private HashMap<LatLng, Marker> friendMarkers = new HashMap<>();
    private User user;

    private LatLng mDefaultLocation = new LatLng(53.5444, -113.490);
    private LatLng mCurrentLocation = mDefaultLocation;
    private static final float DISTANCE = 5f;
    private static final float DEFAULT_ZOOM = 14.0f;

    private SharedPreferences sharedPrefs;
    private Gson gson;

    private Location mLastKnownLocation;
    private boolean mLocationPermissionGranted = false;
    private LocationRequest mLocationRequest;
    private LocationCallback mLocationCallback;
    private GoogleApiClient mGoogleApiClient;
    private FileController fileController;


    /**
     * Set up the view map activity
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_map);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setActionBarTitle(getString(R.string.title_activity_view_map));
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.

        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        gson = new Gson();
        String json = sharedPrefs.getString("username", "");
        String username = gson.fromJson(json, new TypeToken<String>() {}.getType());

        fileController = new FileController();
        user = fileController.loadUser(getApplicationContext(), username);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    /**
     *  Copied from https://stackoverflow.com/questions/8607707/how-to-set-a-custom-font-in-the-actionbar-title
     */
    private void setActionBarTitle(String str) {
        String fontPath = "Raleway-Regular.ttf";

        SpannableString s = new SpannableString(str);
        s.setSpan(new TypefaceSpan(this, fontPath), 0, s.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        // Update the action bar title with the TypefaceSpan instance
        getSupportActionBar().setTitle(s);
    }

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
        // set the camera to the default location with zoom enabled
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mDefaultLocation, 14.0f));
        mMap.getUiSettings().setZoomControlsEnabled(true);

        // set markers for user
        setMarkers(user, false, myMarkers);

        try {
            // now make visible all markers that are filtered in habit history
            String json = sharedPrefs.getString("filteredHabitEvents", "");
            List<HabitEvent> filteredHabitEvents = gson.fromJson(json, new TypeToken<List<HabitEvent>>() {}.getType());
            setVisibleMarkers(filteredHabitEvents, true);
        } catch (Exception e) {
            // in case there was no filter saved, just show them all
            Toast.makeText(this, "NOTE: No Filter. Go to HABIT HISTORY!", Toast.LENGTH_LONG).show();
            for (Habit habit : user.getHabits().getArrayList()) {
                setVisibleMarkers(habit.getEvents().getArrayList(), true);
            }
        }

        // set the markers for friends
        if (user.getFriends().size() > 0) {
            for (String friendStr : user.getFriends().keySet()) {
                User friend = fileController.loadUserFromServer(friendStr);
                setMarkers(friend, true, friendMarkers);
            }
        }

        buildGoogleApiClient();
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        getLocationPermission();
        updateLocationUI();
        getDeviceLocation();
    }

    @Override
    public void onConnectionSuspended(int i) {}

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {}

    private void getLocationPermission() {
    /*
     * Request location permission, so that we can get the location of the
     * device. The result of the permission request is handled by a callback,
     * onRequestPermissionsResult.
     */
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        mLocationPermissionGranted = false;
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true;
                }
            }
        }
        updateLocationUI();
        getDeviceLocation();
    }

    private void updateLocationUI() {
        if (mMap == null) {
            return;
        }
        try {
            if (mLocationPermissionGranted) {
                mMap.setMyLocationEnabled(true);
                mMap.getUiSettings().setMyLocationButtonEnabled(true);
            } else {
                mMap.setMyLocationEnabled(false);
                mMap.getUiSettings().setMyLocationButtonEnabled(false);
                mLastKnownLocation = null;
            }
        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    private void getDeviceLocation() {
    /*
     * Get the best and most recent location of the device, which may be null in rare
     * cases when a location is not available.
     */
        try {
            mMap.clear();
            if (mLocationPermissionGranted) {
                // Location Request to obtain location through GPS or network when  mLastKnownLocation is null
                mLocationRequest = new LocationRequest()
                        .setInterval(1000)
                        .setFastestInterval(1000)
                        .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

                // Callback to handle new Location
                mLocationCallback = new LocationCallback() {
                    @SuppressWarnings("MissingPermission")
                    @Override
                    public void onLocationResult(LocationResult locationResult) {
                        for (Location location : locationResult.getLocations()) {
                            mLastKnownLocation = location;
                        }
                        mMap.setMyLocationEnabled(true);
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                new LatLng(mLastKnownLocation.getLatitude(),
                                        mLastKnownLocation.getLongitude()), DEFAULT_ZOOM));
                        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, mLocationCallback);
                    }
                };
                // Get last known location
                mLastKnownLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
                if (mLastKnownLocation != null) {
                    mCurrentLocation = new LatLng(mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude());
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                            new LatLng(mLastKnownLocation.getLatitude(),
                                    mLastKnownLocation.getLongitude()), DEFAULT_ZOOM));
                } else {
                    Log.d("Add_Location", "Current location is null. Using defaults.");
                    mCurrentLocation = mDefaultLocation;
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mDefaultLocation, DEFAULT_ZOOM));
                    mMap.setMyLocationEnabled(false);
                }
                LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, mLocationCallback, null);
            } else {
                Log.d("Add_Location", "Permission is not granted.");
                mCurrentLocation = mDefaultLocation;
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mDefaultLocation, DEFAULT_ZOOM));
            }
        } catch(SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    /**
     * Depending on the filter set by the user, make the appropriate markers visible for them
     * to see on the map.
     * @param events
     * @param withDistance boolean to set whether or not distance should be calculated
     */
    private void setVisibleMarkers(List<HabitEvent> events, Boolean withDistance) {
        for (HabitEvent habitEvent : events) {
            LatLng location = habitEvent.getLocation();
            if (myMarkers.get(location) != null) {
                float[] dist = {0f,0f,0f};
                Location.distanceBetween(mCurrentLocation.latitude, mCurrentLocation.longitude,
                        location.latitude, location.longitude, dist);
                if (withDistance) {
                    if (dist[0] <= DISTANCE) {
                        myMarkers.get(location).setVisible(true);
                    }
                } else {
                    myMarkers.get(location).setVisible(true);
                }
            }
        }

    }

    /**
     * Generates all markers for the map.
     * By specifying whether or not it is a friend, the color is set appropriately,
     * as well as only showing the most recent habit event for the friend.
     * @param user
     * @param isFriend
     * @param markerMap
     */
    private void setMarkers(User user, Boolean isFriend, HashMap<LatLng, Marker> markerMap) {
        SimpleDateFormat sourceFormat = new SimpleDateFormat("dd/MM/yyyy");
        Float bitMapColor;

        if (isFriend) { //green for friend
            bitMapColor = BitmapDescriptorFactory.HUE_BLUE;
        } else { // red for user
            bitMapColor = BitmapDescriptorFactory.HUE_RED;
        }

        // set all the markers a user has on their events
        for (Habit habit : user.getHabits().getArrayList()) {
            // to ensure we start at the last place in the list in case this call is for a friend
            for (int i = habit.getEvents().size()-1; i >=0; i--) {
                HabitEvent habitEvent = habit.getEvents().getHabitEvent(i);
                if (habitEvent.getLocation() != null) {
                    Marker marker = mMap.addMarker(new MarkerOptions().position(habitEvent.getLocation())
                            .draggable(false)
                            .title(habit.getTitle())
                            .visible(isFriend)
                            .icon(BitmapDescriptorFactory.defaultMarker(bitMapColor))
                            .snippet(sourceFormat.format(habitEvent.getEventDate())));
                    markerMap.put(habitEvent.getLocation(), marker);
                }
                if (isFriend) {
                    break;
                }
            }
        }
    }
}
