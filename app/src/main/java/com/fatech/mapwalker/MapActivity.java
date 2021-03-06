package com.fatech.mapwalker;

import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.directions.route.Route;
import com.directions.route.RouteException;
import com.directions.route.Routing;
import com.directions.route.RoutingListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapActivity extends FragmentActivity implements OnMapReadyCallback, RoutingListener {

    private GoogleMap googleMap;
    private Bundle argumentsFromMainPage;
    private Marker imHereMarker;
    private LocationManager locationManager;
    private LatLng startPoint;
    private LatLng endPoint;
    private List<Polyline> polylines;
    private int distance;
    private float distanceToFinish[];
    private int KKal;
    private Circle finishArea;

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
            startPoint = (LatLng) argumentsFromMainPage.get("start");
            googleMap.addMarker(new MarkerOptions()
                    .position(startPoint)
                    .title("Start")
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.start_marker_icon_50)));
            setLocationManager();
            // Customise the styling of the base map using a JSON object defined
            // in a raw resource file.
            boolean success = googleMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            this, R.raw.mapstyle));
            if (!success) {
                Log.e("MapsActivityRaw", "Style parsing failed.");
            }
        } catch (Exception e) {
            Toast toast = Toast.makeText(this, "Ошибка определения локации" + e.toString(), Toast.LENGTH_LONG);
            toast.show();
        }
    }

    private void setLocationManager() {
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Permission failed", Toast.LENGTH_SHORT).show();
            return;
        } else {
            try {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                        1000 * 5, 50, locationListener);
                locationManager.requestLocationUpdates(
                        LocationManager.NETWORK_PROVIDER, 1000 * 5, 10,
                        locationListener);
                firstSetHereMarker();
            } catch (Exception e) {
                Toast.makeText(this, "Error" + e.toString(), Toast.LENGTH_LONG).show();
            }
        }
    }

    private void firstSetHereMarker() {
        imHereMarker = googleMap.addMarker(new MarkerOptions().position(startPoint));
        moveCamera();
    }

    private void moveCamera() {
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(startPoint, 18));
        calculateEndPoint();
    }

    private void calculateEndPoint() {
        try {
            distance = (int) argumentsFromMainPage.get("distance");
            float z = (float) (distance * 0.009009); // Because 1 latitube = 1 longitube = 111 km => 1 km = 0,009009 lat.
            double x = (double) (Math.random() * z);
            double y = (double) Math.sqrt(Math.pow(z, 2) - Math.pow(x, 2));
            double endLatitube = (float) (Math.random() > 0.5 ? startPoint.latitude + x : startPoint.latitude - x);
            double endLongitube = (float) (Math.random() > 0.5 ? startPoint.longitude + y : startPoint.longitude - y);
            endPoint = new LatLng(endLatitube, endLongitube);
            googleMap.addMarker(new MarkerOptions()
                    .position(endPoint)
                    .title("Finish")
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.finish_marker_icon_50)));
            addFinishAria();
        } catch (Exception e) {
            Toast.makeText(this, "Ошибка нахождения конечной точки\n" + e.toString(), Toast.LENGTH_LONG).show();
        }
    }

    private void addFinishAria() {
        CircleOptions circleOptions = new CircleOptions();
        circleOptions.center(endPoint);
        circleOptions.radius(50 * distance);
//        circleOptions.strokeColor(Color.WHITE);
//        circleOptions.fillColor(Color.WHITE);
        finishArea = googleMap.addCircle(circleOptions);
        makePolyline();
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
        //Toast.makeText(getApplicationContext(), "Расстояние - " + route.get(0).getDistanceValue() / 1000 + " км", Toast.LENGTH_SHORT).show();
        // setFinishDialog(route.get(0).getDistanceValue()/1000);
    }

    private void changePositionOnMap(Location location) {
        try {
            imHereMarker.remove();
            imHereMarker = googleMap.addMarker(new MarkerOptions().position(new LatLng(location.getLatitude(), location.getLongitude())));
            Location.distanceBetween(imHereMarker.getPosition().latitude,
                    imHereMarker.getPosition().longitude, finishArea.getCenter().latitude,
                    finishArea.getCenter().longitude, distanceToFinish);
            if (distanceToFinish[0] < finishArea.getRadius()) {
                showFinishDialog();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Не удалось добавить ваше положение на карту", Toast.LENGTH_LONG);
        }
    }

    private void showFinishDialog() {
        try {
            FinishDialog dialog = new FinishDialog();
            Bundle args = new Bundle();
            args.putInt("KKal", KKal);
            dialog.show(getSupportFragmentManager(), "custom");
        } catch (Exception e) {
            Toast.makeText(this, "Не удалось вывести окно", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onRoutingFailure(RouteException e) {
        if (e != null) {
            Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "Something went wrong, Try again", Toast.LENGTH_SHORT).show();
        }
    }

    private int count = 0;
    private LocationListener locationListener = new LocationListener() {

        @Override
        public void onLocationChanged(Location location) {
            count++;
            changePositionOnMap(location);
        }

        @Override
        public void onProviderDisabled(String provider) {
        }

        @Override
        public void onProviderEnabled(String provider) {
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }
    };

    //Not used method
    @Override
    public void onRoutingStart() {

    }

    //Not used method
    @Override
    public void onRoutingCancelled() {

    }
}
