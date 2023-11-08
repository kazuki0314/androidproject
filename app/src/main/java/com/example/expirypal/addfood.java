package com.example.expirypal;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class addfood extends AppCompatActivity {
    // Declare the UI elements
    private EditText foodn, expdate, remdate, quanty, notes;
    private Spinner spinnerFoodCategory;
    private Button addfoodit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_food_items);

        // Initialize UI elements by finding their respective views
        foodn = findViewById(R.id.etfoodn);
        expdate = findViewById(R.id.etexpidate);
        remdate = findViewById(R.id.etremindt);
        quanty = findViewById(R.id.etqty);
        spinnerFoodCategory = findViewById(R.id.sfcategory);
        notes = findViewById(R.id.etNote);
        addfoodit = findViewById(R.id.addfibtn);

        // Create an ArrayAdapter for the food categories spinner
        ArrayAdapter<CharSequence> foodCategoryAdapter = ArrayAdapter.createFromResource(
                this,
                R.array.food_categories,
                android.R.layout.simple_spinner_item
        );
        foodCategoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerFoodCategory.setAdapter(foodCategoryAdapter);

        // Set click listeners for the date input fields
        expdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog(expdate);
            }
        });

        remdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog(remdate);
            }
        });

        // Set a click listener for the "Add" button
        addfoodit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Retrieve user input from the UI elements
                String foodname = foodn.getText().toString();
                String expDateStr = expdate.getText().toString();
                String remDateStr = remdate.getText().toString();
                String quantity = quanty.getText().toString();
                String category = spinnerFoodCategory.getSelectedItem().toString();
                String note = notes.getText().toString();

                // Check if the required fields are filled
                if (foodname.isEmpty() || expDateStr.isEmpty() || remDateStr.isEmpty() || quantity.isEmpty() || category.equals("Select Category")) {
                    showToast("Please fill in all required fields.");
                    return;
                }

                // Open the database and insert food information
                DatabaseHelper dbHelper = new DatabaseHelper(addfood.this);
                SQLiteDatabase db = dbHelper.getWritableDatabase();

                ContentValues foodinfo = new ContentValues();
                foodinfo.put(DatabaseHelper.COLUMN_FOODNAME, foodname);
                foodinfo.put(DatabaseHelper.COLUMN_EDATE, expDateStr);
                foodinfo.put(DatabaseHelper.COLUMN_RDATE, remDateStr);
                foodinfo.put(DatabaseHelper.COLUMN_QUANTITY, quantity);
                foodinfo.put(DatabaseHelper.COLUMN_CATEGORY, category);
                foodinfo.put(DatabaseHelper.COLUMN_NOTE, note);

                long newRowId = db.insert(DatabaseHelper.TABLE_NAME_FOODS, null, foodinfo);

                // Show a toast message based on the insert result
                if (newRowId != -1) {
                    showToast("Food item added successfully!");
                    finish();
                } else {
                    showToast("Failed to add food item.");
                }

                // Close the database connection
                db.close();
            }
        });

        // Set a click listener for the back button
        ImageButton addfoodback = findViewById(R.id.afiback);
        addfoodback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish(); // Close this activity and return to the previous one.
            }
        });
    }

    // Show a toast message
    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    // Display the date picker dialog
    private void showDatePickerDialog(final EditText editText) {
        Calendar currentDate = Calendar.getInstance();
        int year = currentDate.get(Calendar.YEAR);
        int month = currentDate.get(Calendar.MONTH);
        int day = currentDate.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view, selectedYear, selectedMonth, selectedDay) -> {
            Calendar selectedDate = Calendar.getInstance();
            selectedDate.set(selectedYear, selectedMonth, selectedDay);

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
            String formattedDate = dateFormat.format(selectedDate.getTime());

            editText.setText(formattedDate);
        }, year, month, day);

        datePickerDialog.show();
    }
}
