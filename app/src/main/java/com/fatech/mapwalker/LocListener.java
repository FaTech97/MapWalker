package com.fatech.mapwalker;

import android.location.Location;
import android.os.Bundle;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class LocListener implements android.location.LocationListener {
    private LatLng imHere;

    public LocListener(LatLng latLng) {
        imHere = latLng;
    }

    public LatLng getImHere() {
        return imHere;
    }

    @Override
    public void onLocationChanged(Location location) {
        imHere = new LatLng(location.getLatitude(),location.getLongitude());
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
