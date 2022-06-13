package com.example.e_hailing;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class adminDataBase extends SQLiteOpenHelper {
    public adminDataBase(Context context) {
        super(context, "AdminData.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase DB) {
        DB.execSQL("create Table AdminData(UserName TEXT primary key, Password TEXT)");
        ContentValues contentValues = new ContentValues();
        contentValues.put("UserName", "admin123");
        contentValues.put("Password", "123");
        DB.insert("AdminData", null, contentValues);
    }

    @Override
    public void onUpgrade(SQLiteDatabase DB, int i, int i1) {
        DB.execSQL("drop Table if exists AdminData");
    }

    public void insertAdmin() {
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("UserName", "admin123");
        contentValues.put("Password", "123");
        DB.insert("AdminData", null, contentValues);

    }

    public String getPassword(String name) {
        String res = "";
        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor cursor = DB.rawQuery("Select Password from AdminData where UserName = ?", new String[]{name});
        if (cursor.moveToFirst()) {
            res = cursor.getString(0);
        }
        cursor.close();
        return res;
    }


}

