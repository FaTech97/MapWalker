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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mane_page);
        Button toPage2Button = (Button) findViewById(R.id.toPage2Button);
        toPage2Button.setOnClickListener(new View.OnClickListener() {
             public void onClick(View v) {
                toPage2();
                }
        });
    }

    private void toPage2() {
        Intent intentMapPage = new Intent(this,MapActivity.class);
            try {
                EditText positionEditText = (EditText) findViewById(R.id.position);
                EditText distanceEditText = (EditText) findViewById(R.id.distance);
                int distance = Integer.parseInt(distanceEditText.getText().toString());
                String position = positionEditText.getText().toString();
                LatLng adress = searchResultsPosition(position);
            intentMapPage.putExtra("position", adress);
            intentMapPage.putExtra("distance", distance);
            }
            catch(Exception e){
                Toast toast = Toast.makeText(this, "Error 1 \n" + e,Toast.LENGTH_LONG);
                toast.show();
            }
        startActivity(intentMapPage);
    }

    private LatLng searchResultsPosition(String location){
        Geocoder geocoder = new Geocoder(this);
        try {
            Address address = geocoder.getFromLocationName(location, 1).get(0);
            LatLng positionOnMap = new LatLng(address.getLatitude(), address.getLongitude());
            return positionOnMap;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
