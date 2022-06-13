package com.example.e_hailing;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class driverDataBase extends SQLiteOpenHelper {

    public driverDataBase(Context context) {
        super(context, "DriverData.db", null, 1);
    }


    @Override
    public void onCreate(SQLiteDatabase DB) {
        DB.execSQL("create Table DriverData(Name TEXT primary key, Status TEXT , Capacity INTEGER , CurrentLongLa TEXT, Customer TEXT DEFAULT NULL ,Rating DOUBLE Default 0, INTEGER TotalRating default 0)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase DB, int i, int i1) {
        DB.execSQL("drop Table if exists DriverData");
    }

    public Boolean addDriver(String Name, int Capacity ,String CurrentLongLa)
    {
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("Name", Name);
        contentValues.put("Capacity", Capacity);
        contentValues.put("CurrentLongLa", CurrentLongLa);
        contentValues.put("Status", "Available");
        long result=DB.insert("DriverData", null, contentValues);
        if(result==-1){
            return false;
        }else{
            return true;
        }
    }

    public Boolean deleteDriver(String name)
    {
        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor cursor = DB.rawQuery("Select * from DriverData where Name = ?", new String[]{name});
        if (cursor.getCount() > 0) {
            long result = DB.delete("DriverData", "Name=?", new String[]{name});
            if (result == -1) {
                return false;
            } else {
                return true;
            }
        } else {
            return false;
        }
    }

    public Cursor getDriverData(){
        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor cursor = DB.rawQuery("Select * from DriverData", null);
        return cursor;
    }

    public boolean checkDb(){
        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor mCursor = DB.rawQuery("SELECT * FROM DriverData" , null);
        Boolean rowExists;

        if (mCursor.moveToFirst())
        {
            // DO SOMETHING WITH CURSOR
            rowExists = true;

        } else
        {
            // I AM EMPTY
            rowExists = false;
        }
        return rowExists;
    }

    public int getNumberOfRating(String driverName){
        SQLiteDatabase DB = this.getReadableDatabase();
        int res=0;
        Cursor cursor = DB.rawQuery("Select TotalRating from DriverData where Name = ?", new String[]{driverName});
        if (cursor.moveToFirst()) {
            res = Integer.parseInt(cursor.getString(0));
        }
        cursor.close();


        return res;
    }

    public double getTotalRating(String driverName){
        SQLiteDatabase DB = this.getReadableDatabase();
        double res=0;
        Cursor cursor = DB.rawQuery("Select Rating from DriverData where Name = ?", new String[]{driverName});
        if (cursor.moveToFirst()) {
            res = Double.parseDouble(cursor.getString(0));
        }
        cursor.close();
        return res;
    }

    //use to set the customer of the driver
    public Boolean setCustomer(String driverName, String customerName)
    {
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("Customer", customerName);
        Cursor cursor = DB.rawQuery("Select * from DriverData where Name = ?", new String[]{driverName});
        if (cursor.getCount() > 0) {
            long result = DB.update("DriverData", contentValues, "Name=?", new String[]{driverName});
            if (result == -1) {
                return false;
            } else {
                return true;
            }
        } else {
            return false;
        }
    }

    //use to add and set the rating
    public Boolean setAndAddRating(String driverName,double rating){
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        int number=getNumberOfRating(driverName);
        double currentRating=getTotalRating(driverName);
        double newRating= ((number*currentRating)+rating)/(number+1);
        contentValues.put("Rating", newRating);
        contentValues.put("TotalRating", number+1);

        Cursor cursor = DB.rawQuery("Select * from DriverData where Name = ?", new String[]{driverName});
        if (cursor.getCount() > 0) {
            long result = DB.update("DriverData", contentValues, "Name=?", new String[]{driverName});
            if (result == -1) {
                return false;
            } else {
                return true;
            }
        } else {
            return false;
        }
    }

    //use to set the status of the driver
    public Boolean setStatus(String driverName,String status){
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("Status", status);
        Cursor cursor = DB.rawQuery("Select * from DriverData where Name = ?", new String[]{driverName});
        if (cursor.getCount() > 0) {
            long result = DB.update("DriverData", contentValues, "Name=?", new String[]{driverName});
            if (result == -1) {
                return false;
            } else {
                return true;
            }
        } else {
            return false;
        }
    }

    //set the current longitude and latitude of the driver
    public Boolean setCurDriverLaLong(String driverName,String LongLa){
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("CurrentLongLa", LongLa);
        Cursor cursor = DB.rawQuery("Select * from DriverData where Name = ?", new String[]{driverName});
        if (cursor.getCount() > 0) {
            long result = DB.update("DriverData", contentValues, "Name=?", new String[]{driverName});
            if (result == -1) {
                return false;
            } else {
                return true;
            }
        } else {
            return false;
        }
    }


}
