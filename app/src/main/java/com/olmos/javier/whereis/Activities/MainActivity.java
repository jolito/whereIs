package com.olmos.javier.whereis.Activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.olmos.javier.whereis.R;

import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private final static String EXTRA_MESSAGE = "com.olmos.javier.whereis.MESSAGE";
    private final static String EXTRA_TYPE = "com.olmos.javier.whereis.TYPE";
    private final static String EXTRA_LATITUDE = "com.olmos.javier.whereis.LATITUDE";
    private final static String EXTRA_LONGITUDE = "com.olmos.javier.whereis.LONGITUDE";
    private final static String EXTRA_DESCRIPTION = "com.olmos.javier.whereis.DESCRIPTION";
    private final static String EXTRA_ID = "com.olmos.javier.whereis.ID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void saveClick(View view) {
        Intent intent = new Intent(this, SavePositionActivity.class);
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm dd-MM-yyyy", Locale.getDefault());
        String currentDateandTime = sdf.format(new Date());
        String message = "(" + currentDateandTime + ")";
        intent.putExtra(EXTRA_MESSAGE, message); //para pasar parametros (key,value)
        intent.putExtra(EXTRA_TYPE, false);
        intent.putExtra(EXTRA_DESCRIPTION, "");
        intent.putExtra(EXTRA_ID, -1);

        LocationManager locationManager;
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        Criteria crta = new Criteria();
        crta.setAccuracy(Criteria.ACCURACY_FINE);
        crta.setAltitudeRequired(false);
        crta.setBearingRequired(false);
        crta.setCostAllowed(true);
        crta.setPowerRequirement(Criteria.POWER_LOW);
        String provider;
        if (locationManager != null) {
            provider = locationManager.getBestProvider(crta, true);
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            Location location = locationManager.getLastKnownLocation(provider);

            if (location != null) {
                double lat = location.getLatitude();
                double lon = location.getLongitude();
                intent.putExtra(EXTRA_LATITUDE, lat);
                intent.putExtra(EXTRA_LONGITUDE, lon);
            } else {
                intent.putExtra(EXTRA_LATITUDE, 0.0);
                intent.putExtra(EXTRA_LONGITUDE, 0.0);
            }
        }
        startActivity(intent);
    }

    public void searchingClick(View view) {
        Intent intent = new Intent(this, ListLocationsActivity.class);
        startActivity(intent);
    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }*/
}
