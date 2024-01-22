package com.example.csproject1;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper {
    public DBHelper(Context context) {
        super(context, "Class_details.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase DB) {
        DB.execSQL("create Table Class_details(name TEXT primary key,className TEXT, classInstructor TEXT,classDetails TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase DB, int oldVersion, int newVersion) {
        DB.execSQL("drop Table if exists Class_details");
    }

    public boolean insertdata(String className, String classInstructor, String classDetails) {
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("className", className);
        contentValues.put("classInstructor", classInstructor);
        contentValues.put("classDetails", classDetails);

        long result = DB.insert("Class_details", null, contentValues);
        if (result == 1) {
            return false;
        } else {
            return true;
        }
    }

    public boolean updatedata(String className, String classInstructor, String classDetails) {
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("className", className);
        contentValues.put("classInstructor", classInstructor);
        contentValues.put("classDetails", classDetails);
        Cursor cursor = DB.rawQuery("Select * from Class_details where className =?", new String[]{className});

        if (cursor.getCount() > 0) {

            long result = DB.update("Class_details", contentValues,"className =?" ,new String[] {className});
            if (result == 1) {
                return false;
            } else {
                return true;
            }
        } else {
            return false;
        }
    }


    public boolean deletedata(String className) {
        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor cursor = DB.rawQuery("Select * from Class_details where className =?", new String[]{className});

        if (cursor.getCount() > 0) {

            long result = DB.delete("Class_details", "name=?", new String[] {className});
            if (result == 1) {
                return false;
            } else {
                return true;
            }
        } else {
            return false;
        }
    }

    public Cursor getdata() {
        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor cursor = DB.rawQuery("Select * from Class_details", null);

        return cursor;
    }
}