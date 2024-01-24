package com.example.csproject1;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

public class ClassPage extends AppCompatActivity {

    EditText className, classStart, classEnd, classInstructor,classDetails;
    Button submit,update,delete,views;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        className=findViewById(R.id.class_name);
        classInstructor=findViewById(R.id.class_instructor);
        classDetails=findViewById(R.id.class_details);

        submit=findViewById(R.id.btnSubmit);
        update=findViewById(R.id.btnUpdate);
        delete=findViewById(R.id.btnDelete);
        views=  findViewById(R.id.btnView);

        DBHelper DB = new DBHelper(this);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String classNameTXT= className.getText().toString();
                String classInstructorTXT= classInstructor.getText().toString();
                String classDetailsTXT= classDetails.getText().toString();

                boolean checkinsertdata = DB.insertdata(classNameTXT,classInstructorTXT,classDetailsTXT);

                if(checkinsertdata==true){
                    Toast.makeText(ClassPage.this,"New Entry Inserted",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(ClassPage.this,"New Entry Failed to Inserted",Toast.LENGTH_SHORT).show();
                }

            }
        });
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String classNameTXT= className.getText().toString();
                String classInstructorTXT= classInstructor.getText().toString();
                String classDetailsTXT= classDetails.getText().toString();

                boolean checkupdatedata = DB.updatedata(classNameTXT,classInstructorTXT,classDetailsTXT);

                if(checkupdatedata==true){
                    Toast.makeText(ClassPage.this,"Entry Updated",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(ClassPage.this,"Entry Failed to Update",Toast.LENGTH_SHORT).show();
                }

            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String classNameTXT= className.getText().toString();
                String classInstructorTXT= classInstructor.getText().toString();
                String classDetailsTXT= classDetails.getText().toString();

                boolean checkdeletedata = DB.deletedata(classNameTXT);

                if(checkdeletedata==true){
                    Toast.makeText(ClassPage.this,"Deleted Data",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(ClassPage.this,"Data Failed To Delete",Toast.LENGTH_SHORT).show();
                }
            }
        });

        views.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cursor res = DB.getdata();
                if (res.getCount() == 0) {
                    Toast.makeText(ClassPage.this, "NoEntries", Toast.LENGTH_SHORT).show();
                }
                StringBuffer buffer = new StringBuffer();
                while (res.moveToNext()) {
                    buffer.append("Class Name: "+res.getString(0)+"\n");
                    buffer.append("Class Instructor: "+res.getString(1)+"\n");
                    buffer.append("Class Details: "+res.getString(2)+"\n\n");
                }
                AlertDialog.Builder builder=new AlertDialog.Builder(ClassPage.this);
                builder.setCancelable(true);
                builder.setTitle("Classes for the day");
                builder.setMessage(buffer.toString());
                builder.show();
            }
        });
    }
}