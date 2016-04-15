package com.anchronize.sudomap;

import com.firebase.client.Firebase;

/**
 * Created by tianlinz on 4/14/16.
 */
public class SudoMapApplication extends android.app.Application{

    private boolean isAuthenticated;

    @Override
    public void onCreate() {
        super.onCreate();
        Firebase.setAndroidContext(this);

        isAuthenticated = false;
    }

    public void setAuthenticateStatus(boolean authStatus) {
        this.isAuthenticated = authStatus;
    }

    public boolean getAuthenticateStatus() {
        return isAuthenticated;
    }
}
