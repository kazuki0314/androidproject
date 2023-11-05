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
    private EditText usernameEditText;
    private EditText passwordEditText,cpassword;
    private EditText emailEditText;
    private CheckBox termsCheckBox, showp, showcp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);

        // Initialize views
        usernameEditText = findViewById(R.id.etUsername);
        passwordEditText = findViewById(R.id.etPass);
        cpassword = findViewById(R.id.etConfirmP);
        emailEditText = findViewById(R.id.etEmel);
        termsCheckBox = findViewById(R.id.cbTerms);
        showp = findViewById(R.id.shp);
        showcp = findViewById(R.id.shcp);

        ImageButton back = findViewById(R.id.bbtn);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Navigate back to the main activity
                Intent mainIntent = new Intent(signup.this, MainActivity.class);
                startActivity(mainIntent);
            }
        });

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

        showcp.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // Toggle password visibility
                if (!isChecked) {
                    cpassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                } else {
                    cpassword.setTransformationMethod(null);
                }
            }
        });
    }
}
