package com.siriusgps.siriusgpstracker;

import android.location.LocationProvider;
import android.provider.Settings;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.Location;
import android.content.Intent;
import 	android.content.Context;
import android.widget.Toast;
import android.location.Criteria;
import android.widget.TextView;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import static android.location.Criteria.ACCURACY_FINE;


public class MyActivity extends ActionBarActivity
    implements LocationListener{

    private TextView latitudeField;
    private TextView longitudeField;
    private LocationManager service;
    private String provider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);

        latitudeField = (TextView) findViewById(R.id.TextView02);
        longitudeField = (TextView) findViewById(R.id.TextView04);

        // get the location manager
        service = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        boolean enabled = service.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (!enabled) {
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(intent);
        }

        // Define the criteria how to select the locatioin provider -> use
        // default
        Criteria criteria = new Criteria();
        criteria.setAccuracy(ACCURACY_FINE);

        provider = service.getBestProvider(criteria,false);
        Location location = service.getLastKnownLocation(provider);

        // Initialize the location fields
        if (location != null) {
            System.out.println("Provider " + provider + " has been selected.");
            onLocationChanged(location);
        } else {
            latitudeField.setText("Location not available");
            longitudeField.setText("Location not available");
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.my, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause() {
        super.onPause();
        service.removeUpdates(this);
    }

    @Override
    public void onLocationChanged(Location location) {
        double lat = location.getLatitude();
        double lng = location.getLongitude();
        latitudeField.setText(String.valueOf(lat));
        longitudeField.setText(String.valueOf(lng));
        Toast.makeText(this, "Location Updated",
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub
        if(status == LocationProvider.OUT_OF_SERVICE) {
            Toast.makeText(this, "Out Of Service",
                    Toast.LENGTH_SHORT).show();
        }
        else if(status == LocationProvider.TEMPORARILY_UNAVAILABLE)
        {
            Toast.makeText(this, "Temporary Unavailable",
                    Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(this, "Available",
                Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onProviderEnabled(String provider) {
        Toast.makeText(this, "Enabled new provider " + provider,
                Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onProviderDisabled(String provider) {
        Toast.makeText(this, "Disabled provider " + provider,
                Toast.LENGTH_SHORT).show();
    }
}
