package com.fatech.mapwalker;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;

public class ManeActivityPage extends Activity {

    private Button toPage2Button;
    private Button geolocationButton;
    private Intent intentMapPage;
    private int distance;
    int condCode = 0;
    private LatLng startPoint;
    private LocationManager locationManager;
    private Location locationOnMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mane_page);
        setLocationSetting();
    }

    private void setLocationSetting() {
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Permission failed", Toast.LENGTH_SHORT).show();
            return;
        } else {
            try {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                        1000 * 10, 10, locationListener);
                locationManager.requestLocationUpdates(
                        LocationManager.NETWORK_PROVIDER, 1000 * 10, 10,
                        locationListener);
                setButtonLisners();
            } catch (Exception e) {
                Toast.makeText(this, "Error" + e.toString(), Toast.LENGTH_LONG).show();
            }
        }
    }


    private void setButtonLisners() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Permission failed", Toast.LENGTH_SHORT).show();return;
        } else {
            geolocationButton = (Button) findViewById(R.id.geolocationButton);
            geolocationButton.setOnClickListener(new View.OnClickListener() {
                @SuppressLint("MissingPermission")
                public void onClick(View v) {
                    getLocation(locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER));
                }
            });
        }
        toPage2Button = (Button) findViewById(R.id.toPage2Button);
        toPage2Button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                valueFrominput();
            }
        });
    }

    private void getLocation(Location location) {
        condCode += 1;
        try {
            locationOnMap = location;
            startPoint = new LatLng(locationOnMap.getLatitude(), locationOnMap.getLongitude());
            TextView geolocationTextView = findViewById(R.id.geolocationTextView);
            geolocationTextView.setText(startPoint.toString());
            valueFrominput();
        } catch (Exception e) {
            Toast.makeText(this, "Что-то не так со startPoint " + locationOnMap.toString(), Toast.LENGTH_LONG).show();
        }
    }


    private void valueFrominput() {
        try {
            EditText distanceEditText = (EditText) findViewById(R.id.distance);
            distance = Integer.parseInt(distanceEditText.getText().toString());
            toPage2();
        } catch (Exception e) {
            Toast.makeText(this, "Не заполнены поля", Toast.LENGTH_LONG).show();
        }
    }

    private void toPage2() {
        try {
            intentMapPage = new Intent(this, MapActivity.class);
            intentMapPage.putExtra("start", startPoint);
            intentMapPage.putExtra("distance", distance);
            startActivity(intentMapPage);
        } catch (Exception e) {
            Toast toast = Toast.makeText(this, "Не получилось открыть карты", Toast.LENGTH_LONG);
            toast.show();
        }
    }

    private LocationListener locationListener = new LocationListener() {

        @Override
        public void onLocationChanged(Location location) {
            getLocation(location);
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


}


//    private void searchPosition(String location) {
//        try {
//            Geocoder geocoder = new Geocoder(this);
//            Address address = geocoder.getFromLocationName(location, 1).get(0);
//            positionOnMap = new LatLng(address.getLatitude(), address.getLongitude());
//            toPage2();
//        } catch (IOException e) {
//            Toast toast = Toast.makeText(this, "Не нашли место на карте", Toast.LENGTH_LONG);
//            toast.show();
//        }
//    }