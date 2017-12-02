/*
 * Copyright (c) 2017. Team CMPUT301F17T02, CMPUT301, University of Alberta - All Rights Reserved. You may use, distribute, or modify this code under terms and conditions of the Code of Students Behaviour at University of Alberta.
 */

package com.example.baard;

import android.content.SharedPreferences;
import android.location.Location;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

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

public class ViewMapActivity extends FragmentActivity implements OnMapReadyCallback {

    private static final String TAG = "ViewMapActivity";

    private GoogleMap mMap;
    private HashMap<LatLng, Marker> myMarkers = new HashMap<>();
    private HashMap<LatLng, Marker> friendMarkers = new HashMap<>();
    private User user;

    private boolean mLocationPermissionGranted;
    private LatLng mDefaultLocation = new LatLng(53.5444, -113.490);
    private LatLng mLastKnownLocation = new LatLng(53.5444, -113.490);
    private Object mFusedLocationProviderClient;

    private static final String KEY_CAMERA_POSITION = "camera_position";
    private static final String KEY_LOCATION = "location";
    private static final float DISTANCE = 5000f;

    private SharedPreferences sharedPrefs;
    private Gson gson;

    /**
     * Set up the view map activity
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_map);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.

        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        gson = new Gson();
        String json = sharedPrefs.getString("username", "");
        String username = gson.fromJson(json, new TypeToken<String>() {}.getType());

        FileController fc = new FileController();
        user = fc.loadUser(getApplicationContext(), username);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
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
            for (User friend : user.getFriends().getArrayList()) {
                setMarkers(friend, true, friendMarkers);
            }
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
                Location.distanceBetween(mDefaultLocation.latitude, mDefaultLocation.longitude,
                        location.latitude, location.longitude, dist);
                if (withDistance && (dist[0] <= DISTANCE)) {
                    myMarkers.get(location).setVisible(true);
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
