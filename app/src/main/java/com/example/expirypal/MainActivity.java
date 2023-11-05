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
    private EditText username, password;
    private Button login, signup;
    private CheckBox showButton;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        dbHelper = new DatabaseHelper(this);

        // Initialize UI elements
        username = findViewById(R.id.etUsern);
        password = findViewById(R.id.etPassword);
        login = findViewById(R.id.lgnbtn);
        signup = findViewById(R.id.sgpbtn);
        showButton = findViewById(R.id.showcb);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String usern = username.getText().toString();
                String pass = password.getText().toString();

                boolean isAuthenticated = authenticateUser(usern, pass);

                if (isAuthenticated) {
                    Toast toast1 = Toast.makeText(MainActivity.this, "Login Successful", Toast.LENGTH_SHORT);
                    toast1.setGravity(Gravity.LEFT, 0, 780);
                    toast1.show();

                    // Authentication successful, navigate to the next activity
                    Intent nextpageIntent = new Intent(MainActivity.this, home.class);
                    nextpageIntent.putExtra("username", usern); // Pass the username as an extra
                    startActivity(nextpageIntent);

                } else {
                    Toast toast = Toast.makeText(MainActivity.this, "Incorrect Username/Password", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.LEFT, 0, 780);
                    toast.show();
                }
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // To the signup page
                Intent signupIntent = new Intent(MainActivity.this, signup.class);
                startActivity(signupIntent);
            }
        });

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

    private boolean authenticateUser(String username, String password) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] projection = {DatabaseHelper.COLUMN_USERNAME, DatabaseHelper.COLUMN_PASSWORD};
        String selection = DatabaseHelper.COLUMN_USERNAME + " = ? AND " + DatabaseHelper.COLUMN_PASSWORD + " = ?";
        String[] selectionArgs = {username, password};

        Cursor cursor = db.query(
                DatabaseHelper.TABLE_NAME_USERS,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        boolean isAuthenticated = cursor.getCount() > 0;
        cursor.close();

        return isAuthenticated;
    }
}
