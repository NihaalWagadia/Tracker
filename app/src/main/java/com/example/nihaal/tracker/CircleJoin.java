package com.example.nihaal.tracker;

import android.net.Uri;

public class CircleJoin {

    public String circlememberid, joined_name, lat, lng;

    public CircleJoin(String circlememberid, String joined_name, String lat, String lng){

        this.circlememberid = circlememberid;
        this.joined_name = joined_name;

        this.lat = lat;
        this.lng = lng;

    }
    public CircleJoin(){}
}
