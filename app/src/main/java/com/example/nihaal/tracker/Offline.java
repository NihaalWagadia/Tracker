package com.example.nihaal.tracker;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;

public class Offline extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        //Will load all string type of variables from dataBase i.e Name, code, etc.
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}
