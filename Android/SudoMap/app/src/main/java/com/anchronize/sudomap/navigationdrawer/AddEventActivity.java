package com.anchronize.sudomap.navigationdrawer;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;

import com.anchronize.sudomap.NavigationDrawer;
import com.anchronize.sudomap.R;
import com.anchronize.sudomap.objects.Event;
import com.anchronize.sudomap.objects.User;
import com.firebase.client.Firebase;

public class AddEventActivity extends NavigationDrawer {


    private EditText titleEditText;
    private EditText descriptionEditText;
    private Button createEventButton;
    private Spinner categorySpinner;
    private CheckBox privacyCheckbox;

    // Maintain a connection to Firebase
    private Firebase refEvent;
    private Firebase ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);
        titleEditText = (EditText)findViewById(R.id.titleEditText);
        descriptionEditText = (EditText)findViewById(R.id.descriptionEditText);
        createEventButton = (Button)findViewById(R.id.createButton);
        categorySpinner = (Spinner)findViewById(R.id.categorySpinner);
        privacyCheckbox =(CheckBox)findViewById(R.id.privateCheckbox);


        Firebase.setAndroidContext(this);
        ref = new Firebase("https://anchronize.firebaseio.com");
        refEvent = ref.child("events");


        createEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = titleEditText.getText().toString();
                String description = descriptionEditText.getText().toString();
                boolean isPrivate = privacyCheckbox.isChecked();
                String category = categorySpinner.getSelectedItem().toString();


                //TODO change the hard-code user to the global user
                User tianlinz = new User("123456");
                tianlinz.setPremium(true);
                tianlinz.setInAppName("tianlinz");
                tianlinz.setUserBio("fuck you bio!");

                //create the event object
                Event event = new Event();
                event.setOrganizer(tianlinz);
                event.setTitle(title);
                event.setDescription(description);
                event.setPrivacy(isPrivate);
                event.setCategory(category);
                event.setVisible(true); //default the visibility to true

                Firebase temp = refEvent.push();
                temp.setValue(event);

            }
        });



    }
}
