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
import java.util.Random;

public class MapActivity extends Activity implements OnMapReadyCallback, RoutingListener {

    private GoogleMap googleMap;
    private Bundle argumentsFromMainPage;
    private LatLng startPoint;
    private LatLng endPoint;
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
            startPoint = (LatLng) argumentsFromMainPage.get("position");
            moveCamera();
        } catch (Exception e) {
            Toast toast = Toast.makeText(this, "Ошибка определения локации" + e.toString(), Toast.LENGTH_LONG);
            toast.show();
        }
    }

    private void moveCamera() {
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(startPoint, 18));
        calculateEndPoint();
    }

    private void calculateEndPoint() {
        try {
            int distance = (int) argumentsFromMainPage.get("distance");
            float z = (float) (distance * 0.009009); // Because 1 latitube = 1 longitube = 111 km => 1 km = 0,009009 lat.
            float x = (float) (Math.random() * z);
            float y = (float) Math.sqrt(Math.pow(z, 2) - Math.pow(x, 2));
            float endLatitube = (float) (Math.random() > 0.5 ? startPoint.latitude + x : startPoint.latitude - x);
            float endLongitube = (float) (Math.random() > 0.5 ? startPoint.longitude + y : startPoint.longitude - y);
            endPoint = new LatLng(endLatitube, endLongitube);
            makePolyline();
        } catch (Exception e) {
            Toast.makeText(this, "Ошибка нахождения конечной точки\n" + e.toString(), Toast.LENGTH_LONG).show();
        }
    }

    private void makePolyline() {
        Routing routing = new Routing.Builder()
                .travelMode(Routing.TravelMode.WALKING)
                .withListener(this)
                .waypoints(startPoint, endPoint)
                .build();
        routing.execute();
    }

    @Override
    public void onRoutingSuccess(ArrayList<Route> route, int shortestRouteIndex) {
        if (polylines.size() > 0) {
            for (Polyline poly : polylines) {
                poly.remove();
            }
        }

        polylines = new ArrayList<>();
        PolylineOptions polyOptions = new PolylineOptions();
        polyOptions.color(R.color.primary_dark_material_light);
        polyOptions.width(10);
        polyOptions.addAll(route.get(0).getPoints());
        Polyline polyline = googleMap.addPolyline(polyOptions);
        polylines.add(polyline);
        Toast.makeText(getApplicationContext(), "Расстояние - " + route.get(0).getDistanceValue()/1000 + " км", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRoutingFailure(RouteException e) {
        if (e != null) {
            Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        } else {
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
