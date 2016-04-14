package com.anchronize.sudomap;

import com.firebase.client.Firebase;

/**
 * Created by tianlinz on 4/14/16.
 */
public class ChatApplication extends android.app.Application{
    @Override
    public void onCreate() {
        super.onCreate();
        Firebase.setAndroidContext(this);


    }
}
