package com.example.nihaal.tracker;

import com.google.android.gms.maps.model.LatLng;

public class MarkerName {
    LatLng currLatlng;
    String name;

    public MarkerName(String name, LatLng latLng){
        this.name = name;
        this.currLatlng = latLng;
    }

    public LatLng getCurrLatlng() {
        return currLatlng;
    }

    public void setCurrLatlng(LatLng currLatlng) {
        this.currLatlng = currLatlng;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
