package com.example.csproject1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.TextView;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    EditText className, class_timings, classInstructor,classDetails;
    Button submit,update,delete;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        CalendarView calendarView = findViewById(R.id.calendarView);
        final TextView classList = findViewById(R.id.classView);
        className=findViewById(R.id.class_name);
        classInstructor=findViewById(R.id.class_instructor);
        classDetails=findViewById(R.id.class_details);

        submit=findViewById(R.id.btnSubmit);
        update=findViewById(R.id.btnUpdate);
        delete=findViewById(R.id.btnDelete);

        DBHelper DB = new DBHelper(this);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String classNameTXT= className.getText().toString();
                String classInstructorTXT= classInstructor.getText().toString();
                String classDetailsTXT= classDetails.getText().toString();

                boolean checkinsertdata = DB.insertdata(classNameTXT,classInstructorTXT,classDetailsTXT);

                if(checkinsertdata==true){
                    Toast.makeText(MainActivity.this,"New Entry Inserted",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(MainActivity.this,"New Entry Failed to Inserted",Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(MainActivity.this,"Entry Updated",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(MainActivity.this,"Entry Failed to Update",Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(MainActivity.this,"Deleted Data",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(MainActivity.this,"Data Failed To Delete",Toast.LENGTH_SHORT).show();
                }

            }
        });


        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                classList.setText((month+1)+", "+dayOfMonth+" "+ year);
            }
        });
    }
}