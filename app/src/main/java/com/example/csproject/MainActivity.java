package com.example.csproject;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    DBHelper DB;
    Button addBtn, todoBtn;
    AlertDialog dialog, updateDialog;
    LinearLayout list_layout;
    String userName, currentClassName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent(); // gets the previously created intent
        userName = intent.getStringExtra("userName"); // will return "FirstKeyValue"
        currentClassName =null;

        Button backButton = findViewById(R.id.back);

        addBtn = findViewById(R.id.activity_add_class);
        todoBtn = findViewById(R.id.activity_nav_to_do_list);
        list_layout = findViewById(R.id.container);

        buildUpdateDialog();
        buildDialog();

        backButton.setOnClickListener(v -> finish());

        addBtn.setOnClickListener(v -> dialog.show());

        todoBtn.setOnClickListener(v -> {
            Log.d("MainActivity", "To Do List button clicked");
            navigateToToDoListActivity(null);
        });

        DB = new DBHelper(this);
        showAllCards();
    }

    private void navigateToToDoListActivity(String className) {
        Intent intent = new Intent(MainActivity.this, ToDoListActivity.class);
        intent.putExtra("className",className);
        startActivity(intent);
    }

    private void showAllCards() {
        list_layout.removeAllViews();
        Cursor res = DB.getclassdata(userName);
        if (res.getCount() == 0) {
            return;
        }
        while (res.moveToNext()) {

            String nameText = res.getString(0);
            String instructorText = res.getString(2);
            String classSectionText = res.getString(3);
            String classLocationText = res.getString(4);
            String repeatText = res.getString(5);
            String startTimeText = res.getString(6);
            String endTimeText = res.getString(7);

            addCard(nameText, instructorText, classSectionText, classLocationText, repeatText, startTimeText, endTimeText);
        }
    }

    private void buildUpdateDialog() {
        AlertDialog.Builder updateBuilder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.class_update_dialog, null);
        final EditText instructor = view.findViewById(R.id.dialog_instructorEdit);
        final EditText classSection = view.findViewById(R.id.dialog_classSectionEdit);
        final EditText classLocation = view.findViewById(R.id.dialog_classLocationEdit);
        final EditText repeat = view.findViewById(R.id.dialog_repeatEdit);
        final EditText startTime = view.findViewById(R.id.dialog_startTimeEdit);
        final EditText endTime = view.findViewById(R.id.dialog_endTimeEdit);

        updateBuilder.setView(view);
        updateBuilder.setTitle("Enter Class Information")
                .setPositiveButton("OK", (dialog, which) -> {
                    String instructorText = instructor.getText().toString();
                    String classSectionText = classSection.getText().toString();
                    String classLocationText = classLocation.getText().toString();
                    String repeatText = repeat.getText().toString();
                    String startTimeText = startTime.getText().toString();
                    String endTimeText = endTime.getText().toString();

                    boolean checkUpdateData = DB.updateclassdata(currentClassName, instructorText, classSectionText, classLocationText, repeatText, startTimeText, endTimeText);

                    if (checkUpdateData) {
                        showAllCards();
                        Toast.makeText(MainActivity.this, "Entry Updated", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(MainActivity.this, "Could Not Find Class to Update", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", (dialog, which) -> {
                    // Handle cancel if needed
                });

        updateDialog = updateBuilder.create();
    }
    private void buildDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.class_dialog, null);

        final EditText name = view.findViewById(R.id.dialog_nameEdit);
        final EditText instructor = view.findViewById(R.id.dialog_instructorEdit);
        final EditText classSection = view.findViewById(R.id.dialog_classSectionEdit);
        final EditText classLocation = view.findViewById(R.id.dialog_classLocationEdit);
        final EditText repeat = view.findViewById(R.id.dialog_repeatEdit);
        final EditText startTime = view.findViewById(R.id.dialog_startTimeEdit);
        final EditText endTime = view.findViewById(R.id.dialog_endTimeEdit);

        builder.setView(view);
        builder.setTitle("Enter Class Information")
                .setPositiveButton("OK", (dialog, which) -> {

                    String userActivity = userName;
                    String nameText = name.getText().toString();
                    String instructorText = instructor.getText().toString();
                    String classSectionText = classSection.getText().toString();
                    String classLocationText = classLocation.getText().toString();
                    String repeatText = repeat.getText().toString();
                    String startTimeText = startTime.getText().toString();
                    String endTimeText = endTime.getText().toString();
                    currentClassName =nameText;
                    boolean checkInsertData = DB.insertclassdata(nameText,userActivity,instructorText, classSectionText, classLocationText, repeatText, startTimeText, endTimeText);

                    if (checkInsertData) {
                        showAllCards();
                        Toast.makeText(MainActivity.this, "New Entry Inserted", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(MainActivity.this, "This class may already exist", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", (dialog, which) -> {
                    // Handle cancel if needed
                });

        dialog = builder.create();
    }

    private void addCard(String name,String instructor, String class_section, String class_location, String repeat, String start_time, String end_time) {
        final View view = getLayoutInflater().inflate(R.layout.class_card, null);
        TextView nameView = view.findViewById(R.id.card_classInfoText);
        Button delete = view.findViewById(R.id.card_btnDelete);
        Button update = view.findViewById(R.id.card_btnUpdate);
        Button assignments = view.findViewById(R.id.card_nav_btnAssignments);

        String cardText = String.format("Class Name: %s\nInstructor: %s\nClass Section: %s\nClass Location: %s\nRepeat: %s\nStart Time: %s\nEnd Time: %s",
                name, instructor, class_section, class_location, repeat, start_time, end_time);

        nameView.setText(cardText);

        assignments.setOnClickListener(v -> {
            Log.d("MainActivity", "To Do List button clicked");
            navigateToToDoListActivity(name);
        });

        delete.setOnClickListener(v -> {
            list_layout.removeView(view);
            boolean checkDeleteData = DB.deleteclassdata(name);
            if (checkDeleteData) {
                Toast.makeText(MainActivity.this, "Entry Deleted", Toast.LENGTH_SHORT).show();

                Cursor res = DB.getassignmentdata(name);
                if (res.getCount() == 0) {
                    return;
                }
                while (res.moveToNext()) {
                    String assignment_name = res.getString(0);
                    DB.deleteassignmentdata(assignment_name);
                }
            } else {
                Toast.makeText(MainActivity.this, "Entry Not Deleted", Toast.LENGTH_SHORT).show();
            }
        });
        update.setOnClickListener(v -> {
            currentClassName =name;
            updateDialog.show();
        });

        list_layout.addView(view);
    }
}
