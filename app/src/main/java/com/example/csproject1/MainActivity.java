package com.example.csproject1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.fragment.NavHostFragment;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.TextView;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    Button nextPage;
    TextView class_View=findViewById(R.id.classView);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DBHelper DB = new DBHelper(this);
        //Cursor res = DB.getdata();
        //StringBuffer buffer = new StringBuffer();
/*
        if (res.getCount() == 0) {
            Toast.makeText(MainActivity.this, "NoEntries", Toast.LENGTH_SHORT).show();
        }
        else {
            while (res.moveToNext()) {
                buffer.append("Class Name: " + res.getString(0) + "\n");
                buffer.append("Class Instructor: " + res.getString(1) + "\n");
                buffer.append("Class Details: " + res.getString(2) + "\n\n");
            }
            class_View.setText(buffer.toString());
        }

 */
        setContentView(R.layout.activity_main);
        CalendarView calendarView = findViewById(R.id.calendarView);
        nextPage=findViewById(R.id.buttonToNextScreen);

        /*
        nextPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    MainActivity.actionMainActivityToClassPage action = MainActivity.actionMainActivityToClassPage();
                    NavHostFragment.findNavController(FirstFragment.this).navigate(action);
                }
        });
*/
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
              // class_View.setText(buffer.toString());

            }
        });
    }
}