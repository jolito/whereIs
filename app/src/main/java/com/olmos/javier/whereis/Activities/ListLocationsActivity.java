package com.olmos.javier.whereis.Activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.olmos.javier.whereis.Adapters.LocationItemAdapter;
import com.olmos.javier.whereis.DataBase.DBHelper;
import com.olmos.javier.whereis.Objects.Location;
import com.olmos.javier.whereis.R;

import java.util.ArrayList;

public class ListLocationsActivity extends AppCompatActivity {

    private final static String EXTRA_MESSAGE = "com.olmos.javier.whereis.MESSAGE";
    private final static String EXTRA_TYPE = "com.olmos.javier.whereis.TYPE";
    private final static String EXTRA_LATITUDE = "com.olmos.javier.whereis.LATITUDE";
    private final static String EXTRA_LONGITUDE = "com.olmos.javier.whereis.LONGITUDE";
    private final static String EXTRA_DESCRIPTION = "com.olmos.javier.whereis.DESCRIPTION";
    private final static String EXTRA_ID = "com.olmos.javier.whereis.ID";

    private ListView listView;
    private int ide = -1;
    private ArrayList<Location> locations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_locations);

        DBHelper mydb = new DBHelper(this);

        listView = findViewById(R.id.listView);
        locations = mydb.getAllLocations();
        mydb.close();

        listView.setAdapter(new LocationItemAdapter(this, locations));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View view,
                                    int position, long arg) {

                Location location = (Location) listView.getAdapter().getItem(position);
                goToMap(location, position);
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapter, View view,
                                           int position, long arg) {
                ide = position;
                //opcion de borrar con dialog
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(view.getContext());
                alertDialogBuilder.setTitle(getString(R.string.delete_text));
                alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int arg1) {
                        deleteDB();
                    }
                });
                alertDialogBuilder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();

                return true;//false
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        DBHelper mydb = new DBHelper(this);
        locations = mydb.getAllLocations();
        mydb.close();
        System.out.println(locations);
        LocationItemAdapter locationItemAdapter = (LocationItemAdapter)this.listView.getAdapter();
        locationItemAdapter.notifyDataSetChanged();
    }

    private void goToMap(Location location, int i) {
        Intent intent = new Intent(this, SavePositionActivity.class);
        intent.putExtra(EXTRA_MESSAGE, location.getTitle());
        intent.putExtra(EXTRA_TYPE, true);
        intent.putExtra(EXTRA_DESCRIPTION, location.getDescription());
        intent.putExtra(EXTRA_LATITUDE, location.getLatitude());
        intent.putExtra(EXTRA_LONGITUDE, location.getLongitude());
        intent.putExtra(EXTRA_ID, i);
        startActivity(intent);
    }

    private  void  deleteDB() {
        DBHelper mydb = new DBHelper(this);
        mydb.deleteLocation(ide);
        mydb.close();
    }
}
