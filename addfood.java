package com.example.expirypal;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
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
    private EditText foodn, expdate, remdate, quanty, notes;
    private Spinner spinnerFoodCategory;
    private Button addfoodit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_food_items);

        foodn = findViewById(R.id.etfoodn);
        expdate = findViewById(R.id.etexpidate);
        remdate = findViewById(R.id.etremindt);
        quanty = findViewById(R.id.etqty);
        spinnerFoodCategory = findViewById(R.id.sfcategory);
        notes = findViewById(R.id.etNote);
        addfoodit = findViewById(R.id.addfibtn);

        ArrayAdapter<CharSequence> foodCategoryAdapter = ArrayAdapter.createFromResource(
                this,
                R.array.food_categories,
                android.R.layout.simple_spinner_item
        );
        foodCategoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerFoodCategory.setAdapter(foodCategoryAdapter);

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

        addfoodit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String foodname = foodn.getText().toString();
                String expDateStr = expdate.getText().toString();
                String remDateStr = remdate.getText().toString();
                String quantity = quanty.getText().toString();
                String category = spinnerFoodCategory.getSelectedItem().toString();
                String note = notes.getText().toString();

                if (foodname.isEmpty() || expDateStr.isEmpty() || remDateStr.isEmpty() || quantity.isEmpty() || category.equals("Select Category")) {
                    showToast("Please fill in all required fields.");
                    return;
                }

                DatabaseHelper dbHelperfood = new DatabaseHelper(addfood.this);
                SQLiteDatabase dbfood = dbHelperfood.getWritableDatabase();

                ContentValues foodinfo = new ContentValues();
                foodinfo.put(DatabaseHelper.COLUMN_FOODNAME, foodname);
                foodinfo.put(DatabaseHelper.COLUMN_EDATE, expDateStr);
                foodinfo.put(DatabaseHelper.COLUMN_RDATE, remDateStr);
                foodinfo.put(DatabaseHelper.COLUMN_QUANTITY, quantity);
                foodinfo.put(DatabaseHelper.COLUMN_CATEGORY, category);
                foodinfo.put(DatabaseHelper.COLUMN_NOTE, note);

                long newRowId = dbfood.insert(DatabaseHelper.TABLE_NAME_FOODS, null, foodinfo);

                if (newRowId != -1) {
                    // Schedule the reminder if the food item is added successfully
                    setReminder(remDateStr, foodname);

                    showToast("Food item added successfully!");
                    finish();
                } else {
                    showToast("Failed to add food item.");
                }

                dbfood.close();
            }
        });

        ImageButton addfoodback = findViewById(R.id.afiback);
        addfoodback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

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

    private void setReminder(String reminderDate, String foodName) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, ReminderReceiver.class);
        intent.putExtra("food_name", foodName);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

        long reminderMillis = parseDateToMillis(reminderDate);

        if (alarmManager != null) {
            alarmManager.set(AlarmManager.RTC_WAKEUP, reminderMillis, pendingIntent);
        }
    }

    private long parseDateToMillis(String dateStr) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(dateFormat.parse(dateStr));
            return calendar.getTimeInMillis();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
}
