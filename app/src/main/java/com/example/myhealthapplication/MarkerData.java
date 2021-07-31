package com.example.myhealthapplication;

import com.google.android.gms.maps.model.LatLng;

public class MarkerData {
    private LatLng markerLanLng;
    private String markerTitle;

    public MarkerData(LatLng markerLanLng, String markerTitle) {
        this.markerLanLng = markerLanLng;
        this.markerTitle = markerTitle;
    }

    public LatLng getMarkerLanLng() {
        return markerLanLng;
    }

    public void setMarkerLanLng(LatLng markerLanLng) {
        this.markerLanLng = markerLanLng;
    }

    public String getMarkerTitle() {
        return markerTitle;
    }

    public void setMarkerTitle(String markerTitle) {
        this.markerTitle = markerTitle;
    }
}
