package com.example.locationfinder;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.Locale;

public class LocDatabase extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "LocationFinder.db";
    public static final String LOC_TABLE = "locations";
    public static final String LOC_ID = "id";
    public static final String LOC_ADDR = "address";
    public static final String LOC_LAT = "latitude";
    public static final String LOC_LONG = "longtitude";

    public LocDatabase(Context context) { super(context, DATABASE_NAME, null, 1);}

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
            "create table " + LOC_TABLE + " (" +
            LOC_ID + " integer primary key autoincrement, " +
            LOC_ADDR + " text not null, " +
            LOC_LAT + " real, " +
            LOC_LONG + " real" +
            ");"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldV, int newV) {
        db.execSQL("drop table if exists " + LOC_TABLE);
        onCreate(db);
    }

//    Functions to add, update and delete locations on the database

    public boolean addLoc(LocModel location){
        SQLiteDatabase db = this.getWritableDatabase();

//        get the values from model to be put in the db
        ContentValues values = new ContentValues();
        values.put(LOC_ADDR, location.getAddress());
        values.put(LOC_LAT, location.getLatitude());
        values.put(LOC_LONG, location.getLongtitude());

//        get insertion status
        long addStatus = db.insert(LOC_TABLE, null, values);

//        return true or false based on the success of the query
        if (addStatus == -1){
            return false;
        }
        return true;
    }

    public boolean updateLoc(LocModel location){
        SQLiteDatabase db = this.getWritableDatabase();

//        get the values from model to be put in the db
        ContentValues values = new ContentValues();
        values.put(LOC_ADDR, location.getAddress());
        values.put(LOC_LAT, location.getLatitude());
        values.put(LOC_LONG, location.getLongtitude());

//        get update status
        long updateStatus = db.update(LOC_TABLE, values, LOC_ID + "=?", new String[] {Integer.toString(location.getId())});

//        return true or false based on the success of the query
        if (updateStatus == -1){
            return false;
        }
        return true;
    }

    public boolean deleteLoc(LocModel location){
        SQLiteDatabase db = this.getWritableDatabase();

        long deleteStatus = db.delete(LOC_TABLE, LOC_ID + "=?", new String[] {Integer.toString(location.getId())});

//        return true or false based on the success of the query
        if (deleteStatus == -1){
            return false;
        }
        return true;
    }

//    get all locations in the database to be displayed
    public ArrayList<LocModel> getAllLocs(){
        ArrayList<LocModel> locations = new ArrayList<>();

//        make a query to get from the database
        String query = "select * from " + LOC_TABLE;
        SQLiteDatabase db = this.getReadableDatabase();

//        iterate over each entry and add the location to the arraylist
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()){
            do{
                int id = cursor.getInt(0);
                String address = cursor.getString(1);
                double latitude = cursor.getDouble(2);
                double longtitude = cursor.getDouble(3);

//                make a locmodel to add to arraylist
                LocModel loc = new LocModel(id, address, latitude, longtitude);
                locations.add(loc);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return locations;
    }
}
