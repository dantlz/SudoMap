package com.anchronize.sudomap;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.anchronize.sudomap.objects.EnumerationClasses;
import com.anchronize.sudomap.objects.Event;
import com.anchronize.sudomap.objects.User;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class EventDetailActivity extends AppCompatActivity implements
        GoogleMap.OnMyLocationButtonClickListener,
        OnMapReadyCallback,
        ActivityCompat.OnRequestPermissionsResultCallback{
    //A unique keyname, so that mainActivity can use this key and pass selected event to this activity
    public static final String EVENT_KEY = "com.anchronize.sudomap.EventDetailActivity.event";

    private TextView titleView;
    private TextView locationNameView;
    private TextView locationAddress;
    private TextView descriptionView;
    private HorizontalScrollView attendantsScrollView;
    private Button chatButton;

    private Event mEvent;
    private GoogleMap mMap;

    //TODO Delete this dummy method
    public Event hardCodedEvent(){
        Event e = new Event();
        e.setTitle("Dope Event");
        e.setDescription("THIS IS MEANT TO BE A SUPER LONG STRING THIS IS MEANT TO BE A SUPER LONG STRING THIS IS MEANT TO BE A SUPER LONG STRING THIS IS MEANT TO BE A SUPER LONG STRING THIS IS MEANT TO BE A SUPER LONG STRING THIS IS MEANT TO BE A SUPER LONG STRING THIS IS MEANT TO BE A SUPER LONG STRING THIS IS MEANT TO BE A SUPER LONG STRING THIS IS MEANT TO BE A SUPER LONG STRING THIS IS MEANT TO BE A SUPER LONG STRING THIS IS MEANT TO BE A SUPER LONG STRING THIS IS MEANT TO BE A SUPER LONG STRING THIS IS MEANT TO BE A SUPER LONG STRING THIS IS MEANT TO BE A SUPER LONG STRING THIS IS MEANT TO BE A SUPER LONG STRING THIS IS MEANT TO BE A SUPER LONG STRING THIS IS MEANT TO BE A SUPER LONG STRING THIS IS MEANT TO BE A SUPER LONG STRING THIS IS MEANT TO BE A SUPER LONG STRING THIS IS MEANT TO BE A SUPER LONG STRING THIS IS MEANT TO BE A SUPER LONG STRING THIS IS MEANT TO BE A SUPER LONG STRING THIS IS MEANT TO BE A SUPER LONG STRING THIS IS MEANT TO BE A SUPER LONG STRING THIS IS MEANT TO BE A SUPER LONG STRING THIS IS MEANT TO BE A SUPER LONG STRING THIS IS MEANT TO BE A SUPER LONG STRING ");
        e.setCategory(EnumerationClasses.Categories.FUN.toString());
        e.setLongitude(-118.279838);
        e.setLatitude(34.022799);
        e.setPrivacy(true);
        e.setVisible(true);
        e.setOrganizer(new User("TEST organizer"));
        return e;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);

        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.eventsMap);
        mapFragment.getMapAsync(this);

        titleView = (TextView) findViewById(R.id.eventTitle);
        locationNameView = (TextView) findViewById(R.id.eventLocationNameTextView);
        locationAddress = (TextView) findViewById(R.id.eventLocationAddressTextView);
        descriptionView = (TextView) findViewById(R.id.eventDescriptionView);
        attendantsScrollView = (HorizontalScrollView) findViewById(R.id.attendantsScrollView);
        chatButton = (Button) findViewById(R.id.chatButton);

        //Get the event that get passed in
        Intent i = getIntent();
        mEvent = (Event)i.getSerializableExtra(EVENT_KEY);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        populateDetails();
    }

    public void populateDetails(){
        titleView.setText(mEvent.getTitle());
        locationNameView.setText(
                "Location: "+ nameFromLatLng(mEvent.getLatitude(),mEvent.getLongitude()));
        locationAddress.setText(
                "Address: "+ addressFromLatLng(mEvent.getLatitude(), mEvent.getLongitude()));
        descriptionView.setText(mEvent.getDescription());
        mMap.addMarker(new MarkerOptions().position(new
                LatLng(mEvent.getLatitude(), mEvent.getLongitude())).title("Hello world"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mEvent.getLatitude(), mEvent.getLongitude()),15));
        //TODO Populate the attendants horizontal scroll view | Tinder scrolling
        chatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chatButtonClicked();
            }
        });
    }

    public void chatButtonClicked(){
        Intent i = new Intent(this, ChatActivity.class);
        startActivity(i);
    }

    public String addressFromLatLng(double lat, double lng){
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(this, Locale.getDefault());

        try {
            addresses = geocoder.getFromLocation(lat, lng, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            return addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "RETRIEVE ADDRESS FAILED";
    }

    public String nameFromLatLng(double lat, double lng){
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(this, Locale.getDefault());

        try {
            addresses = geocoder.getFromLocation(lat, lng, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            return addresses.get(0).getFeatureName(); // Only if available else return NULL
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "RETRIEVE ADDRESS NAME FAILED";
    }

    @Override
    public boolean onMyLocationButtonClick() {
        return false;
    }
}
