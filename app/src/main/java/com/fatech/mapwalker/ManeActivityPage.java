package com.fatech.mapwalker;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;

public class ManeActivityPage extends AppCompatActivity {

    private Button toPage2Button;
    private Intent intentMapPage;
    private int distance;
    private LatLng positionOnMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mane_page);
        toPage2Button = (Button) findViewById(R.id.toPage2Button);
        toPage2Button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                valueFrominputs();
            }
        });
    }

    private void valueFrominputs() {
        try {
            EditText positionEditText = (EditText) findViewById(R.id.position);
            EditText distanceEditText = (EditText) findViewById(R.id.distance);
            distance = Integer.parseInt(distanceEditText.getText().toString());
            String location = positionEditText.getText().toString();
            searchPosition(location);
        } catch (Exception e) {
            Toast toast = Toast.makeText(this, "Не заполнены поля", Toast.LENGTH_LONG);
            toast.show();
        }
    }

    private void searchPosition(String location) {
        try {
            Geocoder geocoder = new Geocoder(this);
            Address address = geocoder.getFromLocationName(location, 1).get(0);
            positionOnMap = new LatLng(address.getLatitude(), address.getLongitude());
            toPage2();
        } catch (IOException e) {
            Toast toast = Toast.makeText(this, "Не нашли место на карте", Toast.LENGTH_LONG);
            toast.show();
        }
    }

    private void toPage2() {
        try {
            intentMapPage = new Intent(this, MapActivity.class);
            intentMapPage.putExtra("position", positionOnMap);
            intentMapPage.putExtra("distance", distance);
            startActivity(intentMapPage);
        } catch (Exception e) {
            Toast toast = Toast.makeText(this, "Не получилось открыть карты", Toast.LENGTH_LONG);
            toast.show();
        }
    }

}
