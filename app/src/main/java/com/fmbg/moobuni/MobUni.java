package com.fmbg.moobuni;

import android.app.Application;
import android.content.Context;

public class MobUni extends Application {

    private static MobUni instance;
    // Called when the application is starting, before any other application objects have been created.
    // Overriding this method is totally optional!
    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        // Required initialization logic here!

    }
    public static synchronized MobUni getInstance(){
        return instance;
    }
    public static Context getContext(){
        return getInstance().getBaseContext();
    }

}

