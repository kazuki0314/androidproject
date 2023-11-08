package com.example.expirypal;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private EditText username, password; // Declare EditText fields for username and password
    private Button login, signup; // Declare login and signup buttons
    private CheckBox showButton; // Declare a checkbox for showing/hiding the password
    private DatabaseHelper dbHelper; // Database helper class for database operations

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login); // Set the content view to the login layout

        dbHelper = new DatabaseHelper(this); // Initialize the database helper

        // Initialize UI elements
        username = findViewById(R.id.etUsern); // Find the username EditText by its ID
        password = findViewById(R.id.etPassword); // Find the password EditText by its ID
        login = findViewById(R.id.lgnbtn); // Find the login button by its ID
        signup = findViewById(R.id.sgpbtn); // Find the signup button by its ID
        showButton = findViewById(R.id.showcb); // Find the show password checkbox by its ID

        // Set a click listener for the login button
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String usern = username.getText().toString(); // Get the entered username
                String pass = password.getText().toString(); // Get the entered password

                boolean isAuthenticated = authenticateUser(usern, pass); // Check if user is authenticated

                if (isAuthenticated) {
                    // Show a success toast message
                    Toast toast1 = Toast.makeText(MainActivity.this, "Login Successful", Toast.LENGTH_SHORT);
                    toast1.setGravity(Gravity.LEFT, 0, 780);
                    toast1.show();

                    // Authentication successful, navigate to the next activity (home)
                    Intent nextpageIntent = new Intent(MainActivity.this, home.class);
                    nextpageIntent.putExtra("username", usern); // Pass the username as an extra to the next activity
                    startActivity(nextpageIntent);
                } else {
                    // Show an error toast message for incorrect username/password
                    Toast toast = Toast.makeText(MainActivity.this, "Incorrect Username/Password", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.LEFT, 0, 780);
                    toast.show();
                }
            }
        });

        // Set a click listener for the signup button
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Navigate to the signup activity
                Intent signupIntent = new Intent(MainActivity.this, signup.class);
                startActivity(signupIntent);
            }
        });

        // Set an OnCheckedChangeListener for the show password checkbox
        showButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // Toggle password visibility
                if (!isChecked) {
                    password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                } else {
                    password.setTransformationMethod(null);
                }
            }
        });
    }

    // Authenticate user by checking their username and password in the database
    private boolean authenticateUser(String username, String password) {
        SQLiteDatabase db = dbHelper.getReadableDatabase(); // Get a readable database
        String[] projection = {DatabaseHelper.COLUMN_USERNAME, DatabaseHelper.COLUMN_PASSWORD};
        String selection = DatabaseHelper.COLUMN_USERNAME + " = ? AND " + DatabaseHelper.COLUMN_PASSWORD + " = ?";
        String[] selectionArgs = {username, password};

        Cursor cursor = db.query(
                DatabaseHelper.TABLE_NAME_USERS, // Table name
                projection, // Columns to retrieve
                selection, // WHERE clause
                selectionArgs, // Selection arguments
                null,
                null,
                null
        );

        boolean isAuthenticated = cursor.getCount() > 0; // Check if a record exists in the result set
        cursor.close();

        return isAuthenticated; // Return whether the user is authenticated
    }
}
