package com.anchronize.sudomap.objects;


import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

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

//    public ImageView getProfileImg() {
//        Bitmap bmp =  BitmapFactory.decodeResource(getResources(),
//                R.drawable.chicken);//your image
//        ByteArrayOutputStream bYtE = new ByteArrayOutputStream();
//        bmp.compress(Bitmap.CompressFormat.PNG, 100, bYtE);
//        bmp.recycle();
//        byte[] byteArray = bYtE.toByteArray();
//        String imageFile = Base64.encodeToString(byteArray, Base64.DEFAULT);
//
//        return profileImg;
//    }
//
//    public Bitmap convertToBitmap(Drawable drawable, int widthPixels, int heightPixels) {
//        Bitmap mutableBitmap = Bitmap.createBitmap(widthPixels, heightPixels, Bitmap.Config.ARGB_8888);
//        Canvas canvas = new Canvas(mutableBitmap);
//        drawable.setBounds(0, 0, widthPixels, heightPixels);
//        drawable.draw(canvas);
//
//        return mutableBitmap;
//    }

//    public void setProfileImg(ImageView profileImg) {
//        this.profileImg = profileImg;
//    }

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
//    private ImageView profileImg;
    private String userBio;
//    private UserPreference userPreferences;
    private ArrayList<Event> attendingEvents = new ArrayList<Event>();
    private ArrayList<Event> bookmarkedEvents = new ArrayList<Event>();
}

