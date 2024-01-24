package com.example.csprojectcalapp;

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
        DB.execSQL("create Table Class_info(uniqueID_class TEXT primary key,className TEXT, classInstructor TEXT,classDetails TEXT)");
        DB.execSQL("create Table Class_details(uniqueID_assignment TEXT primary key,assignmentName TEXT,assignmentType Text,dueDate TEXT,assignmentDetails TEXT)");
    }
    public void onUpgrade(SQLiteDatabase DB, int oldVersion, int newVersion) {
        DB.execSQL("drop Table if exists Class_info");
        DB.execSQL("drop Table if exists Class_details");
    }

    //Class Data Methods
    public boolean insertClassData(String className, String classInstructor, String classDetails) {
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("className", className);
        contentValues.put("classInstructor", classInstructor);
        contentValues.put("classDetails", classDetails);

        long result = DB.insert("Class_info", null, contentValues);
        return result != 1;
    }
    public boolean updateClassData(String className, String classInstructor, String classDetails) {
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("className", className);
        contentValues.put("classInstructor", classInstructor);
        contentValues.put("classDetails", classDetails);
        Cursor cursor = DB.rawQuery("Select * from Class_info where className =?", new String[]{className});
        if (cursor.getCount() > 0) {
            long result = DB.update("Class_info", contentValues, "className =?", new String[]{className});
            cursor.close();
            return result != 1;
        } else {
            cursor.close();
            return false;
        }
    }
    public boolean deleteClassData(String className) {
        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor cursor = DB.rawQuery("Select * from Class_info where className =?", new String[]{className});

        if (cursor.getCount() > 0) {

            long result = DB.delete("Class_details", "name=?", new String[]{className});
            cursor.close();
            return result != 1;
        } else {
            cursor.close();
            return false;
        }
    }

    public Cursor getClassData() {
        SQLiteDatabase DB = this.getWritableDatabase();
        return DB.rawQuery("Select * from Class_info", null);
    }

//Assignment Table Methods
    public boolean insertAssignmentData(String className, String classInstructor, String classDetails) {
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("className", className);
        contentValues.put("classInstructor", classInstructor);
        contentValues.put("classDetails", classDetails);

        long result = DB.insert("Class_details", null, contentValues);
        return result != 1;
    }
    public boolean updateAssignmentData(String className, String classInstructor, String classDetails) {
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("className", className);
        contentValues.put("classInstructor", classInstructor);
        contentValues.put("classDetails", classDetails);
        Cursor cursor = DB.rawQuery("Select * from Class_info where className =?", new String[]{className});
        if (cursor.getCount() > 0) {
            long result = DB.update("Class_details", contentValues, "className =?", new String[]{className});
            cursor.close();
            return result != 1;
        } else {
            cursor.close();
            return false;
        }
    }
    public boolean deleteAssignmentData(String uniqueID) {
        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor cursor = DB.rawQuery("Select * from Class_details where uniqueID_assignment =?", new String[]{uniqueID});

        if (cursor.getCount() > 0) {
            long result = DB.delete("Class_details", "name=?", new String[]{uniqueID});
            cursor.close();
            return result != 1;
        } else {
            cursor.close();
            return false;
        }
    }

    public Cursor getAssignmentData(String class_id) {
        SQLiteDatabase DB = this.getWritableDatabase();
        return DB.rawQuery("Select * from Class_info where uniqueClass_id=?",new String[]{class_id}, null);
    }




}