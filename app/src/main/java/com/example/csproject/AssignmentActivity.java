package com.example.csproject;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class AssignmentActivity extends AppCompatActivity {
    DBHelper DB;
    Button updateButton, backButton, deleteButton;
    AlertDialog updateDialog;
    String assignment_name, assignment_type, assignment_class, assignment_location, due_date, progress, complete;
    TextView assignmentInfo;
    String current_assignment_name=null;
    String current_assignment_class=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assignment);

        assignmentInfo = findViewById(R.id.activity_assignmentInfoText);
        deleteButton = findViewById(R.id.activity_btnDelete);
        updateButton = findViewById(R.id.activity_nav_btnUpdate);
        backButton = findViewById(R.id.activity_btnBack);

        Intent intent = getIntent();
       String assignmentName=intent.getStringExtra("assignmentName");

        DB = new DBHelper(this);

        Cursor res;
        res = DB.getassignmentinfodata(assignmentName);

        while (res.moveToNext()) {
            assignment_name = res.getString(0);
            assignment_type = res.getString(1);
            assignment_class = res.getString(2);
            assignment_location = res.getString(3);
            due_date = res.getString(4);
            progress = res.getString(5);
            complete = res.getString(6);
        }
        current_assignment_name=assignment_name;
        current_assignment_class=assignment_class;

        assignmentInfo.setText(String.format("Assignment Name: %s\nAssignment Class: %s\nAssignment Type: %s\nLocation: %s\nDue Date: %s\nProgress: %s\nComplete: %s",
                assignment_name, assignment_class, assignment_type, assignment_location, due_date, progress, complete));

        buildUpdateDialog();

        updateButton.setOnClickListener(v -> updateDialog.show());
        backButton.setOnClickListener(v -> finish());

        deleteButton.setOnClickListener(v -> {

            boolean checkDeleteData = DB.deleteassignmentdata(current_assignment_name);
            if (checkDeleteData) {
                Toast.makeText(AssignmentActivity.this, "Entry Deleted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(AssignmentActivity.this, "Entry Not Deleted", Toast.LENGTH_SHORT).show();
            }
            finish();
        });

    }

    private void buildUpdateDialog() {
        AlertDialog.Builder updateBuilder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.todo_update_dialog, null);

        final EditText assignmentTypeField = view.findViewById(R.id.dialog_assignmentTypeEdit);
        final EditText assignmentLocationField = view.findViewById(R.id.dialog_assignmentLocationEdit);
        final DatePicker dueDateField = view.findViewById(R.id.dialog_dueDateEdit);
        final EditText progressField = view.findViewById(R.id.dialog_progressEdit);

        updateBuilder.setView(view);

        updateBuilder.setTitle("Enter Class Information")
                .setPositiveButton("OK", (dialog, which) -> {

                    String assignment_type = assignmentTypeField.getText().toString();
                    String assignment_location = assignmentLocationField.getText().toString();
                    String due_date = (dueDateField.getMonth()+1)+"/"+dueDateField.getDayOfMonth()+"/"+dueDateField.getYear();
                    String progress = progressField.getText().toString();

                    boolean checkUpdateData = DB.updateassignmentdata(current_assignment_name, assignment_type, current_assignment_class, assignment_location, due_date, progress, complete);
                    if (checkUpdateData) {
                        assignmentInfo.setText(String.format("Assignment Name: %s\nAssignment Class: %s\nAssignment Type: %s\nLocation: %s\nDue Date: %s\nProgress: %s\nComplete: %s",
                                assignment_name, assignment_class, assignment_type, assignment_location, due_date, progress, complete));
                        Toast.makeText(AssignmentActivity.this, "Entry Updated", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(AssignmentActivity.this, "Could Not Find Class to Update", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", (dialog, which) -> {
                    // Handle cancel if needed
                });

        updateDialog = updateBuilder.create();
    }

    }
