package com.anchronize.sudomap.objects;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.net.Uri;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.anchronize.sudomap.R;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by tianlinz on 4/2/16.
 */
public class User implements Serializable {

    public static final long serialVersionUID = 2L;

    public User(){}

    public User(String ID){
        userID = ID;
    }

    public String getUserID() {
        return userID;
    }

    public boolean isPremium() {
        return premium;
    }

    public void setPremium(boolean premium) {
        this.premium = premium;
    }

    public String getInAppName() {
        return inAppName;
    }

    public void setInAppName(String inAppName) {
        this.inAppName = inAppName;
    }

    public Bitmap getProfileImageBitMap(){
        byte [] encodeByte =Base64.decode(profileImgString,Base64.DEFAULT);
        Bitmap bitmap=BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
        return bitmap;
    }

    public String getUserBio() {
        return userBio;
    }

    public void setUserBio(String userBio) {
        this.userBio = userBio;
    }

//    public UserPreference getUserPreferences() {
//        return userPreferences;
//    }
//
//    public void setUserPreferences(UserPreference userPreferences) {
//        this.userPreferences = userPreferences;
//    }
//
    public ArrayList<Event> getAttendingEvents() {
        return attendingEvents;
    }

    public void addAttendingEvent(Event attendingEvent){
        this.attendingEvents.add(attendingEvent);
    }

    public void removeAttendingEvent(String eventID){
        for(Event event: attendingEvents){
            if(event.getEventID().equals(eventID)){
                attendingEvents.remove(event);
            }
        }
    }

    public ArrayList<Event> getBookmarkedEvents() {
        return bookmarkedEvents;
    }

    public void addBookmarkedEvent(Event bookmarkedEvent) {
        this.bookmarkedEvents.add(bookmarkedEvent);
    }

    public void removeBookmarkedEvent(String eventID){
        for(Event event: bookmarkedEvents){
            if(event.getEventID().equals(eventID)){
                bookmarkedEvents.remove(event);
            }
        }
    }

    private boolean premium;
    private String userID;
    private String inAppName;
    //TODO We don't know if we can store profile image.
    private String profileImgString;
    private String userBio;
//    private UserPreference userPreferences;
    private ArrayList<Event> attendingEvents = new ArrayList<Event>();
    private ArrayList<Event> bookmarkedEvents = new ArrayList<Event>();
}

