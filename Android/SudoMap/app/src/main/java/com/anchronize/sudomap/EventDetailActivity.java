package com.anchronize.sudomap;

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

    private TextView titleView;
    private TextView locationNameView;
    private TextView locationAddress;
    private TextView descriptionView;
    private HorizontalScrollView attendantsScrollView;
    private Button chatButton;

    private Event mEvent;
    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);

        titleView = (TextView) findViewById(R.id.eventTitle);
        locationNameView = (TextView) findViewById(R.id.eventLocationNameTextView);
        locationAddress = (TextView) findViewById(R.id.eventLocationAddressTextView);
        descriptionView = (TextView) findViewById(R.id.eventDescriptionView);
        attendantsScrollView = (HorizontalScrollView) findViewById(R.id.attendantsScrollView);
        chatButton = (Button) findViewById(R.id.chatButton);

        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.eventsMap);
        mapFragment.getMapAsync(this);
    }

    public void setEvent(Event event){
        this.mEvent = event;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        //Toast.makeText(EventDetailActivity.this, "MAP READY", Toast.LENGTH_SHORT).show();

        mMap = googleMap;

        //Pass in an event from HomeActivity by calling setEvent.
        //Delete the hardcoded event below
        mEvent = new Event("TEST");
        mEvent.setTitle("Dope Event");
        mEvent.setDescription("THIS IS MEANT TO BE A SUPER LONG STRING THIS IS MEANT TO BE A SUPER LONG STRING THIS IS MEANT TO BE A SUPER LONG STRING THIS IS MEANT TO BE A SUPER LONG STRING THIS IS MEANT TO BE A SUPER LONG STRING THIS IS MEANT TO BE A SUPER LONG STRING THIS IS MEANT TO BE A SUPER LONG STRING THIS IS MEANT TO BE A SUPER LONG STRING THIS IS MEANT TO BE A SUPER LONG STRING THIS IS MEANT TO BE A SUPER LONG STRING THIS IS MEANT TO BE A SUPER LONG STRING THIS IS MEANT TO BE A SUPER LONG STRING THIS IS MEANT TO BE A SUPER LONG STRING THIS IS MEANT TO BE A SUPER LONG STRING THIS IS MEANT TO BE A SUPER LONG STRING THIS IS MEANT TO BE A SUPER LONG STRING THIS IS MEANT TO BE A SUPER LONG STRING THIS IS MEANT TO BE A SUPER LONG STRING THIS IS MEANT TO BE A SUPER LONG STRING THIS IS MEANT TO BE A SUPER LONG STRING THIS IS MEANT TO BE A SUPER LONG STRING THIS IS MEANT TO BE A SUPER LONG STRING THIS IS MEANT TO BE A SUPER LONG STRING THIS IS MEANT TO BE A SUPER LONG STRING THIS IS MEANT TO BE A SUPER LONG STRING THIS IS MEANT TO BE A SUPER LONG STRING THIS IS MEANT TO BE A SUPER LONG STRING ");
        mEvent.setCategory(EnumerationClasses.Categories.FUN.toString());
        mEvent.setLongitude(-118.279838);
        mEvent.setLatitude(34.022799);
        mEvent.setPrivacy(true);
        mEvent.setVisible(true);
        mEvent.setOrganizer(new User("TEST organizer"));

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
        //Populate the attendants horizontal scroll view | Tinder scrolling
        chatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chatButtonClicked();
            }
        });
    }

    public void chatButtonClicked(){
        Toast.makeText(EventDetailActivity.this, "Chat Button Clicked", Toast.LENGTH_SHORT).show();
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
