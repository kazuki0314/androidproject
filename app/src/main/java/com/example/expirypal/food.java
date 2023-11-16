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
    private ListView foodListView;
    private DatabaseHelper dbHelper;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.food_item_page);

        dbHelper = new DatabaseHelper(this);

        ImageButton fooditemsback = findViewById(R.id.fiback);
        ImageButton foodadd = findViewById(R.id.fiadd);

        fooditemsback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent homeIntent = new Intent(food.this, home.class);
                startActivity(homeIntent);
            }
        });

        foodadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addFoodIntent = new Intent(food.this, addfood.class);
                startActivityForResult(addFoodIntent, ADD_FOOD_REQUEST);
            }
        });

        foodListView = findViewById(R.id.foodListView);

        EditText searchEditText = findViewById(R.id.fisearch);

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

        refreshFoodList();

        foodListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedFoodDetails = adapter.getItem(position);

                Intent editFoodIntent = new Intent(food.this, editfood.class);
                editFoodIntent.putExtra("selectedFoodDetails", selectedFoodDetails);
                startActivity(editFoodIntent);
            }
        });

        foodListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                showDeleteConfirmationDialog(position);
                return true;
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

    private static final int ADD_FOOD_REQUEST = 1;

    private void refreshFoodList() {
        ArrayList<String> foodItemsWithDetails = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String currentDate = DatabaseHelper.getCurrentDate();

        Cursor cursor = db.query(DatabaseHelper.TABLE_NAME_FOODS,
                new String[]{DatabaseHelper.COLUMN_FOODNAME, DatabaseHelper.COLUMN_EDATE, DatabaseHelper.COLUMN_RDATE, DatabaseHelper.COLUMN_QUANTITY, DatabaseHelper.COLUMN_CATEGORY, DatabaseHelper.COLUMN_NOTE},
                null, null, null, null, DatabaseHelper.COLUMN_EDATE);

        while (cursor.moveToNext()) {
            String foodName = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_FOODNAME));
            String expiryDate = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_EDATE));
            String reminderDate = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_RDATE));
            String quantity = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_QUANTITY));
            String category = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_CATEGORY));
            String note = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_NOTE));

            if (DatabaseHelper.isDateNearExpiry(currentDate, expiryDate)) {
                String details = "Food Name: " + foodName +
                        "\nExpiry Date: " + expiryDate +
                        "\nReminder Date: " + reminderDate +
                        "\nQuantity: " + quantity +
                        "\nCategory: " + category +
                        "\nNote: " + note;

                foodItemsWithDetails.add(details);
            }
        }

        cursor.close();
        db.close();

        // Clear the adapter before updating it
        if (adapter != null) {
            adapter.clear();
        }

        // Update the adapter with the new data
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, foodItemsWithDetails);
        foodListView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
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
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void deleteFoodItem(int position) {
        String selectedFoodDetails = adapter.getItem(position);
        String foodName = getFoodNameFromDetails(selectedFoodDetails);

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int rowsDeleted = db.delete(DatabaseHelper.TABLE_NAME_FOODS, DatabaseHelper.COLUMN_FOODNAME + "=?", new String[]{foodName});
        db.close();

        if (rowsDeleted > 0) {
            refreshFoodList();
        }
    }

    private String getFoodNameFromDetails(String details) {
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
