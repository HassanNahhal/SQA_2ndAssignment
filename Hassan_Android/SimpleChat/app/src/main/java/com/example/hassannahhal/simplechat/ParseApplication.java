package com.example.hassannahhal.simplechat;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseObject;

/**
 * Created by hassannahhal on 2015-11-19.
 */
public class ParseApplication extends Application {

    public static final String YOUR_APPLICATION_ID = "WUXa2xvzApxSNu65oisCysyqWHahMjIVJ1o13TXq";
    public static final String YOUR_CLIENT_KEY = "kOmHrZyQkVfFULimAt3NNCUc431ySZGofYVAMyQa";


    @Override
    public void onCreate() {
        super.onCreate();

        ParseObject.registerSubclass(Message.class);
        Parse.enableLocalDatastore(this);
        Parse.initialize(this, YOUR_APPLICATION_ID, YOUR_CLIENT_KEY);
    }
}
