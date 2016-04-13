package com.anchronize.sudomap;

import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.anchronize.sudomap.objects.EnumerationClasses;
import com.anchronize.sudomap.objects.Event;
import com.anchronize.sudomap.objects.User;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class EventDetailActivity extends AppCompatActivity implements
        GoogleMap.OnMyLocationButtonClickListener,
        OnMapReadyCallback,
        ActivityCompat.OnRequestPermissionsResultCallback{

    private Event mEvent;
    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);

        //Pass in an event from HomeActivity, set mEvent to the passed in event.
        //Delete the hardcoded event below
        mEvent = new Event("TEST");
        mEvent.setTitle("TEST title");
        mEvent.setDescription("TEST description");
        mEvent.setCategory(EnumerationClasses.Categories.FUN.toString());
        mEvent.setLongitude(34.022799);
        mEvent.setLatitude(-118.279838);
        mEvent.setPrivacy(true);
        mEvent.setVisible(true);
        mEvent.setOrganizer(new User("TEST organizer"));
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        populateDetails();
    }

    public void populateDetails(){
        mMap.addMarker(new MarkerOptions().position(new
                LatLng(mEvent.getLatitude(), mEvent.getLongitude())).title("Hello world"));
    }

    @Override
    public boolean onMyLocationButtonClick() {
        return false;
    }
}
