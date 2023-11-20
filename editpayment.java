package com.example.expirypal;

import android.content.DialogInterface;
import android.content.Intent;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
public class editpayment extends AppCompatActivity {

    private DatabaseHelper dbHelperpayment; // Database helper for database operations
    private boolean isEditModeEnabled = false; // Flag to track if edit mode is enabled
    private String originalPaymentName; // Store the original payment name

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_payment_details);

        dbHelperpayment = new DatabaseHelper(this);
        ImageButton editpayback = findViewById(R.id.epdback);


        editpayback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent payeditbackintent = new Intent(editpayment.this, payment.class);
                startActivity(payeditbackintent);
            }
        });
        // Retrieve the selected food details passed from the food activity
        Intent retrievePaymentItemsIntent = getIntent();
        String selectedPaymentDetails = retrievePaymentItemsIntent.getStringExtra("selectedPaymentDetails");

        // Split the details into separate fields as needed
        String[] details = selectedPaymentDetails.split("\n");
        originalPaymentName = details[0].replace("Payment Name: ", ""); // Store the original food name

        // Find the EditText fields in the layout
        EditText edpayname= findViewById(R.id.edpayname);
        EditText edpayto = findViewById(R.id.edpayto);
        EditText edpayfor = findViewById(R.id.edpayfor);
        EditText edpaydate = findViewById(R.id.edpaydate);
        EditText edpayrem= findViewById(R.id.edremdate);
        EditText edpayamt = findViewById(R.id.edpayamt);
        EditText edpayaccnum = findViewById(R.id.edpayaccnum);
        EditText edpaymethod = findViewById(R.id.edpaymethod);
        EditText edpaynotes = findViewById(R.id.edpaynote);


        // Populate the EditText fields with the retrieved data
        edpayname.setText(originalPaymentName);
        edpayto.setText(details[1].replace("Pay To: ", ""));
        edpayfor.setText(details[2].replace("Pay For: ", ""));
        edpaydate.setText(details[3].replace("Date: ", ""));
        edpayrem.setText(details[4].replace("Reminder Date: ", ""));
        edpayamt.setText(details[5].replace("Account Number: ", ""));
        edpayaccnum.setText(details[6].replace("Amount: ", ""));
        edpaymethod.setText(details[7].replace("Payment Method: ", ""));
        edpaynotes.setText(details[8].replace("Notes:", ""));


        // Find the "Edit" button
        ImageButton efienedit = findViewById(R.id.edpaybtn);

        // Add a click listener to enable or disable edit mode
        efienedit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isEditModeEnabled) {
                    // Disable edit mode
                    disableEditMode(edpayname);
                    disableEditMode(edpayto);
                    disableEditMode(edpayfor);
                    disableEditMode(edpaydate);
                    disableEditMode(edpayrem);
                    disableEditMode(edpayamt);
                    disableEditMode(edpayaccnum);
                    disableEditMode(edpaymethod);
                    disableEditMode(edpaynotes);
                } else {
                    // Enable edit mode
                    enableEditMode(edpayname);
                    enableEditMode(edpayto);
                    enableEditMode(edpayfor);
                    enableEditMode(edpaydate);
                    enableEditMode(edpayrem);
                    enableEditMode(edpayamt);
                    enableEditMode(edpayaccnum);
                    enableEditMode(edpaymethod);
                    enableEditMode(edpaynotes);
                }
                isEditModeEnabled = !isEditModeEnabled; // Toggle edit mode flag
            }
        });

        // Add click listeners for the pay date and reminder date EditText fields
        edpaydate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog(edpaydate);
            }
        });

        edpayrem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog(edpayrem);
            }
        });

        // Find the "Save" button
        Button edpaysave = findViewById(R.id.edpaysavebtn);

        // Add a click listener to save the edited food details
        edpaysave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get the updated food details from the EditText fields
                String paymentnameStr = edpayname.getText().toString();
                String paytoStr = edpayto.getText().toString();
                String payforStr = edpayfor.getText().toString();
                String paydateStr = edpaydate.getText().toString();
                String payremdateStr = edpayrem.getText().toString();
                String payamountStr = edpayamt.getText().toString();
                String accnumStr = edpayaccnum.getText().toString();
                String paymethodStr = edpaymethod.getText().toString();
                String paynotesStr = edpaynotes.getText().toString();

                // Check for empty fields (except "Note")
                if (paymentnameStr.isEmpty() || paytoStr.isEmpty() || payforStr.isEmpty() || paydateStr.isEmpty() || payremdateStr.isEmpty() || payamountStr.isEmpty() || accnumStr.isEmpty() || paymethodStr.isEmpty() || paynotesStr.isEmpty())

                    Toast.makeText(editpayment.this, "Please fill in all required fields.", Toast.LENGTH_SHORT).show();
                else {
                    // Create a confirmation dialog
                    AlertDialog.Builder buildereditpayment = new AlertDialog.Builder(editpayment.this);
                    buildereditpayment.setMessage("Do you want to save the changes?");
                    buildereditpayment.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // Update the food details in the database
                            boolean isUpdateSuccessful = dbHelperpayment.updatePaymentDetails(originalPaymentName, paymentnameStr, paytoStr, payforStr, paydateStr, payremdateStr, payamountStr, accnumStr,paymethodStr, paynotesStr);

                            if (isUpdateSuccessful) {
                                // Show a success message
                                Toast.makeText(editpayment.this, "Payment details updated successfully", Toast.LENGTH_SHORT).show();

                                // Return to the previous page
                                Intent editbackeditIntent = new Intent(editpayment.this, payment.class);
                                startActivity(editbackeditIntent);
                            } else {
                                // Show an error message
                                Toast.makeText(editpayment.this, "Failed to update payment details", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    buildereditpayment.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // User canceled the operation
                            dialog.dismiss(); // Close the dialog
                        }
                    });

                    // Show the confirmation dialog
                    AlertDialog dialogeditpayment = buildereditpayment.create();
                    dialogeditpayment.show();
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

}