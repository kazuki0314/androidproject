package com.example.expirypal;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class adddocument extends AppCompatActivity {

    // Declare the UI elements
    private EditText docName, renewalDate, reminder, sendTo, docNumber, note, location;
    private Button addDocButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_document);

        // Initialize UI elements by finding their respective views
        docName = findViewById(R.id.editTextText);
        renewalDate = findViewById(R.id.editTextText4);
        reminder = findViewById(R.id.editTextText5);
        sendTo = findViewById(R.id.editTextText7);
        docNumber = findViewById(R.id.editTextNumber2);
        note = findViewById(R.id.editTextText9);
        location = findViewById(R.id.editTextText8);
        addDocButton = findViewById(R.id.button7);

        // Set click listener for the renewal date input field
        renewalDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog(renewalDate);
            }
        });

        // Set click listener for the "Add Document" button
        addDocButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Retrieve user input from the UI elements
                String documentName = docName.getText().toString();
                String renewalDateStr = renewalDate.getText().toString();
                String reminderStr = reminder.getText().toString();
                String sendToStr = sendTo.getText().toString();
                String docNumberStr = docNumber.getText().toString();
                String noteStr = note.getText().toString();
                String locationStr = location.getText().toString();

                // Check if the required fields are filled
                if (documentName.isEmpty() || renewalDateStr.isEmpty() || reminderStr.isEmpty() || sendToStr.isEmpty() || docNumberStr.isEmpty() || noteStr.isEmpty() || locationStr.isEmpty()) {
                    showToast("Please fill in all required fields.");
                    return;
                }

                // Open the database and insert document information
                DatabaseHelper dbHelper = new DatabaseHelper(adddocument.this);
                SQLiteDatabase db = dbHelper.getWritableDatabase();

                ContentValues documentInfo = new ContentValues();
                documentInfo.put(DatabaseHelper.COLUMN_DOCNAME, documentName);
                documentInfo.put(DatabaseHelper.COLUMN_RENEWAL_DATE, renewalDateStr);
                documentInfo.put(DatabaseHelper.COLUMN_RDATE_DOC, reminderStr);
                documentInfo.put(DatabaseHelper.COLUMN_SENDTO, sendToStr);
                documentInfo.put(DatabaseHelper.COLUMN_DOCNUM, docNumberStr);
                documentInfo.put(DatabaseHelper.COLUMN_NOTE, noteStr);
                documentInfo.put(DatabaseHelper.COLUMN_LOCATION, locationStr);

                long newRowId = db.insert(DatabaseHelper.TABLE_NAME_DOCUMENTS, null, documentInfo);

                // Show a toast message based on the insert result
                if (newRowId != -1) {
                    showToast("Document added successfully!");
                    finish();
                } else {
                    showToast("Failed to add document.");
                }

                // Close the database connection
                db.close();
            }
        });

        // Set a click listener for the back button
        ImageButton addDocBack = findViewById(R.id.addocback);
        addDocBack.setOnClickListener(new View.OnClickListener() {
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
