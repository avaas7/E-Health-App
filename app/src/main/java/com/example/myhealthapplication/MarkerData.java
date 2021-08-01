package com.example.myhealthapplication;

import com.google.android.gms.maps.model.LatLng;

public class MarkerData {
    private LatLng markerLanLng;
    private String markerTitle;
    private String tel;


    public MarkerData(LatLng markerLanLng, String markerTitle,String tel) {
        this.markerLanLng = markerLanLng;
        this.markerTitle = markerTitle;
        this.tel = tel;

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

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }
}
