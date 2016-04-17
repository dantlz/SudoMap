package com.anchronize.sudomap;

import android.util.Log;

import com.anchronize.sudomap.objects.Event;
import com.anchronize.sudomap.objects.User;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;

/**
 * Created by tianlinz on 4/14/16.
 */
public class SudoMapApplication extends android.app.Application{

    private boolean isAuthenticated;
    private User currentUser;
    private String currentUserID;

    private Firebase ref, refUsers;

    private User tempUser; //this is used for queries, after other activies request on this application
    @Override
    public void onCreate() {
        super.onCreate();
        Firebase.setAndroidContext(this);
         ref =  new Firebase("https://anchronize.firebaseio.com");
         refUsers = ref.child("users");



        isAuthenticated = false;
        currentUserID = null;

    }

    public void setAuthenticateStatus(boolean authStatus) {
        this.isAuthenticated = authStatus;
    }

    public boolean getAuthenticateStatus() {
        return isAuthenticated;
    }

    public User getUserFromID(String UserID){
        Query queryRef = refUsers.orderByChild("userID").equalTo(UserID);

        queryRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot snapshot, String previousChild) {
                Log.d("snapSHOTPARENT:", snapshot.toString());
                tempUser = snapshot.getValue(User.class);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }

        });

        return tempUser;
    }


    public void updateCurrentUser(final User user){
        if(!user.getUserID().equalsIgnoreCase(currentUserID)){

            return;
        }

        Query queryRef = refUsers.orderByChild("userID").equalTo(user.getUserID());
        Log.d("progress", "here");
        queryRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot snapshot, String previousChild) {
                //get the nameOfSubTree
                String subTreename = snapshot.getKey();
                Log.d("subTree", subTreename);
                Firebase refSubTree = refUsers.child(subTreename);
                Log.d("bio", user.getUserBio());
                //update the user on the Firebase
                refSubTree.setValue(user);

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }

        });

    }

    public User getCurrentUser() {
        Query queryRef = refUsers.orderByChild("userID").equalTo(currentUserID);

        queryRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot snapshot, String previousChild) {
                Log.d("snapSHOTPARENT:", snapshot.toString());
                currentUser = snapshot.getValue(User.class);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }

        });


        return currentUser;
    }

    public void setCurrentUserID(String currentID) {
        this.currentUserID = currentID;
    }

    public String getCurrentUserID() {
        return currentUserID;
    }

}
