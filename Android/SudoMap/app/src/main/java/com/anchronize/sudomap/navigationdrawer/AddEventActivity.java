package com.anchronize.sudomap.navigationdrawer;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.anchronize.sudomap.R;
import com.anchronize.sudomap.SudoMapApplication;
import com.anchronize.sudomap.objects.Event;
import com.anchronize.sudomap.objects.User;
import com.firebase.client.Firebase;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AddEventActivity extends AppCompatActivity{

    private EditText titleEditText;
    private EditText descriptionEditText;
    private Button createEventButton;
    private Spinner categorySpinner;
    private CheckBox privacyCheckBox;

    private TextView locationTV, startDateTV, startTimeTV, endDateTV, endTimeTV;

    private ImageView timeIconIV, descriptionIconIV, locationIconIV, privacyIconIV, filterIconIV;

    int startMonth, startDay, startYear, endMonth, endDay, endYear;
    int startHour, startMinute, endHour, endMinute;
    String startAM_PM = "", endAM_PM = "";

    // Maintain a connection to Firebase
    private Firebase refEvent;
    private Firebase ref;

    private Activity activity = this;
    private double latitude = 0;
    private double longitude = 0;
    private String address = "";

    int PLACE_PICKER_REQUEST = 2;   //request code for google place picker

    int DIALOG_ID = 0;

    final Calendar cal = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);
        titleEditText = (EditText)findViewById(R.id.titleEditText);
        descriptionEditText = (EditText)findViewById(R.id.descriptionEditText);
        createEventButton = (Button)findViewById(R.id.createButton);
        categorySpinner = (Spinner)findViewById(R.id.categorySpinner);
        privacyCheckBox = (CheckBox)findViewById(R.id.privacyCheckBox);

        locationTV = (TextView) findViewById(R.id.locationTextView);
        startDateTV = (TextView) findViewById(R.id.startDateTextView);
        startTimeTV = (TextView) findViewById(R.id.startTimeTextView);
        endDateTV = (TextView) findViewById(R.id.endDateTextView);
        endTimeTV = (TextView) findViewById(R.id.endTimeTextView);

        startMonth = cal.get(Calendar.MONTH);
        startDay = cal.get(Calendar.DAY_OF_MONTH);
        startYear = cal.get(Calendar.YEAR);
        endMonth = cal.get(Calendar.MONTH);
        endDay = cal.get(Calendar.DAY_OF_MONTH);
        endYear = cal.get(Calendar.YEAR);

        startHour = cal.get(Calendar.HOUR);
        startMinute = cal.get(Calendar.MINUTE);
        endHour = cal.get(Calendar.HOUR);
        endMinute = cal.get(Calendar.MINUTE);

