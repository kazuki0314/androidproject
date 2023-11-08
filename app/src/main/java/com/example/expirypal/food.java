package com.example.expirypal;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class food extends AppCompatActivity {
    private ListView foodListView; // Declare a ListView for displaying food items
    private DatabaseHelper dbHelper; // Database helper for database operations
    private ArrayAdapter<String> adapter; // Adapter for the ListView

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.food_item_page); // Set the content view to the food_item_page layout

        // Initialize the database helper
        dbHelper = new DatabaseHelper(this);

        // Set up the back and add buttons
        ImageButton fooditemsback = findViewById(R.id.fiback); // Find the back button by its ID
        ImageButton foodadd = findViewById(R.id.fiadd); // Find the add button by its ID

        // Set a click listener for the "Back" button
        fooditemsback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate back to the home activity
                Intent homeIntent = new Intent(food.this, home.class);
                startActivity(homeIntent);
            }
        });

        // Set a click listener for the "Add" button
        foodadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Start the "addfood" activity to add a new food item
                Intent addFoodIntent = new Intent(food.this, addfood.class);
                startActivityForResult(addFoodIntent, ADD_FOOD_REQUEST);
            }
        });

        // Initialize the ListView
        foodListView = findViewById(R.id.foodListView); // Find the ListView by its ID

        // Set up the search EditText and add a TextWatcher
        EditText searchEditText = findViewById(R.id.fisearch); // Find the search EditText by its ID

        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        // Retrieve and display the initial food items
        refreshFoodList();

        // Set an item click listener to handle clicks on the food items
        foodListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Handle item click and pass all food details to the editfood activity
                String selectedFoodDetails = adapter.getItem(position);

                Intent editFoodIntent = new Intent(food.this, editfood.class);
                editFoodIntent.putExtra("selectedFoodDetails", selectedFoodDetails);
                startActivity(editFoodIntent);
            }
        });

        // Set a long-press listener to delete food items
        foodListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                showDeleteConfirmationDialog(position);
                return true; // Consume the long-press event
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ADD_FOOD_REQUEST && resultCode == RESULT_OK) {
            // If a new item was added successfully, refresh the list
            refreshFoodList();
        }
    }

    private static final int ADD_FOOD_REQUEST = 1; // Request code for adding food items

    private void refreshFoodList() {
        ArrayList<String> foodItemsWithDetails = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(DatabaseHelper.TABLE_NAME_FOODS,
                new String[]{DatabaseHelper.COLUMN_FOODNAME, DatabaseHelper.COLUMN_EDATE, DatabaseHelper.COLUMN_RDATE, DatabaseHelper.COLUMN_QUANTITY, DatabaseHelper.COLUMN_CATEGORY, DatabaseHelper.COLUMN_NOTE},
                null, null, null, null, null);

        while (cursor.moveToNext()) {
            String foodName = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_FOODNAME));
            String expiryDate = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_EDATE));
            String reminderDate = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_RDATE));
            String quantity = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_QUANTITY));
            String category = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_CATEGORY));
            String note = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_NOTE));

            String details = "Food Name: " + foodName +
                    "\nExpiry Date: " + expiryDate +
                    "\nReminder Date: " + reminderDate +
                    "\nQuantity: " + quantity +
                    "\nCategory: " + category +
                    "\nNote: " + note;

            foodItemsWithDetails.add(details);
        }

        cursor.close();
        db.close();

        if (adapter == null) {
            adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, foodItemsWithDetails);
            foodListView.setAdapter(adapter);
        } else {
            adapter.clear();
            adapter.addAll(foodItemsWithDetails);
            adapter.notifyDataSetChanged();
        }
    }

    private void showDeleteConfirmationDialog(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Do you want to delete this item?")
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        deleteFoodItem(position);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User canceled the deletion, so do nothing
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void deleteFoodItem(int position) {
        // Get the selected food name from the adapter
        String selectedFoodDetails = adapter.getItem(position);
        String foodName = getFoodNameFromDetails(selectedFoodDetails);

        // Delete the item from the database based on the food name
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int rowsDeleted = db.delete(DatabaseHelper.TABLE_NAME_FOODS, DatabaseHelper.COLUMN_FOODNAME + "=?", new String[]{foodName});
        db.close();

        if (rowsDeleted > 0) {
            // Deletion was successful, refresh the food list
            refreshFoodList();
        }
    }

    private String getFoodNameFromDetails(String details) {
        // Parse the food name from the food details string
        String[] lines = details.split("\n");
        if (lines.length > 0) {
            String line = lines[0];
            int colonIndex = line.indexOf(": ");
            if (colonIndex >= 0 && colonIndex < line.length() - 2) {
                return line.substring(colonIndex + 2);
            }
        }
        return "";
    }
}
