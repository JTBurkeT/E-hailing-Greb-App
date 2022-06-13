package com.example.e_hailing;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class customerDataBase extends SQLiteOpenHelper {

    public customerDataBase(Context context) {
        super(context, "CustomerData.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase DB) {
        DB.execSQL("create Table CustomerData(UserName TEXT primary key, Email TEXT,Password TEXT, Status TEXT default null, ExpectedArrivalTime TEXT default null,Capacity INTEGER default 0,  StartingLongLa TEXT default null, DestinationLongLa TEXT default null)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase DB, int i, int i1) {
        DB.execSQL("drop Table if exists CustomerData");
    }

    public Boolean registerCustomer(String UserName, String Email, String password) {
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("UserName", UserName);
        contentValues.put("Email", Email);
        contentValues.put("Password", password);
        long result = DB.insert("CustomerData", null, contentValues);
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public Cursor getAllCustomerData() {
        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor cursor = DB.rawQuery("Select * from CustomerData", null);
        return cursor;
    }

    public Boolean makeRequest(String UserName, String ExpectedArrivalTime, int Capacity, String StartingLongLa, String DestinationLongLa) {
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("Status", "Pending");
        contentValues.put("ExpectedArrivalTime", ExpectedArrivalTime);
        contentValues.put("Capacity", Capacity);
        contentValues.put("StartingLongLa", StartingLongLa);
        contentValues.put("DestinationLongLa", DestinationLongLa);
        Cursor cursor = DB.rawQuery("Select * from CustomerData where UserName = ?", new String[]{UserName});
        if (cursor.getCount() > 0) {
            long result = DB.update("CustomerData", contentValues, "UserName=?", new String[]{UserName});
            if (result == -1) {
                return false;
            } else {
                return true;
            }
        } else {
            return false;
        }
    }

    public String getPassword(String name) {
        SQLiteDatabase DB = this.getReadableDatabase();
        String res = "";
        Cursor cursor = DB.rawQuery("Select Password from CustomerData where UserName = ?", new String[]{name});
        if (cursor.moveToFirst()) {
            res = cursor.getString(0);
        }
        cursor.close();


        return res;

    }

    public Cursor getCustomerData()
    {
        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor cursor = DB.rawQuery("Select * from CustomerData", null);
        return cursor;
    }

    public Boolean setStatus(String customerName, String status){
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("Status", status);
        Cursor cursor = DB.rawQuery("Select * from CustomerData where UserName = ?", new String[]{customerName});
        if (cursor.getCount() > 0) {
            long result = DB.update("CustomerData", contentValues, "UserName=?", new String[]{customerName});
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