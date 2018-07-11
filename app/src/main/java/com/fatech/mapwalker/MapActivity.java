package com.fatech.mapwalker;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Toast;

import com.directions.route.Route;
import com.directions.route.RouteException;
import com.directions.route.Routing;
import com.directions.route.RoutingListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;

public class MapActivity extends Activity implements OnMapReadyCallback, RoutingListener {

    private GoogleMap googleMap;
    private Bundle argumentsFromMainPage;
    private LatLng address;
    private List<Polyline> polylines;
    private static final int[] COLORS = new int[]{R.color.primary_dark_material_light};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_map);
            polylines = new ArrayList<>();
            MapFragment mapFragment = (MapFragment) getFragmentManager()
                    .findFragmentById(R.id.mapView);
            mapFragment.getMapAsync(this);
        } catch (Exception e) {
            Toast toast = Toast.makeText(this, "Не смогли открыть карты", Toast.LENGTH_LONG);
            toast.show();
        }
    }

    @Override
    public void onMapReady(GoogleMap map) {
        googleMap = map;
        try {
            argumentsFromMainPage = getIntent().getExtras();
            address = (LatLng) argumentsFromMainPage.get("position");
            moveCamera();
        } catch (Exception e) {
            Toast toast = Toast.makeText(this, "Ошибка определения локации" + e.toString(), Toast.LENGTH_LONG);
            toast.show();
        }
    }

    private void moveCamera() {
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(address, 18));
        makePolyline();
    }

    private void makePolyline() {
        LatLng end = new LatLng(55.751244, 37.618423);
        Routing routing = new Routing.Builder()
                .travelMode(Routing.TravelMode.WALKING)
                .withListener(this)
                .waypoints(address,end)
                .build();
        routing.execute();
    }

    @Override
    public void onRoutingSuccess(ArrayList<Route> route, int shortestRouteIndex) {
        if(polylines.size()>0) {
            for (Polyline poly : polylines) {
                poly.remove();
            }
        }

        polylines = new ArrayList<>();
        //add route(s) to the map.
        for (int i = 0; i <route.size(); i++) {
            //In case of more than 5 alternative routes
            int colorIndex = i % COLORS.length;
            PolylineOptions polyOptions = new PolylineOptions();
            polyOptions.color(getResources().getColor(COLORS[colorIndex]));
            polyOptions.width(10 + i * 3);
            polyOptions.addAll(route.get(i).getPoints());
            Polyline polyline = googleMap.addPolyline(polyOptions);
            polylines.add(polyline);
            Toast.makeText(getApplicationContext(),"Путь "+ (i+1) +": расстояние - "+ route.get(i).getDistanceValue()/1000 + " км",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRoutingFailure(RouteException e) {
        if(e != null) {
            Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }else {
            Toast.makeText(this, "Something went wrong, Try again", Toast.LENGTH_SHORT).show();
        }
    }


    //Not used method
    @Override
    public void onRoutingStart() {

    }

    //Not used method
    @Override
    public void onRoutingCancelled() {

    }
}
