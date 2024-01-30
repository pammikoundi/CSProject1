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

public class UserActivity extends AppCompatActivity {
    DBHelper DB;
    Button add;
    AlertDialog dialog, updateDialog;
    LinearLayout layout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        add = findViewById(R.id.add);
        layout = findViewById(R.id.container);

        buildDialog();
        buildUpdateDialog();
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
            }
        });

        DB = new DBHelper(this);
        showAllCards();
    }

    private void showAllCards() {
        layout.removeAllViews();
        Cursor res = DB.getalluserdata();
        if (res.getCount() == 0) {
            return;
        }
        while (res.moveToNext()) {
            String user_name = res.getString(0);
            addCard(user_name);
        }
    }
    private void buildUpdateDialog() {
        AlertDialog.Builder updateBuilder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.user_dialog, null);

        final EditText userNameField = view.findViewById(R.id.userNameEdit);
        updateBuilder.setView(view);
        updateBuilder.setTitle("Enter User Information")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String user_name = userNameField.getText().toString();

                        boolean checkUpdateData = DB.updateUserdata(user_name);
                        if (checkUpdateData) {
                            showAllCards();
                            Toast.makeText(UserActivity.this, "Entry Updated", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(UserActivity.this, "Could Not Find User", Toast.LENGTH_SHORT).show();
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
        View view = getLayoutInflater().inflate(R.layout.user_dialog, null);

        final EditText userNameField = view.findViewById(R.id.userNameEdit);

        builder.setView(view);
        builder.setTitle("Enter User Information")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String user_name = userNameField.getText().toString();
                        boolean checkInsertData = DB.insertUserdata(user_name);

                        if (checkInsertData) {
                            showAllCards();
                            Toast.makeText(UserActivity.this, "New Entry Inserted", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(UserActivity.this, "This class may already exist", Toast.LENGTH_SHORT).show();
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

    private void navigateToMainActivity(String userName) {

        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("userName",userName);

        startActivity(intent);
    }

    private void addCard (String user_name){
        final View view = getLayoutInflater().inflate(R.layout.user_card, null);
        TextView nameView = view.findViewById(R.id.userInfo);
        Button delete = view.findViewById(R.id.btnDelete);
        Button showClasses = view.findViewById(R.id.btnShowClasses);

        String cardText = String.format("User Name:"+user_name);
        nameView.setText(cardText);
        showClasses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToMainActivity(user_name);
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean checkDeleteData = DB.deleteuserdata(user_name);
                showAllCards();
                if (checkDeleteData) {
                    Toast.makeText(UserActivity.this, "Entry Deleted", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(UserActivity.this, "Entry Not Deleted", Toast.LENGTH_SHORT).show();
                }
            }
        });


        layout.addView(view);
    }
}