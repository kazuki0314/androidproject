package com.example.expirypal;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import android.app.AlarmManager;
import android.app.PendingIntent;

public class adddocument extends AppCompatActivity {

    private EditText docName, renewalDate, reminder, sendTo, docNumber, docnotes, location;
    private Button addDocButton, addDocFileBtn;

    ArrayList<Uri> attachments = new ArrayList<>();
    AttachmentListAdapter attachmentListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_document);

        docName = findViewById(R.id.editTextText);
        renewalDate = findViewById(R.id.editTextText4);
        reminder = findViewById(R.id.editTextText5);
        sendTo = findViewById(R.id.editTextText7);
        docNumber = findViewById(R.id.editTextNumber2);
        docnotes = findViewById(R.id.editTextText9);
        location = findViewById(R.id.editTextText8);
        addDocButton = findViewById(R.id.button7);
        addDocFileBtn = findViewById(R.id.addDocFileBtn);

        // Initialize the attachmentsListView and create an adapter
        ListView attachementListView = findViewById(R.id.docfilesListView);
        attachmentListAdapter = new AttachmentListAdapter(this, attachments);
        attachementListView.setAdapter(attachmentListAdapter);

        // Set click listener for the renewal date input field
        renewalDate.setOnClickListener(view -> showDatePickerDialog(renewalDate));
        reminder.setOnClickListener(View -> showDatePickerDialog(reminder));

        // Set click listener for the "Add Document" button
        addDocButton.setOnClickListener(view -> {
            // Retrieve user input from the UI elements
            String documentName = docName.getText().toString();
            String renewalDateStr = renewalDate.getText().toString();
            String reminderStr = reminder.getText().toString();
            String sendToStr = sendTo.getText().toString();
            String docNumberStr = docNumber.getText().toString();
            String docnotesStr = docnotes.getText().toString();
            String locationStr = location.getText().toString();

            // Check if the required fields are filled
            if (documentName.isEmpty() || renewalDateStr.isEmpty() || reminderStr.isEmpty() || sendToStr.isEmpty() || docNumberStr.isEmpty() || docnotesStr.isEmpty() || locationStr.isEmpty()) {
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
            documentInfo.put(DatabaseHelper.COLUMN_DOC_NOTES, docnotesStr);
            documentInfo.put(DatabaseHelper.COLUMN_LOCATION, locationStr);

            long newRowId = db.insert(DatabaseHelper.TABLE_NAME_DOCUMENTS, null, documentInfo);

            // Show a toast message based on the insert result
            if (newRowId != -1) {
                //Scheduling the reminder if the document is successfully added
                setReminder(reminderStr, documentName);

                showToast("Document added successfully!");
                finish();
            } else {
                showToast("Failed to add document.");
            }

            // Close the database connection
            db.close();
        });

        // Set a click listener for the back button
        ImageButton addDocBack = findViewById(R.id.addocback);
        addDocBack.setOnClickListener(view -> finish());

        // Set click listener for the "Add File" button
        addDocFileBtn.setOnClickListener(view -> onAddButtonClick());
    }

    private void onAddButtonClick() {
        // Use an Intent to open the file picker
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*"); // Allow all file types to be selected
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        // Start the file picker activity
        startActivityForResult(intent, 1);
    }

    // Handle the result of the file picker activity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK) {
            if (data != null) {
                Uri selectedFileUri = data.getData();
                attachments.add(selectedFileUri); // Store the selected file URI

                // Notify the adapter that the data set has changed
                attachmentListAdapter.notifyDataSetChanged();

                // You can display the attached file name or other information as needed
                // For example, you can show a list of attached files on the UI
            }
        }
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

    private void setReminder(String reminderDate, String documentName) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, ReminderReceiverDocument.class);
        intent.putExtra("document_name", documentName);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

        long reminderMillis = parseDateToMillis(reminderDate);

        if (alarmManager != null) {
            alarmManager.set(AlarmManager.RTC_WAKEUP, reminderMillis, pendingIntent);
        }
    }

    // Add this method to parse the date to milliseconds
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
