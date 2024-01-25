package com.example.csprojectcalapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import org.w3c.dom.Text;

import java.util.UUID;

public class ClassDBHelper extends SQLiteOpenHelper {
    public ClassDBHelper(Context context) {
        super(context, "Class_details.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase DB) {
        DB.execSQL("create Table Class_info(uniqueID_class TEXT primary key,className TEXT, classInstructor TEXT,classSection TEXT,classLocation TEXT,time TEXT,repeat TEXT)");
        DB.execSQL("create Table Class_details(uniqueID_assignment TEXT primary key,uniqueID_class TEXT,assignmentName TEXT,assignmentType Text,dueDate TEXT,complete boolean,assignmentDetails TEXT)");
    }
    public void onUpgrade(SQLiteDatabase DB, int oldVersion, int newVersion) {
        DB.execSQL("drop Table if exists Class_info");
        DB.execSQL("drop Table if exists Class_details");
    }

    //Class Data Methods
    public boolean insertClassData(String className , String classInstructor,String classSection,String classLocation,String startTime,String endTime, String repeat) {
        String uuid = UUID.randomUUID().toString().replace("-", "");
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("uniqueID_class",uuid);
        contentValues.put("className", className);
        contentValues.put("classInstructor", classInstructor);
        contentValues.put("classSection",classSection);
        contentValues.put("classLocation",classLocation);
        contentValues.put("startTime",startTime);
        contentValues.put("endTime",endTime);
        contentValues.put("repeat",repeat);
        long result = DB.insert("Class_info", null, contentValues);
            return result != 1;
    }
    public boolean updateClassData(String uniqueID_class, String className , String classInstructor,String classSection,String classLocation,String date,String startTime,String endTime, String repeat) {
            SQLiteDatabase DB = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put("className", className);
            contentValues.put("classInstructor", classInstructor);
            contentValues.put("classSection",classSection);
            contentValues.put("classLocation",classLocation);
            contentValues.put("date",date);
            contentValues.put("startTime",startTime);
            contentValues.put("endTime",endTime);
        Cursor cursor = DB.rawQuery("Select * from Class_info where uniqueID_class =?", new String[]{uniqueID_class});
        if (cursor.getCount() > 0) {
            long result = DB.update("Class_info", contentValues, "uniqueID_class =?", new String[]{uniqueID_class});
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
            long result = DB.delete("Class_info", "name=?", new String[]{className});
            long del_uuid = DB.delete("Class_details", "name=?", new String[]{cursor.getString(0)});

            cursor.close();
            return result+del_uuid != 1;
        }
            return false;
        }

    public Cursor getClassData() {
        SQLiteDatabase DB = this.getWritableDatabase();
        return DB.rawQuery("Select * from Class_info", null);
    }

////Assignment Table Methods
    public boolean insertAssignmentData(String uniqueID_class , String assignmentName, String assignmentType,String dueDate,boolean complete, String assignmentDetails){
        String uuid = UUID.randomUUID().toString().replace("-", "");

        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("uniqueID_assignment",uuid);
        contentValues.put("uniqueID_class",uniqueID_class);
        contentValues.put("assignmentName", assignmentName);
        contentValues.put("assignmentType", assignmentType);
        contentValues.put("dueDate", dueDate);
        contentValues.put("complete",complete);
        contentValues.put("assignmentDetails",assignmentDetails);

        long result = DB.insert("Class_details", null, contentValues);
        return result != 1;
    }
    public boolean updateAssignmentData(String uniqueID_assignment , String assignmentName,String dueDate,boolean complete, String assignmentDetails){
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("assignmentName", assignmentName);
        contentValues.put("dueDate", dueDate);
        contentValues.put("complete",complete);
        contentValues.put("assignmentDetails",assignmentDetails);

        Cursor cursor = DB.rawQuery("Select * from Class_info where uniqueID_assignment =?", new String[]{uniqueID_assignment});
        if (cursor.getCount() > 0) {
            long result = DB.update("Class_details", contentValues, "uniqueID_assignment =?", new String[]{uniqueID_assignment});
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
    public Cursor getAssignmentData(String uniqueID) {
        SQLiteDatabase DB = this.getWritableDatabase();
        return DB.rawQuery("Select * from Class_details where uniqueID_class=?",new String[]{uniqueID}, null);
    }

}