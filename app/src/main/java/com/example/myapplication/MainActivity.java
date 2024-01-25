package com.example.myapplication;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.csprojectcalapp.R;

public class MainActivity extends AppCompatActivity {

    Button add;
    AlertDialog addDialog;

    LinearLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        add = findViewById(R.id.add);
        layout = findViewById(R.id.container);
        addCard("Welcome");
        buildAddDialog();

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addDialog.show();
            }
        });
    }

    private void buildAddDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.create_dialog, null);

        final EditText name = view.findViewById(R.id.nameEdit);
        final EditText instructor = view.findViewById(R.id.instructorEdit);
        final EditText classSection = view.findViewById(R.id.classSectionEdit);
        final EditText classLocation = view.findViewById(R.id.classLocationEdit);
        final EditText repeat = view.findViewById(R.id.repeatEdit);
        final EditText startTime = view.findViewById(R.id.startTimeEdit);
        final EditText endTime = view.findViewById(R.id.endTimeEdit);

        builder.setView(view);
        builder.setTitle("Enter name")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        addCard(
                                ("Class Name: "+name.getText().toString())+
                                        ("\nInstructor: "+instructor.getText().toString())+
                                        ("\nClass Section: "+classSection.getText().toString())+
                                        ("\nClass Location: "+classLocation.getText().toString())+
                                        ("\nRepeat: "+repeat.getText().toString())+
                                        ("\nStart Time: "+startTime.getText().toString())+
                                        ("\nEnd Time: "+endTime.getText().toString()));
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        addDialog = builder.create();
    }
    private void addCard(String name) {
        final View view = getLayoutInflater().inflate(R.layout.card, null);

        TextView nameView = view.findViewById(R.id.classInfo);
        Button update = view.findViewById(R.id.update);

        nameView.setText(name);

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layout.removeView(view);
                addDialog.show();
            }

        });

        layout.addView(view);
    }
}
