package com.example.csproject;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class UserActivity extends AppCompatActivity {
    DBHelper DB;
    Button add, submit;
    EditText userName, password;
    AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        DB = new DBHelper(this);
        add = findViewById(R.id.activty_add_user);
        userName = findViewById(R.id.activity_userNameEdit);
        password = findViewById(R.id.activity_userPasswordEdit);
        submit = findViewById(R.id.activity_submit);
        buildDialog();
        add.setOnClickListener(v -> dialog.show());

        submit.setOnClickListener(v -> {
                    Cursor res = DB.getuserdata(userName.getText().toString());
                    if (res.getCount() == 0) {
                        Toast.makeText(UserActivity.this, "No Entry", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    while (res.moveToNext()) {
                        String passwordDB = res.getString(1);
                        if (password.getText().toString().equals(passwordDB)) {
                            navigateToMainActivity(userName.getText().toString());
                        } else {
                            Toast.makeText(UserActivity.this, "Login Failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                );

    }

    private void buildDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.user_dialog, null);

        final EditText userNameField = view.findViewById(R.id.dialog_userNameEdit);
        final EditText userPasswordField = view.findViewById(R.id.dialog_userPasswordEdit);

        builder.setView(view);
        builder.setTitle("Enter User Information")
                .setPositiveButton("OK", (dialog, which) -> {
                    String user_name = userNameField.getText().toString();
                    String password = userPasswordField.getText().toString();
                    boolean checkInsertData = DB.insertUserdata(user_name,password);

                    if (checkInsertData) {
                        Toast.makeText(UserActivity.this, "New Entry Inserted", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(UserActivity.this, "This class may already exist", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", (dialog, which) -> {
                });

        dialog = builder.create();
    }

    private void navigateToMainActivity(String userName) {

        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("userName",userName);

        startActivity(intent);
    }


}