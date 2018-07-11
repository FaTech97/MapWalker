package com.fatech.mapwalker;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

public class MapActivity extends Activity implements OnMapReadyCallback{

    public static GoogleMap googleMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_map);
            MapFragment mapFragment = (MapFragment) getFragmentManager()
                    .findFragmentById(R.id.mapView);
            mapFragment.getMapAsync(this);
        }
        catch (Exception e){
            Toast toast = Toast.makeText(this, "Error 2 \n "+ e.toString(),Toast.LENGTH_LONG);
            toast.show();
        }
    }

    @Override
    public void onMapReady(GoogleMap map) {
        googleMap = map;
        try {
            Bundle argumentsFromMainPage = getIntent().getExtras();
            LatLng address = (LatLng) argumentsFromMainPage.get("position");
//            googleMap.addMarker(new MarkerOptions().position(address));
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(address, 18));
            PolylineOptions rectOptions = new PolylineOptions()
                    .add(address)
                    .add(new LatLng(55.751244, 37.618423)); // Closes the polyline.
            // Set the rectangle's color to red
            rectOptions.color(Color.RED);

            // Get back the mutable Polyline
            Polyline polyline = googleMap.addPolyline(rectOptions);
        }
        catch (Exception e){
            Toast toast = Toast.makeText(this, "Error 2 \n "+ e.toString(),Toast.LENGTH_LONG);
            toast.show();
        }
    }

}