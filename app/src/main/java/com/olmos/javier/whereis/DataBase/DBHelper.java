package com.olmos.javier.whereis.DataBase;

import java.util.ArrayList;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;

import com.olmos.javier.whereis.Objects.Location;

/**
 * Created by JOL on 08/12/2015.
 */

public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "whereis.db";
    private static final String LOCATIONS_TABLE_NAME = "locations";
    private static final String LOCATIONS_COLUMN_ID = "id";
    private static final String LOCATIONS_COLUMN_TITLE = "title";
    private static final String LOCATIONS_COLUMN_DESCRIPTION = "description";
    private static final String LOCATIONS_COLUMN_LATITUDE = "latitude";
    private static final String LOCATIONS_COLUMN_LONGITUDE = "longitude";

    public DBHelper(Context context)
    {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "create table " + LOCATIONS_TABLE_NAME +
                        " (" + LOCATIONS_COLUMN_ID + " integer primary key, " +
                        LOCATIONS_COLUMN_TITLE + " text, " +
                        LOCATIONS_COLUMN_DESCRIPTION + " text, " +
                        LOCATIONS_COLUMN_LATITUDE + " real, " +
                        LOCATIONS_COLUMN_LONGITUDE + " real)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + LOCATIONS_TABLE_NAME);
        onCreate(db);
    }

    public boolean insertLocation(Location location) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(LOCATIONS_COLUMN_TITLE, location.getTitle());
        contentValues.put(LOCATIONS_COLUMN_DESCRIPTION, location.getDescription());
        contentValues.put(LOCATIONS_COLUMN_LATITUDE, location.getLatitude());
        contentValues.put(LOCATIONS_COLUMN_LONGITUDE, location.getLongitude());
        long l = db.insert(LOCATIONS_TABLE_NAME, null, contentValues);
        return l != -1;
    }

    public Location getData(int id){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor =  db.rawQuery( "select * from " + LOCATIONS_TABLE_NAME + " where id=" + id + "", null );
        cursor.moveToFirst();
        Location location = null;
        if(!cursor.isAfterLast()){
            String title = cursor.getString(cursor.getColumnIndex(LOCATIONS_COLUMN_TITLE));
            String description = cursor.getString(cursor.getColumnIndex(LOCATIONS_COLUMN_DESCRIPTION));
            double latitude = cursor.getDouble(cursor.getColumnIndex(LOCATIONS_COLUMN_LATITUDE));
            double longitude = cursor.getDouble(cursor.getColumnIndex(LOCATIONS_COLUMN_LONGITUDE));
            cursor.moveToNext();
            location = new Location(title, description, latitude, longitude);
        }
        cursor.close();
        db.close();
        return location;
    }

    public int numberOfRows(){
        SQLiteDatabase db = this.getReadableDatabase();
        return (int) DatabaseUtils.queryNumEntries(db, LOCATIONS_TABLE_NAME);
    }

    public boolean updateLocation (Integer id, Location location) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(LOCATIONS_COLUMN_TITLE, location.getTitle());
        contentValues.put(LOCATIONS_COLUMN_DESCRIPTION, location.getDescription());
        contentValues.put(LOCATIONS_COLUMN_LATITUDE, location.getLatitude());
        contentValues.put(LOCATIONS_COLUMN_LONGITUDE, location.getLongitude());
        db.update(LOCATIONS_TABLE_NAME, contentValues, "" + LOCATIONS_COLUMN_ID + " = ? ", new String[] { Integer.toString(id) } );
        return true;
    }

    public Integer deleteLocation(Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        System.out.println("" + LOCATIONS_COLUMN_ID + " = " + id);
        return db.delete(LOCATIONS_TABLE_NAME, "" + LOCATIONS_COLUMN_ID + " = " + id, null);
    }

    public ArrayList<Location> getAllLocations() {
        ArrayList<Location> array_list = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor =  db.rawQuery( "select * from " + LOCATIONS_TABLE_NAME, null );
        cursor.moveToFirst();

        while(!cursor.isAfterLast()){
            String ti = cursor.getString(cursor.getColumnIndex(LOCATIONS_COLUMN_TITLE));
            String de = cursor.getString(cursor.getColumnIndex(LOCATIONS_COLUMN_DESCRIPTION));
            double la = cursor.getDouble(cursor.getColumnIndex(LOCATIONS_COLUMN_LATITUDE));
            double lo = cursor.getDouble(cursor.getColumnIndex(LOCATIONS_COLUMN_LONGITUDE));

            array_list.add(new Location(ti, de, la, lo));
            cursor.moveToNext();
        }
        cursor.close();
        db.close();
        return array_list;
    }
}