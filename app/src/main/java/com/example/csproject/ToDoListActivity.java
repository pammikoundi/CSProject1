package com.example.csproject;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.content.DialogInterface;

public class ToDoListActivity extends AppCompatActivity {
    DBHelper DB;
    Button add, backButton;
    AlertDialog dialog, updateDialog;
    LinearLayout layout;

    String className;
    String current_assignment_name=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_list);

        Intent intent = getIntent(); // gets the previously created intent
        className = intent.getStringExtra("className"); // will return "FirstKeyValue"

        add = findViewById(R.id.add);
        backButton = findViewById(R.id.back);
        layout = findViewById(R.id.container);

        buildDialog();
        buildUpdateDialog();

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
            }
        });


        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        DB = new DBHelper(this);
        showCards();
    }


    private void showCards() {
        layout.removeAllViews();
        Cursor res;
        if(className!=null) {
             res = DB.getassignmentdata(className);
        }
        else {
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

            addCard(assignment_name, assignment_type, className, assignment_location, due_date, progress, complete);
        }

    }

    private void buildUpdateDialog() {
        AlertDialog.Builder updateBuilder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.todo_update_dialog, null);

        final EditText assignmentTypeField = view.findViewById(R.id.assignmentTypeEdit);
        final EditText assignmentLocationField = view.findViewById(R.id.assignmentLocationEdit);
        final EditText dueDateField = view.findViewById(R.id.dueDateEdit);
        final EditText progressField = view.findViewById(R.id.progressEdit);
        final EditText completeField = view.findViewById(R.id.completeEdit);

        updateBuilder.setView(view);

        updateBuilder.setTitle("Enter Class Information")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String assignment_name = current_assignment_name;
                        String assignment_type = assignmentTypeField.getText().toString();
                        String assignment_class= className;
                        String assignment_location = assignmentLocationField.getText().toString();
                        String due_date = dueDateField.getText().toString();
                        String progress = progressField.getText().toString();
                        String complete = completeField.getText().toString();

                        boolean checkUpdateData = DB.updateassignmentdata(assignment_name, assignment_type, assignment_class, assignment_location, due_date, progress, complete);
                        if (checkUpdateData) {
                            showCards();
                            Toast.makeText(ToDoListActivity.this, "Entry Updated", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(ToDoListActivity.this, "Could Not Find Class to Update", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Handle cancel if needed
                    }
                });

        updateDialog = updateBuilder.create();
    }
    private void buildDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.todo_dialog, null);

        final EditText assignmentNameField = view.findViewById(R.id.assignmentNameEdit);
        final EditText assignmentTypeField = view.findViewById(R.id.assignmentTypeEdit);
        final EditText assignmentLocationField = view.findViewById(R.id.assignmentLocationEdit);
        final EditText dueDateField = view.findViewById(R.id.dueDateEdit);
        final EditText progressField = view.findViewById(R.id.progressEdit);
        final EditText completeField = view.findViewById(R.id.completeEdit);

        builder.setView(view);
        builder.setTitle("Enter Class Information")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String assignment_name = assignmentNameField.getText().toString();
                        String assignment_type = assignmentTypeField.getText().toString();
                        String assignment_class= className;
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
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Handle cancel if needed
                    }
                });

        dialog = builder.create();
    }

    private void addCard (String assignment_name, String assignment_type, String assignment_class, String assignment_location,String due_date, String progress, String complete){
        final View view = getLayoutInflater().inflate(R.layout.todo_card, null);
        TextView nameView = view.findViewById(R.id.classInfo);
        Button delete = view.findViewById(R.id.btnDelete);
        Button update = view.findViewById(R.id.btnUpdate);

        String cardText = String.format("Assignment Name: %s\nAssignment Type: %s\nLocation: %s\nDue Date: %s\nProgress: %s\nComplete: %s",
                assignment_name, assignment_type, assignment_class, assignment_location, due_date, progress, complete);

        nameView.setText(cardText);

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layout.removeView(view);
                boolean checkDeleteData = DB.deleteassignmentdata(assignment_name);
                if (checkDeleteData) {
                    Toast.makeText(ToDoListActivity.this, "Entry Deleted", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ToDoListActivity.this, "Entry Not Deleted", Toast.LENGTH_SHORT).show();
                }
            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                current_assignment_name=assignment_name;
                updateDialog.show();
            }
        });

        layout.addView(view);
    }
}