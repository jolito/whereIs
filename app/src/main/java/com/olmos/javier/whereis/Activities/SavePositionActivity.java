package com.olmos.javier.whereis.Activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.olmos.javier.whereis.DataBase.DBHelper;
import com.olmos.javier.whereis.Objects.Location;
import com.olmos.javier.whereis.R;

public class SavePositionActivity extends FragmentActivity implements GoogleMap.OnMarkerDragListener {

    private final static String EXTRA_MESSAGE = "com.olmos.javier.whereis.MESSAGE";
    private final static String EXTRA_TYPE = "com.olmos.javier.whereis.TYPE";
    private final static String EXTRA_LATITUDE = "com.olmos.javier.whereis.LATITUDE";
    private final static String EXTRA_LONGITUDE = "com.olmos.javier.whereis.LONGITUDE";
    private final static String EXTRA_DESCRIPTION = "com.olmos.javier.whereis.DESCRIPTION";
    private final static String EXTRA_ID = "com.olmos.javier.whereis.ID";

    private double latitudeIni = 0.0;
    private double longitudeIni = 0.0;
    private boolean type = false;
    private int ide = -1;
    private Marker markerActual;

    private GoogleMap map; // Might be null if Google Play services APK is not available.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_position);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String value = extras.getString(EXTRA_MESSAGE);
            EditText editText = this.findViewById(R.id.title_location);
            editText.setText(value);

            String des = extras.getString(EXTRA_DESCRIPTION);
            EditText description = this.findViewById(R.id.description_location);
            description.setText(des);

            latitudeIni = extras.getDouble(EXTRA_LATITUDE);
            longitudeIni = extras.getDouble(EXTRA_LONGITUDE);
            type = extras.getBoolean(EXTRA_TYPE);
            ide = extras.getInt(EXTRA_ID);

            Button but = this.findViewById(R.id.save_location_button);
            //controlar el boton y lo que tiene que hacer, borrar o guardar
            if (type) {
                //true cuando es un valor real ya guardado. cancelar lo editable y quitar boton
                editText.setEnabled(false);
                description.setEnabled(false);
                but.setText(getString(R.string.delete_text));
            } else {
                editText.setEnabled(true);
                description.setEnabled(true);
                but.setText(getString(R.string.save_text));
            }
        }
        setUpMapIfNeeded();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    public void saveLocationClick(View view) {
        if (type) {
            deleteDB();
        } else {
            saveDB();
        }
    }

    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #map} is not null.
     * <p/>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p/>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (map == null) {
            // Try to obtain the map from the SupportMapFragment.
            SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
            supportMapFragment.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(GoogleMap googleMap) {
                    map = googleMap;
                    configureMap();
                }
            });
        }
    }

    private void configureMap() {
        map.setMapType(GoogleMap.MAP_TYPE_HYBRID);

        CameraUpdate center = CameraUpdateFactory.newLatLng(new LatLng(latitudeIni, longitudeIni));
        CameraUpdate zoom= CameraUpdateFactory.zoomTo(17);
        map.moveCamera(center);
        map.animateCamera(zoom);
        map.setOnMarkerDragListener(this);
        if (map != null) {
            setUpMap();
        }
    }
    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p/>
     * This should only be called once and when we are sure that {@link #map} is not null.
     */
    private void setUpMap() {
        MarkerOptions theMarker = new MarkerOptions();
        theMarker.position(new LatLng(latitudeIni, longitudeIni));
        theMarker.title(latitudeIni + " , " + longitudeIni);
        theMarker.visible(true);
        theMarker.draggable(true);
        markerActual = map.addMarker(theMarker);
    }

    @Override
    public void onMarkerDragStart(Marker marker) {
        if (marker.equals(markerActual))
        {
            if (markerActual.isInfoWindowShown()) {
                markerActual.hideInfoWindow();
            }
        }
    }
    @Override
    public void onMarkerDrag(Marker marker) {}

    @Override
    public void onMarkerDragEnd(Marker marker) {
        if (marker.equals(markerActual)) {
            LatLng pos = markerActual.getPosition();
            markerActual.setTitle(pos.latitude + " , " + pos.longitude);
        }
    }

    private void saveDB() {
        EditText titleView = this.findViewById(R.id.title_location);
        EditText descriptionView = this.findViewById(R.id.description_location);

        String title = titleView.getText().toString();
        String description = descriptionView.getText().toString();
        double latitude = markerActual.getPosition().latitude;
        double longitude = markerActual.getPosition().longitude;

        Location loc = new Location(title, description, latitude, longitude);
        DBHelper mydb = new DBHelper(this);
        Boolean correcto = mydb.insertLocation(loc);
        mydb.close();
        if (correcto) {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setTitle(getString(R.string.success));
            alertDialogBuilder.setMessage(getString(R.string.save_location_ok));
            alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int arg1) {
                    finish();
                }
            });
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        } else {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setTitle(getString(R.string.error));
            alertDialogBuilder.setMessage(getString(R.string.save_location_error));
            alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int arg1) {
                    dialog.cancel();
                }
            });
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }
    }

    private void  deleteDB() {
        DBHelper mydb = new DBHelper(this);
        System.out.println("" + ide);
        Location location = mydb.getData(ide);
        if (location == null) System.out.println("null");
        else System.out.println(location.toString());
        int resultado = mydb.deleteLocation(ide);
        System.out.println("result: " + resultado);
        mydb.close();
        finish();
    }
}
