package com.anchronize.sudomap;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.anchronize.sudomap.objects.Event;
import com.anchronize.sudomap.objects.User;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Locale;

public class EventDetailActivity extends AppCompatActivity implements
        GoogleMap.OnMyLocationButtonClickListener,
        OnMapReadyCallback,
        ActivityCompat.OnRequestPermissionsResultCallback{
    //A unique keyname, so that mainActivity can use this key and pass selected event to this activity
    public static final String EVENT_KEY = "com.anchronize.sudomap.EventDetailActivity.event";

    private TextView titleView;
    private TextView organizerView;
    private TextView locationNameView;
    private TextView locationAddress;
    private TextView descriptionView;
    private HorizontalScrollView attendantsScrollView;
    private LinearLayout attendantsView;
    private Button chatButton;
    private Button bookmarkButton;
    private Button attendingButton;

    private Event mEvent;
    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);
        initializeComponents();
        addListeners();
    }

    public void initializeComponents(){
        //Current Event
        Intent i = getIntent();
        mEvent = (Event)i.getSerializableExtra(EVENT_KEY);

        //Map
        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.eventsMap);
        mapFragment.getMapAsync(this);

        //GUI
        titleView = (TextView) findViewById(R.id.eventTitle);
        organizerView = (TextView) findViewById(R.id.organizerInfo);
        locationNameView = (TextView) findViewById(R.id.eventLocationNameTextView);
        locationAddress = (TextView) findViewById(R.id.eventLocationAddressTextView);
        descriptionView = (TextView) findViewById(R.id.eventDescriptionView);
        attendantsScrollView = (HorizontalScrollView) findViewById(R.id.attendantsScrollView);
        attendantsView = (LinearLayout) findViewById(R.id.attendants);
        chatButton = (Button) findViewById(R.id.chatButton);
        bookmarkButton = (Button) findViewById(R.id.bookmarkButton);
        attendingButton = (Button) findViewById(R.id.attendingButton);
    }

    public void addListeners(){
        chatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chatButtonClicked();
            }
        });
        bookmarkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bookmarkButtonClicked();
            }
        });
        attendingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attendingButtonClicked();
            }
        });
    }

    public void chatButtonClicked(){
        Intent i = new Intent(this, ChatActivity.class);
        startActivity(i);
    }

    public void bookmarkButtonClicked(){
        User user = ((SudoMapApplication)getApplication()).getCurrentUser();
        user.addBookmarkedEvent(mEvent);
        ((SudoMapApplication)getApplication()).updateCurrentUser(user);
    }

    public void attendingButtonClicked(){
        User user = ((SudoMapApplication)getApplication()).getCurrentUser();
        user.addAttendingEvent(mEvent);
        ((SudoMapApplication)getApplication()).updateCurrentUser(user);
        mEvent.addAttendant(user);
        //TODO update this event in firebase
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        populateDetails();
    }

    public void populateDetails(){

        //TODO category
        titleView.setText(mEvent.getTitle());
        //TODO getUserFromID seems to be broken
        User organizer = (((SudoMapApplication)getApplication()).getUserFromID(mEvent.getOrganizerID()));
        String organizerName = "";
        if(organizer == null){
            organizerName = "getUserFromID returned null";
        }
        else{
            organizerName = organizer.getInAppName();
        }
        organizerView.setText("By: "+ organizerName);
        locationNameView.setText(
                "Location: "+ nameFromLatLng(mEvent.getLatitude(),mEvent.getLongitude()));
        locationAddress.setText(
                "Address: "+ addressFromLatLng(mEvent.getLatitude(), mEvent.getLongitude()));
        descriptionView.setText(mEvent.getDescription());
        mMap.addMarker(new MarkerOptions().position(new
                LatLng(mEvent.getLatitude(), mEvent.getLongitude())).title("Hello world"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mEvent.getLatitude(), mEvent.getLongitude()),15));

//        for(User user: mEvent.getAttendants()){
//            AttendantsItem item = new AttendantsItem(getApplicationContext());
//            item.setName(user.getInAppName());
//            item.setPicBitMap(user.getProfileImageBitMap());
//            attendantsView.addView(item);
//        }
        for(int i = 0; i < 5; i++){
            AttendantsItem item = new AttendantsItem(getApplicationContext());
            item.setName(Integer.toString(i)+ " -test- " + Integer.toString(i));
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.miller);
            item.setPicBitMap(bitmap);
            attendantsView.addView(item);
        }
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
