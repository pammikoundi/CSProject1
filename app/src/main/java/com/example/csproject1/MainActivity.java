package com.example.csproject1;

import android.os.Bundle;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

public class MainActivity extends Fragment {

    Button nextPage;
    TextView class_View = findViewById(R.id.classView);

    @Override
    public void onCreate(Bundle savedInstanceState) {
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