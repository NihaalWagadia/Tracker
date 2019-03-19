package com.example.nihaal.tracker;

import android.net.Uri;

public class CircleJoin {

    public String circlememberid, joined_name,joined_imageUrl, lat, lng;

    public CircleJoin(String circlememberid, String joined_name, String joined_imageUrl, String lat, String lng){

        this.circlememberid = circlememberid;
        this.joined_name = joined_name;
        this.joined_imageUrl = joined_imageUrl;
        this.lat = lat;
        this.lng = lng;

    }
    public CircleJoin(){}
}
