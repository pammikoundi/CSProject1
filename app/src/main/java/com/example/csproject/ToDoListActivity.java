package com.example.csproject;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.sql.Time;

public class ToDoListActivity extends AppCompatActivity {
    DBHelper DB;
    Button addAssignment,addExam, backButton, sortClass, sortDate;
    AlertDialog assignmentDialog,examDialog, examUpdateDialog;
    LinearLayout incompleteLayout, completeLayout;

    String className, currentExamName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_list);

        Intent intent = getIntent();
        className = intent.getStringExtra("className");

        addAssignment = findViewById(R.id.activity_add_assignment);
        addExam = findViewById(R.id.activity_add_exam);
        backButton = findViewById(R.id.activity_back);
        incompleteLayout = findViewById(R.id.activity_assignment_incomplete_list_container);
        completeLayout = findViewById(R.id.activity_assignment_complete_list_container);
        sortClass = findViewById(R.id.activity_sort_assignment_class);
        sortDate = findViewById(R.id.activity_sort_assignment_due_date);

        currentExamName =null;

        buildAssignmentDialog();
        buildExamUpdateDialog();
        buildExamDialog();

        addAssignment.setOnClickListener(v -> assignmentDialog.show());
        addExam.setOnClickListener(v -> examDialog.show());

        backButton.setOnClickListener(v -> finish());
        sortClass.setOnClickListener(v -> showCards("class_name"));
        sortDate.setOnClickListener(v -> showCards("due_date"));

        DB = new DBHelper(this);
        showCards("due_date");

    }
    private void navigateAssignmentActivity(String assignment_name) {
        Intent intent = new Intent(ToDoListActivity.this, AssignmentActivity.class);
        intent.putExtra("assignmentName", assignment_name);
        startActivity(intent);
    }
    private void showCards(String sortBy) {
        incompleteLayout.removeAllViews();
        completeLayout.removeAllViews();

        Cursor res = DB.getsortedassignmentdata(className, sortBy);
        Cursor exams = DB.getsortedexamdata(className, sortBy);

        if (res.getCount() > 0) {
            while (res.moveToNext()) {
                String assignment_name = res.getString(0);
                Cursor details = DB.getassignmentinfodata(assignment_name);
                String isComplete = "Not Complete";
                while (details.moveToNext()) {
                    isComplete = details.getString(6);
                }
                addAssignmentCard(assignment_name, isComplete);
            }
        }

        if (exams.getCount() > 0) {
            while (exams.moveToNext()) {
                String exam_name = exams.getString(0);
                String exam_course = exams.getString(1);
                String exam_date = exams.getString(2);
                String exam_location = exams.getString(3);
                String exam_time = exams.getString(4);
                addExamCard(exam_name, exam_course, exam_date, exam_location, exam_time);
            }
        }
    }

    private void buildAssignmentDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.todo_dialog, null);

        final EditText assignmentNameField = view.findViewById(R.id.dialog_assignmentNameEdit);
        final EditText assignmentTypeField = view.findViewById(R.id.dialog_assignmentTypeEdit);
        final EditText assignmentLocationField = view.findViewById(R.id.dialog_assignmentLocationEdit);
        final DatePicker dueDateField = view.findViewById(R.id.dialog_dueDateEdit);
        final EditText progressField = view.findViewById(R.id.dialog_progressEdit);

        builder.setView(view);
        builder.setTitle("Enter Assignment Information")
                .setPositiveButton("OK", (dialog, which) -> {
                    String assignment_name = assignmentNameField.getText().toString();
                    String assignment_type = assignmentTypeField.getText().toString();
                    String assignment_class = className;
                    String assignment_location = assignmentLocationField.getText().toString();
                    String due_date = (dueDateField.getMonth()+1)+"/"+dueDateField.getDayOfMonth()+"/"+dueDateField.getYear();
                    String progress = progressField.getText().toString();
                    String complete="Not Complete";

                    boolean checkInsertData = DB.insertassignmentdata(assignment_name, assignment_type, assignment_class, assignment_location, due_date, progress, complete);
                    if (checkInsertData) {
                        showCards("due_date");
                        Toast.makeText(ToDoListActivity.this, "New Entry Inserted", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(ToDoListActivity.this, "This class may already exist", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", (dialog, which) -> {
                    // Handle cancel if needed
                });
        assignmentDialog = builder.create();
    }

    private void buildExamDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.exam_dialog, null);

        final EditText examNameField = view.findViewById(R.id.dialog_examNameEdit);
        final EditText examLocationField = view.findViewById(R.id.dialog_examLocationEdit);
        final DatePicker dateField = view.findViewById(R.id.dialog_dateEdit);
        final TimePicker examTimeField = view.findViewById(R.id.dialog_startTime);

        builder.setView(view);
        builder.setTitle("Enter Exam Information")
                .setPositiveButton("OK", (dialog, which) -> {
                    String startHourFormat=null;
                    int startHour=0;
                    if(examTimeField.getHour()<12){
                        startHourFormat="AM";
                        startHour=examTimeField.getHour();
                    }
                    else{
                        startHourFormat="PM";
                        startHour=examTimeField.getHour()-12;
                    }
                    String startMinFormat=String.format("%02d",examTimeField.getMinute());

                    String exam_name = examNameField.getText().toString();
                    String exam_course = className;
                    String exam_location = examLocationField.getText().toString();
                    String exam_date = (dateField.getMonth()+1)+"/"+dateField.getDayOfMonth()+"/"+dateField.getYear();
                    String startTimeText = startHour+":"+startMinFormat+" "+startHourFormat;

                    boolean checkInsertData = DB.insertexamdata(exam_name,exam_course,exam_date,exam_location,startTimeText);
                    if (checkInsertData) {
                        showCards("due_date");
                        Toast.makeText(ToDoListActivity.this, "New Entry Inserted", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(ToDoListActivity.this, "This exam may already exist", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", (dialog, which) -> {
                    // Handle cancel if needed
                });
        examDialog = builder.create();
    }

    private void buildExamUpdateDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.exam_update_dialog, null);
        final EditText examLocationField = view.findViewById(R.id.dialog_examLocationEdit);
        final DatePicker dateField = view.findViewById(R.id.dialog_dateEdit);
        final TimePicker examTimeField = view.findViewById(R.id.dialog_startTime);

        builder.setView(view);
        builder.setTitle("Enter Exam Information")
                .setPositiveButton("OK", (dialog, which) -> {
                    String startHourFormat=null;
                    int startHour=0;
                    if(examTimeField.getHour()<12){
                        startHourFormat="AM";
                        startHour=examTimeField.getHour();
                    }
                    else{
                        startHourFormat="PM";
                        startHour=examTimeField.getHour()-12;
                    }
                    String startMinFormat=String.format("%02d",examTimeField.getMinute());
                    String exam_course = className;
                    String exam_location = examLocationField.getText().toString();
                    String exam_date = (dateField.getMonth()+1)+"/"+dateField.getDayOfMonth()+"/"+dateField.getYear();
                    String startTimeText = startHour+":"+startMinFormat+" "+startHourFormat;

                    boolean checkInsertData = DB.updateexamdata(currentExamName,exam_course,exam_date,exam_location,startTimeText);
                    if (checkInsertData) {
                        Toast.makeText(ToDoListActivity.this, "New Entry Inserted", Toast.LENGTH_SHORT).show();
                        showCards("due_date");
                    } else {
                        Toast.makeText(ToDoListActivity.this, "This exam may already exist", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", (dialog, which) -> {
                    // Handle cancel if needed
                });
        examUpdateDialog = builder.create();
    }

    private void addAssignmentCard(String assignment_name, String isComplete) {
        final View view = getLayoutInflater().inflate(R.layout.todo_card,null);
        TextView nameView = view.findViewById(R.id.card_assignmentNameText);
        Button viewDetails = view.findViewById(R.id.card_btnviewDetails);
        Button deleteButton = view.findViewById(R.id.card_btnDelete);
        CheckBox complete = view.findViewById(R.id.card_checkComplete);
        if(isComplete.equals("Complete")){
            complete.setChecked(true);
        }
        if (complete.isChecked())
        {
            completeLayout.addView(view);
        }
        else
        {
            incompleteLayout.addView(view);
        }
        String cardText = String.format("Assignment Name: " + assignment_name);
        nameView.setText(cardText);
        deleteButton.setOnClickListener(v -> {
            boolean checkDeleteData = DB.deleteassignmentdata(assignment_name);
            if (checkDeleteData) {
                Toast.makeText(ToDoListActivity.this, "Entry Deleted", Toast.LENGTH_SHORT).show();
                showCards("due_date");
            } else {
                Toast.makeText(ToDoListActivity.this, "Entry Not Deleted", Toast.LENGTH_SHORT).show();
            }
        });
        viewDetails.setOnClickListener(v -> {
                    navigateAssignmentActivity(assignment_name);
                }
        );
        complete.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                String assignment_type = null,assignment_class=null,assignment_location=null,due_date=null,progress=null,isComplete=null;

                Cursor res;
                res = DB.getassignmentinfodata(assignment_name);
                while (res.moveToNext()) {
                    assignment_type = res.getString(1);
                    assignment_class = res.getString(2);
                    assignment_location = res.getString(3);
                    due_date = res.getString(4);
                    progress = res.getString(5);
                }
                if (buttonView.isChecked()) {
                    isComplete="Complete";
                }
                else
                {
                    isComplete= "Not Complete";
                }
                DB.updateassignmentdata(assignment_name, assignment_type, assignment_class, assignment_location, due_date, progress, isComplete);
                showCards("due_date");
            }
        });
    }
    private void addExamCard(String exam_name, String exam_course, String exam_date, String exam_location, String exam_time) {
        final View view = getLayoutInflater().inflate(R.layout.exam_card, null);
        TextView nameView = view.findViewById(R.id.card_examNameText);
        Button updateDetails = view.findViewById(R.id.card_btnUpdate);
        CheckBox complete = view.findViewById(R.id.card_checkComplete);

        String cardText = String.format("Exam Name: " + exam_name + "\nExam Course: " + exam_course + "\nExam Date: " + exam_date + "\nExam Location" + exam_location + "\nExam Time" + exam_time);
        nameView.setText(cardText);

        incompleteLayout.addView(view);

        updateDetails.setOnClickListener(v -> {
            currentExamName = exam_name;
            buildExamUpdateDialog();
            examUpdateDialog.show();
        });

        complete.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                boolean checkDeleteData = DB.deleteexamdata(exam_name);
                if (checkDeleteData) {
                    Toast.makeText(ToDoListActivity.this, "Entry Deleted", Toast.LENGTH_SHORT).show();
                    showCards("due_date");
                } else {
                    Toast.makeText(ToDoListActivity.this, "Entry Not Deleted", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}