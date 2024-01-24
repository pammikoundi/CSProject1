package com.example.csproject1;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ClassDBHelper extends SQLiteOpenHelper {
    public ClassDBHelper(Context context) {
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

    public boolean insertData(String className, String classInstructor, String classDetails) {
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("className", className);
        contentValues.put("classInstructor", classInstructor);
        contentValues.put("classDetails", classDetails);

        long result = DB.insert("Class_details", null, contentValues);
        return result != 1;
    }

    public boolean updateData(String className, String classInstructor, String classDetails) {
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("className", className);
        contentValues.put("classInstructor", classInstructor);
        contentValues.put("classDetails", classDetails);
        Cursor cursor = DB.rawQuery("Select * from Class_details where className =?", new String[]{className});

        if (cursor.getCount() > 0) {

            long result = DB.update("Class_details", contentValues, "className =?", new String[]{className});
            cursor.close();
            return result != 1;
        } else {
            cursor.close();
            return false;
        }
    }


    public boolean deleteData(String className) {
        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor cursor = DB.rawQuery("Select * from Class_details where className =?", new String[]{className});

        if (cursor.getCount() > 0) {

            long result = DB.delete("Class_details", "name=?", new String[]{className});
            cursor.close();
            return result != 1;
        } else {
            cursor.close();
            return false;
        }
    }

    public Cursor getData() {
        SQLiteDatabase DB = this.getWritableDatabase();

        return DB.rawQuery("Select * from Class_details", null);
    }
}