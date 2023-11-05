package com.example.expirypal;

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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class addfood extends AppCompatActivity {

    private EditText foodn, expdate, remdate, quanty, notes;
    private Spinner spinnerFoodCategory;
    private Button addfoodit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_food_items);

        foodn = findViewById(R.id.etfoodn);
        expdate = findViewById(R.id.editTextDate);
        remdate = findViewById(R.id.editTextDate2);
        quanty = findViewById(R.id.editTextNumber);
        spinnerFoodCategory = findViewById(R.id.spinnerFoodCategory);
        notes = findViewById(R.id.editTextText3);
        addfoodit = findViewById(R.id.addfibtn);

        ArrayAdapter<CharSequence> foodCategoryAdapter = ArrayAdapter.createFromResource(
                this,
                R.array.food_categories,
                android.R.layout.simple_spinner_item
        );
        foodCategoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerFoodCategory.setAdapter(foodCategoryAdapter);

        ImageButton addfoodback = findViewById(R.id.afiback);

        addfoodit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String foodname = foodn.getText().toString();
                String expDateStr = expdate.getText().toString();
                String remDateStr = remdate.getText().toString();
                String quantity = quanty.getText().toString();
                String category = spinnerFoodCategory.getSelectedItem().toString();
                String note = notes.getText().toString();

                long expDateMillis = parseDate(expDateStr);
                long remDateMillis = parseDate(remDateStr);

                if (expDateMillis == -1 || remDateMillis == -1) {
                    showToast("Invalid date format. Please use yyyy-MM-dd.");
                    return; // Don't proceed if date parsing fails.
                }

                DatabaseHelper dbHelper = new DatabaseHelper(addfood.this);
                SQLiteDatabase db = dbHelper.getWritableDatabase();

                ContentValues foodinfo = new ContentValues();
                foodinfo.put(DatabaseHelper.COLUMN_FOODNAME, foodname);
                foodinfo.put(DatabaseHelper.COLUMN_EDATE, expDateMillis);
                foodinfo.put(DatabaseHelper.COLUMN_RDATE, remDateMillis);
                foodinfo.put(DatabaseHelper.COLUMN_QUANTITY, quantity);
                foodinfo.put(DatabaseHelper.COLUMN_CATEGORY, category);
                foodinfo.put(DatabaseHelper.COLUMN_NOTE, note);

                long newRowId = db.insert(DatabaseHelper.TABLE_NAME_FOODS, null, foodinfo);

                if (newRowId != -1) {
                    showToast("Food item added successfully!");
                    finish();
                } else {
                    showToast("Failed to add food item.");
                }

                db.close();
            }
        });

        addfoodback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish(); // Close this activity and return to the previous one.
            }
        });
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private long parseDate(String dateStr) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date = dateFormat.parse(dateStr);
            return date != null ? date.getTime() : -1;
        } catch (ParseException e) {
            e.printStackTrace();
            return -1;
        }
    }
}
