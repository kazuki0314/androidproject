package com.example.expirypal; // Replace com.example.yourapp with your actual package name

import android.content.Intent;
import android.app.DatePickerDialog;
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

public class editdocument extends AppCompatActivity {
    private DatabaseHelper dbHelper; // Database helper for database operations
    private boolean isEditModeEnabled = false; // Flag to track if edit mode is enabled
    private String originalDocumentName; // Store the original document name

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_document); // Set the content view to the edit_document_item layout

        dbHelper = new DatabaseHelper(this);

        // Find the back button
        ImageButton edibk = findViewById(R.id.eddocback);

        edibk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Return to the "document" activity
                Intent documentIntent = new Intent(editdocument.this, document.class);
                startActivity(documentIntent);
            }
        });

        // Retrieve the selected document details passed from the document activity
        Intent retrieveDocumentItemsIntent = getIntent();
        String selectedDocumentDetails = retrieveDocumentItemsIntent.getStringExtra("selectedDocumentDetails");

        // Split the details into separate fields as needed
        String[] details = selectedDocumentDetails.split("\n");
        originalDocumentName = details[0].replace("Document Name: ", ""); // Store the original document name

        // Find the EditText fields in the layout
        EditText etdnedit = findViewById(R.id.editTextText20); // Document Name
        EditText eteditrd = findViewById(R.id.editTextDate3); // Renewal Date
        EditText eteditrem = findViewById(R.id.editTextText18); // Reminder
        EditText eteditdn = findViewById(R.id.editTextNumber4); // Document Number
        EditText eteditsendto = findViewById(R.id.editTextText17); // Send To
        EditText eteditloc = findViewById(R.id.editTextText16); // Location Stored
        EditText eteditnote = findViewById(R.id.editTextText15); // Notes
        //EditText eteditattach = findViewById(R.id.editTextText21); // Attachment

        // Populate the EditText fields with the retrieved data
        etdnedit.setText(originalDocumentName);
        eteditrd.setText(details[1].replace("Renewal Date: ", ""));
        eteditrem.setText(details[2].replace("Reminder: ", ""));
        eteditdn.setText(details[3].replace("Document Number: ", ""));
        eteditsendto.setText(details[4].replace("Send To: ", ""));
        eteditloc.setText(details[5].replace("Location Stored: ", ""));
        eteditnote.setText(details[6].replace("Notes: ", ""));
        //eteditattach.setText(details[7].replace("Attachment: ", ""));

        // Find the "Edit" button
        ImageButton edienedit = findViewById(R.id.edienedit);

        // Add a click listener to enable or disable edit mode
        edienedit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Toggle edit mode flag
                isEditModeEnabled = !isEditModeEnabled;

                // Enable or disable edit mode based on the flag
                enableDisableEditMode(isEditModeEnabled, etdnedit, eteditrd, eteditrem, eteditdn, eteditsendto, eteditloc, eteditnote);
            }
        });

        // Add click listener for the renewal date EditText field
        eteditrd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog(eteditrd);
            }
        });

        // Find the "Save" button
        Button edisave = findViewById(R.id.button5);

        // Add a click listener to save the edited document details
        edisave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get the updated document details from the EditText fields
                String documentName = etdnedit.getText().toString();
                String renewalDate = eteditrd.getText().toString();
                String reminder = eteditrem.getText().toString();
                String documentNumber = eteditdn.getText().toString();
                String sendTo = eteditsendto.getText().toString();
                String locationStored = eteditloc.getText().toString();
                String notes = eteditnote.getText().toString();
                //String attachment = eteditattach.getText().toString();

                // Update the document details in the database
                boolean isUpdateSuccessful = dbHelper.updateDocumentDetails(originalDocumentName, documentName, renewalDate, reminder, documentNumber, sendTo, locationStored, notes);

                if (isUpdateSuccessful) {
                    // Show a success message
                    Toast.makeText(editdocument.this, "Document details updated successfully", Toast.LENGTH_SHORT).show();

                    // Return to the previous page
                    Intent documentIntent = new Intent(editdocument.this, document.class);
                    startActivity(documentIntent);
                } else {
                    // Show an error message
                    Toast.makeText(editdocument.this, "Failed to update document details", Toast.LENGTH_SHORT).show();
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

            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yy", Locale.US);
            String formattedDate = dateFormat.format(selectedDate.getTime());

            editText.setText(formattedDate);
        }, year, month, day);

        datePickerDialog.show();
    }

    // Enable or disable edit mode for EditText fields based on the flag
    private void enableDisableEditMode(boolean isEditModeEnabled, EditText... editTexts) {
        for (EditText editText : editTexts) {
            editText.setEnabled(isEditModeEnabled);
        }
    }
}