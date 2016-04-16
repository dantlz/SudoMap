package com.anchronize.sudomap.navigationdrawer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;

import com.anchronize.sudomap.R;
import com.anchronize.sudomap.SudoMapApplication;
import com.anchronize.sudomap.objects.Event;
import com.anchronize.sudomap.objects.User;
import com.firebase.client.Firebase;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;

import java.util.HashMap;
import java.util.Map;

public class AddEventActivity extends AppCompatActivity {


    private EditText titleEditText;
    private EditText descriptionEditText;
    private Button createEventButton, locationButton;
    private Spinner categorySpinner;
    private CheckBox privacyCheckbox;

    // Maintain a connection to Firebase
    private Firebase refEvent;
    private Firebase ref;

    private Activity activity = this;
    private double latitude = 0;
    private double longitude = 0;
    private String address = "";

    int PLACE_PICKER_REQUEST = 2;   //request code for google place picker

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);
        titleEditText = (EditText)findViewById(R.id.titleEditText);
        descriptionEditText = (EditText)findViewById(R.id.descriptionEditText);
        createEventButton = (Button)findViewById(R.id.createButton);
        locationButton = (Button)findViewById(R.id.locationButton);
        categorySpinner = (Spinner)findViewById(R.id.categorySpinner);
        privacyCheckbox =(CheckBox)findViewById(R.id.privateCheckbox);


//        Firebase.setAndroidContext(this);
        ref = new Firebase("https://anchronize.firebaseio.com");
        refEvent = ref.child("events");


        createEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = titleEditText.getText().toString();
                String description = descriptionEditText.getText().toString();
                boolean isPrivate = privacyCheckbox.isChecked();
                String category = categorySpinner.getSelectedItem().toString();



                //create the event object
                Event event = new Event();
                String currentID = ((SudoMapApplication) getApplication()).getCurrentUserID();
                event.setOrganizerID(currentID);
                event.setTitle(title);
                event.setDescription(description);
                event.setPrivacy(isPrivate);
                event.setCategory(category);
                event.setVisible(true); //default the visibility to true
                event.setLatitude(latitude);
                event.setLongitude(longitude);
                event.setAddress(address);

                Firebase temp = refEvent.push();
                String id = temp.getKey();
                Log.d("id", id);
                temp.setValue(event);

                Map<String, Object> eventID = new HashMap<String, Object>();
                eventID.put("eventID", id);
                temp.updateChildren(eventID);

                setResult(RESULT_OK, null);
                finish();

            }
        });

        locationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                try {
                    startActivityForResult(builder.build(activity), PLACE_PICKER_REQUEST);
                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(getApplicationContext(), data);
                locationButton.setText(place.getAddress());
                longitude = place.getLatLng().longitude;
                latitude = place.getLatLng().latitude;
                address = String.valueOf(place.getAddress());
            }
        }
    }
}
