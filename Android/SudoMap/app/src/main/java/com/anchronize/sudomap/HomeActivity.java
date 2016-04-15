package com.anchronize.sudomap;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import com.anchronize.sudomap.objects.Event;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by jasonlin on 3/15/16.
 */
//public class HomeActivity {
//}

public class HomeActivity extends NavigationDrawer
        implements
        GoogleMap.OnMyLocationButtonClickListener,
        GoogleMap.OnInfoWindowClickListener,
        GoogleMap.OnInfoWindowCloseListener,
        OnMapReadyCallback,
        ActivityCompat.OnRequestPermissionsResultCallback,
        GoogleMap.OnMarkerClickListener{

    /**
     * Request code for location permission request.
     *
     * @see #onRequestPermissionsResult(int, String[], int[])
     */
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    /**
     * Flag indicating whether a requested permission has been denied after returning in
     * {@link #onRequestPermissionsResult(int, String[], int[])}.
     */
    private boolean mPermissionDenied = false;

    private GoogleMap mMap;

//    //Test Marker LatLng
//    private static final LatLng USC = new LatLng(34.0224 , -118.2851);

    private Marker lastSelectedMarker;

    private Firebase ref;

    private ArrayList<Event> allEventsinFirebase;   //store all events in database

    private HashMap<Marker, Event> markerEventHashMap; //maintain a map from maker to event

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_demo);

        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        allEventsinFirebase = new ArrayList<Event>();
        markerEventHashMap = new HashMap<Marker,Event>();
        //set context for firebase
        Firebase.setAndroidContext(this);
        ref = new Firebase("https://anchronize.firebaseio.com");
        //create a Firebase reference to the child tree "event"
        Firebase refEvents = ref.child("events");
        Firebase refUsers = ref.child("user");

        //query data once for to get all the events
        refEvents.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                allEventsinFirebase.clear();
//                System.out.println(snapshot.getValue());
//                System.out.println("There are " + snapshot.getChildrenCount() + " events");
                for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                    Event event = postSnapshot.getValue(Event.class);
                    allEventsinFirebase.add(event);
                }
                if ( ((SudoMapApplication)getApplication()).getAuthenticateStatus() == true)
                    addMapMarkers();
            }
            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

    }

    @Override
    public void onMapReady(GoogleMap map) {
        mMap = map;

        mMap.setOnMyLocationButtonClickListener(this);
        enableMyLocation();


       // addMapMarkers();

        mMap.setOnMarkerClickListener(this);
        mMap.setOnInfoWindowClickListener(this);
        mMap.setOnInfoWindowCloseListener(this);
        //mMap.setOnMarkerDragListener(this);
    }

    @Override
    public boolean onMarkerClick(final Marker marker) {
        lastSelectedMarker = marker;

        return false;
    }

    @Override
    public void onInfoWindowClick(Marker marker) {

        Event e = markerEventHashMap.get(lastSelectedMarker);
        Intent i = new Intent(getApplicationContext(), EventDetailActivity.class);
        i.putExtra(EventDetailActivity.EVENT_KEY, e);
        //// TODO: 4/14/16  startActivity for result 
        startActivity(i);
    }

    @Override
    public void onInfoWindowClose(Marker marker) {
        Toast.makeText(this, "Info window closed", Toast.LENGTH_SHORT).show();
    }

    private void addMapMarkers(){

        markerEventHashMap.clear();
        for(Event e : allEventsinFirebase){

            Marker marker = mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(e.getLatitude(), e.getLongitude()))
                    .title(e.getTitle())
                    .snippet(e.getCategory())
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
            markerEventHashMap.put(marker, e);
        }

    }

    /**
     * Enables the My Location layer if the fine location permission has been granted.
     */
    private void enableMyLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission to access the location is missing.
            com.anchronize.sudomap.PermissionUtils.requestPermission(this, LOCATION_PERMISSION_REQUEST_CODE,
                    Manifest.permission.ACCESS_FINE_LOCATION, true);
        } else if (mMap != null) {
            // Access to the location has been granted to the app.
            mMap.setMyLocationEnabled(true);

            // Getting last location and zooming to that level w/o animation
            LocationManager locationManager = (LocationManager)
                    getSystemService(Context.LOCATION_SERVICE);
            Criteria criteria = new Criteria();

            Location location = locationManager.getLastKnownLocation(locationManager
                    .getBestProvider(criteria, false));

//            mMap.moveCamera( CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 17) );
            // Customize camera position:https://developers.google.com/maps/documentation/android-api/views#the_camera_position
            CameraPosition currentPosition = new CameraPosition.Builder()
//                    .target(new LatLng(location.getLatitude(), location.getLongitude()))
                    .target(new LatLng(34.0213578, -118.2846286))
                    .zoom(16) // this is the zoom level
                    .bearing(35)   // this is the rotation angle
                    .tilt(40)   // this is the degree of elevation
                    .build();

            mMap.moveCamera(CameraUpdateFactory.newCameraPosition(currentPosition));
            mMap.animateCamera(CameraUpdateFactory.scrollBy(-100,-50));

            mMap.setBuildingsEnabled(true);

//            Commented out so it doesn't crash
//            // adding marker and testing anchor
//            mMap.addMarker(new MarkerOptions()
////                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.red_pin))
//                    .anchor(0.0f, 1.0f) // Anchors the marker on the bottom left
//                    .position(new LatLng(location.getLatitude(), location.getLongitude())));
        }
    }

    @Override
    public boolean onMyLocationButtonClick() {
        Toast.makeText(this, "MyLocation button clicked", Toast.LENGTH_SHORT).show();
        // Return false so that we don't consume the event and the default behavior still occurs
        // (the camera animates to the user's current position).
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode != LOCATION_PERMISSION_REQUEST_CODE) {
            return;
        }

        if (com.anchronize.sudomap.PermissionUtils.isPermissionGranted(permissions, grantResults,
                Manifest.permission.ACCESS_FINE_LOCATION)) {
            // Enable the my location layer if the permission has been granted.
            enableMyLocation();
        } else {
            // Display the missing permission error dialog when the fragments resume.
            mPermissionDenied = true;
        }
    }
    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
        if (mPermissionDenied) {
            // Permission was not granted, display error dialog.
            showMissingPermissionError();
            mPermissionDenied = false;
        }
    }

    /**
     * Displays a dialog with error message explaining that the location permission is missing.
     */
    private void showMissingPermissionError() {
        com.anchronize.sudomap.PermissionUtils.PermissionDeniedDialog
                .newInstance(true).show(getSupportFragmentManager(), "dialog");
    }
}