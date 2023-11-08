package com.example.expirypal;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class signup extends AppCompatActivity {
    private EditText usernameEditText; // Declare EditText for the username
    private EditText passwordEditText, cpassword; // Declare EditTexts for password and confirm password
    private EditText emailEditText; // Declare EditText for email
    private CheckBox termsCheckBox, showp, showcp; // Declare checkboxes for terms, show password, and show confirm password

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup); // Set the content view to the signup layout

        // Initialize views
        usernameEditText = findViewById(R.id.etUsername); // Find the username EditText by its ID
        passwordEditText = findViewById(R.id.etPass); // Find the password EditText by its ID
        cpassword = findViewById(R.id.etConfirmP); // Find the confirm password EditText by its ID
        emailEditText = findViewById(R.id.etEmel); // Find the email EditText by its ID
        termsCheckBox = findViewById(R.id.cbTerms); // Find the terms checkbox by its ID
        showp = findViewById(R.id.shp); // Find the show password checkbox by its ID
        showcp = findViewById(R.id.shcp); // Find the show confirm password checkbox by its ID

        // Set a click listener for the back button
        ImageButton back = findViewById(R.id.bbtn);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Navigate back to the main activity
                Intent mainIntent = new Intent(signup.this, MainActivity.class);
                startActivity(mainIntent);
            }
        });

        // Set a click listener for the sign-up button
        Button signUpButton = findViewById(R.id.btnSgp);
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get user input
                String username = usernameEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                String confirmPassword = cpassword.getText().toString();
                String email = emailEditText.getText().toString();
                boolean acceptedTerms = termsCheckBox.isChecked();

                // Validate input
                if (username.isEmpty() || password.isEmpty() || email.isEmpty()) {
                    // Display an error message if any field is empty
                    Toast.makeText(signup.this, "Please fill in all fields.", Toast.LENGTH_SHORT).show();
                } else if (!acceptedTerms) {
                    // Display an error message if terms are not accepted
                    Toast.makeText(signup.this, "Please accept the Terms & Conditions.", Toast.LENGTH_SHORT).show();
                } else if (!password.equals(confirmPassword)) {
                    // Display an error message if passwords don't match
                    Toast.makeText(signup.this, "Confirm password must be the same as the password.", Toast.LENGTH_SHORT).show();
                } else {
                    // Insert data into the database
                    DatabaseHelper dbHelper = new DatabaseHelper(signup.this);
                    SQLiteDatabase db = dbHelper.getWritableDatabase();

                    ContentValues values = new ContentValues();
                    values.put(DatabaseHelper.COLUMN_USERNAME, username);
                    values.put(DatabaseHelper.COLUMN_PASSWORD, password);
                    values.put(DatabaseHelper.COLUMN_EMAIL, email);

                    long newRowId = db.insert(DatabaseHelper.TABLE_NAME_USERS, null, values);

                    if (newRowId != -1) {
                        // Registration successful
                        Toast.makeText(signup.this, "Registration successful.", Toast.LENGTH_SHORT).show();
                        Intent registersuc = new Intent(signup.this, MainActivity.class);
                        startActivity(registersuc);
                    } else {
                        // Registration failed
                        Toast.makeText(signup.this, "Registration failed. Please try again.", Toast.LENGTH_SHORT).show();
                        // Handle the error
                    }

                    // Close the database connection
                    db.close();
                }
            }
        });

        // Set an OnCheckedChangeListener for the show password checkbox
        showp.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // Toggle password visibility
                if (!isChecked) {
                    passwordEditText.setTransformationMethod(PasswordTransformationMethod.getInstance());
                } else {
                    passwordEditText.setTransformationMethod(null);
                }
            }
        });

        // Set an OnCheckedChangeListener for the show confirm password checkbox
        showcp.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // Toggle confirm password visibility
                if (!isChecked) {
                    cpassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                } else {
                    cpassword.setTransformationMethod(null);
                }
            }
        });
    }
}
