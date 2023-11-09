package com.example.expirypal;

import android.content.DialogInterface;
import android.content.Intent;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class editfood extends AppCompatActivity {
    private DatabaseHelper dbHelper; // Database helper for database operations
    private boolean isEditModeEnabled = false; // Flag to track if edit mode is enabled
    private String originalFoodName; // Store the original food name

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_food_item); // Set the content view to the edit_food_item layout

        dbHelper = new DatabaseHelper(this);

        // Find the back button
        ImageButton efibk = findViewById(R.id.efiback);

        efibk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Return to the "food" activity
                Intent foodgobackeditprevIntent = new Intent(editfood.this, food.class);
                startActivity(foodgobackeditprevIntent);
            }
        });

        // Retrieve the selected food details passed from the food activity
        Intent retrieveFoodItemsIntent = getIntent();
        String selectedFoodDetails = retrieveFoodItemsIntent.getStringExtra("selectedFoodDetails");

        // Split the details into separate fields as needed
        String[] details = selectedFoodDetails.split("\n");
        originalFoodName = details[0].replace("Food Name: ", ""); // Store the original food name

        // Find the EditText fields in the layout
        EditText etfnedit = findViewById(R.id.etfnedit);
        EditText eteditexp = findViewById(R.id.eteditexp);
        EditText eteditrem = findViewById(R.id.eteditrem);
        EditText eteditqty = findViewById(R.id.eteditqty);
        Spinner sfoodcty = findViewById(R.id.sfcategory2);
        EditText eteditnote = findViewById(R.id.eteditnote);
        // Find the "Save" button
        Button efisave = findViewById(R.id.btnsvefi);

        // Get the array of food categories from the resources
        String[] foodCategories = getResources().getStringArray(R.array.food_categories);

        // Create an ArrayAdapter for the Spinner using the foodCategories array
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, foodCategories);

        // Set the adapter for the Spinner
        sfoodcty.setAdapter(adapter);
        sfoodcty.setEnabled(false);

        // Populate the EditText fields with the retrieved data
        etfnedit.setText(originalFoodName);
        eteditexp.setText(details[1].replace("Expiry Date: ", ""));
        eteditrem.setText(details[2].replace("Reminder Date: ", ""));
        eteditqty.setText(details[3].replace("Quantity: ", ""));
        String selectedCategory = details[4].replace("Category: ", "");
        sfoodcty.setSelection(findCategoryPosition(foodCategories, selectedCategory));
        eteditnote.setText(details[5].replace("Note: ", ""));

        // Find the "Edit" button
        ImageButton efienedit = findViewById(R.id.efienedit);

        // Add a click listener to enable or disable edit mode
        efienedit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isEditModeEnabled) {
                    // Disable edit mode
                    disableEditMode(etfnedit);
                    disableEditMode(eteditexp);
                    disableEditMode(eteditrem);
                    disableEditMode(eteditqty);
                    disableEditMode(eteditnote);
                    sfoodcty.setEnabled(false); // Disable the Spinner
                    efisave.setEnabled(false);
                } else {
                    // Enable edit mode
                    enableEditMode(etfnedit);
                    enableEditMode(eteditexp);
                    enableEditMode(eteditrem);
                    enableEditMode(eteditqty);
                    enableEditMode(eteditnote);
                    sfoodcty.setEnabled(true); // Enable the Spinner
                    efisave.setEnabled(true);
                }
                isEditModeEnabled = !isEditModeEnabled; // Toggle edit mode flag
            }
        });

        // Add click listeners for the expiry date and reminder date EditText fields
        eteditexp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog(eteditexp);
            }
        });

        eteditrem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog(eteditrem);
            }
        });

        // Add a click listener to save the edited food details
        efisave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get the updated food details from the EditText fields
                String foodName = etfnedit.getText().toString();
                String expiryDate = eteditexp.getText().toString();
                String reminderDate = eteditrem.getText().toString();
                String quantity = eteditqty.getText().toString();
                String category = sfoodcty.getSelectedItem().toString();
                String note = eteditnote.getText().toString();

                // Check for empty fields (except "Note")
                if (foodName.isEmpty() || expiryDate.isEmpty() || reminderDate.isEmpty() || quantity.isEmpty() || category.equals("Select Category")) {
                    // Show an error message if any of the required fields are empty
                    Toast.makeText(editfood.this, "Please fill in all required fields.", Toast.LENGTH_SHORT).show();
                } else {
                    // Create a confirmation dialog
                    AlertDialog.Builder buildereditfood = new AlertDialog.Builder(editfood.this);
                    buildereditfood.setMessage("Do you want to save the changes?");
                    buildereditfood.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // Update the food details in the database
                            boolean isUpdateSuccessful = dbHelper.updateFoodDetails(originalFoodName, foodName, expiryDate, reminderDate, quantity, category, note);

                            if (isUpdateSuccessful) {
                                // Show a success message
                                Toast.makeText(editfood.this, "Food details updated successfully", Toast.LENGTH_SHORT).show();

                                // Return to the previous page
                                Intent foodbackeditIntent = new Intent(editfood.this, food.class);
                                startActivity(foodbackeditIntent);
                            } else {
                                // Show an error message
                                Toast.makeText(editfood.this, "Failed to update food details", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    buildereditfood.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // User canceled the operation
                            dialog.dismiss(); // Close the dialog
                        }
                    });

                    // Show the confirmation dialog
                    AlertDialog dialogeditfood = buildereditfood.create();
                    dialogeditfood.show();
                }
            }
        });


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

    // Enable edit mode for an EditText
    private void enableEditMode(EditText editText) {
        editText.setEnabled(true);
    }

    // Disable edit mode for an EditText
    private void disableEditMode(EditText editText) {
        editText.setEnabled(false);
    }

    // Helper method to find the position of the selected category in the array
    private int findCategoryPosition(String[] categories, String selectedCategory) {
        for (int i = 0; i < categories.length; i++) {
            if (categories[i].equals(selectedCategory)) {
                return i;
            }
        }
        return 0; // Default to the first category if not found
    }
}