//        Firebase.setAndroidContext(this);
        ref = new Firebase("https://anchronize.firebaseio.com");
        refEvent = ref.child("events");

        createEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = titleEditText.getText().toString();
                String description = descriptionEditText.getText().toString();
                boolean isPrivate = privacyCheckBox.isChecked();
                String category = categorySpinner.getSelectedItem().toString();


                //TODO change the hard-code user to the global user
                User tianlinz = new User("123456");
                tianlinz.setPremium(true);
                tianlinz.setInAppName("tianlinz");
                tianlinz.setUserBio("fuck you bio!");

                //create the event object
                Event event = new Event();
                User currentUser = ((SudoMapApplication)getApplication()).getCurrentUser();
                event.setOrganizerID(currentUser.getUserID());
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
//                event.setEventID(id);
                setResult(RESULT_OK, null);
                finish();

            }
        });

        locationTV.setOnClickListener(new View.OnClickListener() {
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

        startDateTV.setOnClickListener(new DialogListener(startDateTV.getId()));
        endDateTV.setOnClickListener(new DialogListener(endDateTV.getId()));
        startTimeTV.setOnClickListener(new DialogListener(startTimeTV.getId()));
        endTimeTV.setOnClickListener(new DialogListener(endTimeTV.getId()));

        customizeIconColors();
    }

    @Override
    protected Dialog onCreateDialog(int id){
        if(id == R.id.startDateTextView){
            return new DatePickerDialog(this, dPickerListener, startYear, startMonth, startDay);
        }
        if(id == R.id.endDateTextView){
            return new DatePickerDialog(this, dPickerListener, endYear, endMonth, endDay);
        }
        if(id == R.id.startTimeTextView){
            return new TimePickerDialog(this, tPickerListener, startHour, startMinute, false);
        }
        if(id == R.id.endTimeTextView){
            return new TimePickerDialog(this, tPickerListener, endHour, endMinute, false);
        }
        return null;
    }

    private TimePickerDialog.OnTimeSetListener tPickerListener
            = new TimePickerDialog.OnTimeSetListener(){
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            if(DIALOG_ID == R.id.startTimeTextView){

                Calendar time = Calendar.getInstance();
                time.set(Calendar.HOUR_OF_DAY, hourOfDay);
                time.set(Calendar.MINUTE, minute);

                if(time.get(Calendar.AM_PM) == Calendar.AM)
                    startAM_PM = "AM";
                else if(time.get(Calendar.AM_PM) == Calendar.PM)
                    startAM_PM = "PM";

                String hour = (time.get(Calendar.HOUR) == 0) ?"12":time.get(Calendar.HOUR)+"";
                startHour = Integer.parseInt(hour);

                int selectedMinute = time.get(Calendar.MINUTE);
                startMinute = selectedMinute;
                String sMinute = "00";
                if(selectedMinute < 10){
                    sMinute = "0" + selectedMinute;
                } else {
                    sMinute = selectedMinute + "";
                }

                startTimeTV.setText(hour + ":" + sMinute + " " + startAM_PM);
            }
            if(DIALOG_ID == R.id.endTimeTextView){
                Calendar time = Calendar.getInstance();
                time.set(Calendar.HOUR_OF_DAY, hourOfDay);
                time.set(Calendar.MINUTE, minute);

                if(time.get(Calendar.AM_PM) == Calendar.AM)
                    endAM_PM = "AM";
                else if(time.get(Calendar.AM_PM) == Calendar.PM)
                    endAM_PM = "PM";

                String hour = (time.get(Calendar.HOUR) == 0) ?"12":time.get(Calendar.HOUR)+"";
                endHour = Integer.parseInt(hour);

                int selectedMinute = time.get(Calendar.MINUTE);
                endMinute = selectedMinute;
                String sMinute = "00";
                if(selectedMinute < 10){
                    sMinute = "0" + selectedMinute;
                } else {
                    sMinute = selectedMinute + "";
                }

                endTimeTV.setText(hour + ":" + sMinute + " " + endAM_PM);
            }
        }
    };

    private DatePickerDialog.OnDateSetListener dPickerListener
            = new DatePickerDialog.OnDateSetListener(){

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            if(DIALOG_ID == R.id.startDateTextView){
                startYear = year;
                startMonth = monthOfYear;
                startDay = dayOfMonth;

                cal.set(Calendar.MONTH, startMonth);
                String month = cal.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.US);
                String date = month + " " + startDay + ", " + startYear;
                startDateTV.setText(date);
            }
            if(DIALOG_ID == R.id.endDateTextView){
                endYear = year;
                endMonth = monthOfYear;
                endDay = dayOfMonth;

                cal.set(Calendar.MONTH, endMonth);
                String month = cal.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.US);
                String date = month + " " + endDay + ", " + endYear;
                endDateTV.setText(date);
            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(getApplicationContext(), data);
                longitude = place.getLatLng().longitude;
                latitude = place.getLatLng().latitude;
                address = String.valueOf(place.getAddress());
                locationTV.setText(place.getName().toString());
            }
        }
    }

    private class DialogListener implements View.OnClickListener{

        private int viewID;

        public DialogListener(int viewID){
            this.viewID = viewID;
        }

        @Override
        public void onClick(View v) {

            if((viewID == R.id.startDateTextView) || (viewID == R.id.endDateTextView)){
                DIALOG_ID = viewID;
                showDialog(DIALOG_ID);
            }

            if((viewID == R.id.startTimeTextView) || (viewID == R.id.endTimeTextView)){
                DIALOG_ID = viewID;
                showDialog(DIALOG_ID);
            }
        }

    }

    private void customizeIconColors(){
        timeIconIV = (ImageView) findViewById(R.id.timeIconIV);
        descriptionIconIV = (ImageView) findViewById(R.id.descriptionIconIV);
        locationIconIV = (ImageView) findViewById(R.id.locationIconIV);
        privacyIconIV = (ImageView) findViewById(R.id.privacyIconIV);
        filterIconIV = (ImageView) findViewById(R.id.filterIconIV);

        int iconColor = Color.parseColor("#454545");
        timeIconIV.setColorFilter(iconColor);
        descriptionIconIV.setColorFilter(iconColor);
        locationIconIV.setColorFilter(iconColor);
        privacyIconIV.setColorFilter(iconColor);
        filterIconIV.setColorFilter(iconColor);
    }
}
