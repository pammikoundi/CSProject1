package com.example.csproject;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class ToDoListActivity extends AppCompatActivity {
    DBHelper DB;
    Button add, backButton;
    AlertDialog dialog, updateDialog;
    LinearLayout layout;

    String className;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_list);

        Intent intent = getIntent(); // gets the previously created intent
        className = intent.getStringExtra("className"); // will return "FirstKeyValue"

        add = findViewById(R.id.activity_add_assignment);
        backButton = findViewById(R.id.activity_back);
        layout = findViewById(R.id.activity_class_list_container);

        buildDialog();

        add.setOnClickListener(v -> dialog.show());
        backButton.setOnClickListener(v -> finish());

        DB = new DBHelper(this);
        showCards();
    }

    private void navigateAssignmentActivity(String assignment_name) {
        Intent intent = new Intent(ToDoListActivity.this, AssignmentActivity.class);
        intent.putExtra("assignmentName", assignment_name);

        startActivity(intent);
    }

    private void showCards() {
        layout.removeAllViews();
        Cursor res;
        if (className != null) {
            res = DB.getassignmentdata(className);
        } else {
            res = DB.getallassignmentdata();
        }
        if (res.getCount() == 0) {
            return;
        }
        while (res.moveToNext()) {
            String assignment_name = res.getString(0);
            String assignment_type = res.getString(1);
            String assignment_location = res.getString(3);
            String due_date = res.getString(4);
            String progress = res.getString(5);
            String complete = res.getString(6);

            addCard(assignment_name);
        }
    }

    private void buildDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.todo_dialog, null);

        final EditText assignmentNameField = view.findViewById(R.id.dialog_assignmentNameEdit);
        final EditText assignmentTypeField = view.findViewById(R.id.dialog_assignmentTypeEdit);
        final EditText assignmentLocationField = view.findViewById(R.id.dialog_assignmentLocationEdit);
        final EditText dueDateField = view.findViewById(R.id.dialog_dueDateEdit);
        final EditText progressField = view.findViewById(R.id.dialog_progressEdit);
        final EditText completeField = view.findViewById(R.id.dialog_completeEdit);

        builder.setView(view);
        builder.setTitle("Enter Class Information")
                .setPositiveButton("OK", (dialog, which) -> {
                    String assignment_name = assignmentNameField.getText().toString();
                    String assignment_type = assignmentTypeField.getText().toString();
                    String assignment_class = className;
                    String assignment_location = assignmentLocationField.getText().toString();
                    String due_date = dueDateField.getText().toString();
                    String progress = progressField.getText().toString();
                    String complete = completeField.getText().toString();

                    boolean checkInsertData = DB.insertassignmentdata(assignment_name, assignment_type, assignment_class, assignment_location, due_date, progress, complete);
                    if (checkInsertData) {
                        showCards();
                        Toast.makeText(ToDoListActivity.this, "New Entry Inserted", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(ToDoListActivity.this, "This class may already exist", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", (dialog, which) -> {
                    // Handle cancel if needed
                });
        dialog = builder.create();
    }

    private void addCard (String assignment_name) {
        final View view = getLayoutInflater().inflate(R.layout.todo_card, null);
        TextView nameView = view.findViewById(R.id.card_assignmentNameText);
        Button viewDetails = view.findViewById(R.id.card_btnviewDetails);
        CheckBox complete = view.findViewById(R.id.card_checkComplete);
        String cardText = String.format("Assignment Name: " + assignment_name);
        nameView.setText(cardText);

        viewDetails.setOnClickListener(v -> {
            navigateAssignmentActivity(assignment_name);
        }
        );

        layout.addView(view);
    }

}