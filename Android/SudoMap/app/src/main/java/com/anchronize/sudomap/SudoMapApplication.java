package com.anchronize.sudomap;

import com.anchronize.sudomap.objects.User;
import com.firebase.client.Firebase;

/**
 * Created by tianlinz on 4/14/16.
 */
public class SudoMapApplication extends android.app.Application{

    private boolean isAuthenticated;
    private User currentUser;
    private String currentUserID;

    @Override
    public void onCreate() {
        super.onCreate();
        Firebase.setAndroidContext(this);

        isAuthenticated = false;
        currentUserID = null;
    }

    public void setAuthenticateStatus(boolean authStatus) {
        this.isAuthenticated = authStatus;
    }

    public boolean getAuthenticateStatus() {
        return isAuthenticated;
    }

    public void setCurrentUser(User current) {
        this.currentUser = current;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUserID(String currentID) {
        this.currentUserID = currentID;
    }

    public String getCurrentUserID() {
        return currentUserID;
    }

}
