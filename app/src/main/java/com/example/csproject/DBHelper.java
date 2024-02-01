package com.example.csproject;

/*
*This file contains all methods relating to Databases.
* It contains 3 tables userdetails, classdetails and assignmentdetails
* It contains the methods for each of the tables.
*
*
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
    public DBHelper(Context context) {
        super(context, "Userdata.db", null, 1);
    }
    @Override
    public void onCreate(SQLiteDatabase DB) {
        DB.execSQL("create Table Userdetails(user_name TEXT primary key)");
        DB.execSQL("create Table Classdetails(class_name TEXT primary key,user_name TEXT, instructor TEXT, class_section TEXT, class_location TEXT, repeat TEXT, start_time TEXT, end_time TEXT)");
        DB.execSQL("create Table Assignmentdetails(assignment_name TEXT primary key,assignment_type TEXT, assignment_class TEXT, assignment_location TEXT, due_date TEXT, progress TEXT, complete TEXT)");

    }
    @Override
    public void onUpgrade(SQLiteDatabase DB, int i, int ii) {
        DB.execSQL("drop Table if exists Userdetails");
        DB.execSQL("drop Table if exists Classdetails");
        DB.execSQL("drop Table if exists Assignmentdetails");
    }
    public Boolean insertUserdata(String user_name)
    {
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("user_name",user_name);

        long result=DB.insert("Userdetails", null, contentValues);
        return result != -1;
    }
    public Boolean updateUserdata(String user_name)
    {
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("user_name",user_name);


        Cursor cursor = DB.rawQuery("Select * from Userdetails where user_name = ?", new String[]{user_name});
        if (cursor.getCount() > 0) {
            long result = DB.update("Userdetails", contentValues, "user_name=?", new String[]{user_name});
            return result != -1;
        } else {
            return false;
        }
    }
    public Boolean deleteuserdata(String name)
    {
        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor cursor = DB.rawQuery("Select * from Userdetails where user_name = ?", new String[]{name});
        if (cursor.getCount() > 0) {
            long result = DB.delete("Userdetails", "user_name=?", new String[]{name});
            return result != -1;
        } else {
            return false;
        }
    }
    public Cursor getalluserdata()
    {
        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor cursor = DB.rawQuery("Select * from Userdetails", null);
        return cursor;
    }

    public Cursor getuserdata(String userName)
    {
        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor cursor = DB.rawQuery("Select * from Userdetails where user_name=?", new String[]{userName});
        return cursor;
    }

    public Boolean insertassignmentdata(String assignment_name, String assignment_type, String assignment_class, String assignment_location,String due_date, String progress, String complete)
    {
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("assignment_name",assignment_name);
        contentValues.put("assignment_type",assignment_type);
        contentValues.put("assignment_class",assignment_class);
        contentValues.put("assignment_location",assignment_location);
        contentValues.put("due_date",due_date);
        contentValues.put("progress",progress);
        contentValues.put("complete",complete);

        long result=DB.insert("Assignmentdetails", null, contentValues);
        return result != -1;
    }
    public Boolean updateassignmentdata(String assignment_name, String assignment_type, String assignment_class, String assignment_location,String due_date, String progress, String complete)
    {
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("assignment_name",assignment_name);
        contentValues.put("assignment_type",assignment_type);
        contentValues.put("assignment_class",assignment_class);
        contentValues.put("assignment_location",assignment_location);
        contentValues.put("due_date",due_date);
        contentValues.put("progress",progress);
        contentValues.put("complete",complete);

        Cursor cursor = DB.rawQuery("Select * from Assignmentdetails where assignment_name = ?", new String[]{assignment_name});
        if (cursor.getCount() > 0) {
            long result = DB.update("Assignmentdetails", contentValues, "assignment_name=?", new String[]{assignment_name});
            return result != -1;
        } else {
            return false;
        }
    }
    public Boolean deleteassignmentdata(String name)
    {
        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor cursor = DB.rawQuery("Select * from Assignmentdetails where assignment_name = ?", new String[]{name});
        if (cursor.getCount() > 0) {
            long result = DB.delete("Assignmentdetails", "assignment_name=?", new String[]{name});
            return result != -1;
        } else {
            return false;
        }
    }
    public Cursor getallassignmentdata()
    {
        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor cursor = DB.rawQuery("Select * from Assignmentdetails", null);
        return cursor;
    }

    public Cursor getassignmentdata(String className)
    {
        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor cursor = DB.rawQuery("Select * from Assignmentdetails where assignment_class=?", new String[]{className});
        return cursor;
    }

    public Cursor getassignmentinfodata(String assignmentName)
    {
        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor cursor = DB.rawQuery("Select * from Assignmentdetails where assignment_name=?", new String[]{assignmentName});
        return cursor;
    }

    public Boolean insertclassdata(String name,String user_name,String instructor, String class_section, String class_location, String repeat, String start_time, String end_time)
    {
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("class_name", name);
        contentValues.put("user_name", user_name);
        contentValues.put("instructor", instructor);
        contentValues.put("class_section", class_section);
        contentValues.put("class_location", class_location);
        contentValues.put("repeat", repeat);
        contentValues.put("start_time", start_time);
        contentValues.put("end_time", end_time);
        long result=DB.insert("Classdetails", null, contentValues);
        return result != -1;
    }
    public Boolean updateclassdata(String name, String instructor, String class_section, String class_location, String repeat, String start_time, String end_time)
    {
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("class_name", name);
        contentValues.put("instructor", instructor);
        contentValues.put("class_section", class_section);
        contentValues.put("class_location", class_location);
        contentValues.put("repeat", repeat);
        contentValues.put("start_time", start_time);
        contentValues.put("end_time", end_time);

        Cursor cursor = DB.rawQuery("Select * from Classdetails where class_name = ?", new String[]{name});
        if (cursor.getCount() > 0) {
            long result = DB.update("Classdetails", contentValues, "class_name=?", new String[]{name});
            return result != -1;
        } else {
            return false;
        }
    }

    public Boolean deleteclassdata(String name)
    {
        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor cursor = DB.rawQuery("Select * from Classdetails where class_name = ?", new String[]{name});
        if (cursor.getCount() > 0) {
            long result = DB.delete("Classdetails", "class_name=?", new String[]{name});
            return result != -1;
        } else {
            return false;
        }
    }

    public Cursor getallclassdata()
    {
        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor cursor = DB.rawQuery("Select * from Classdetails", null);
        return cursor;
    }
    public Cursor getclassdata(String userName)
    {
        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor cursor = DB.rawQuery("Select * from Classdetails where user_name = ?", new String[]{userName});
        return cursor;
    }

}