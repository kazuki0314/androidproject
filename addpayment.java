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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class addpayment extends AppCompatActivity {
    // Declare the UI elements
    private EditText payn, payto, payfor, paydate, payremdate, payamount, accnum, paymethod, paynotes;
    private Button addpaymentbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_payment_details);

        // Initialize UI elements by finding their respective views
        payn = findViewById(R.id.adpayName);
        payto = findViewById(R.id.adpayTo);
        payfor = findViewById(R.id.adpayFor);
        paydate = findViewById(R.id.adpayDate);
        payremdate = findViewById(R.id.adpayRem);
        payamount = findViewById(R.id.adpayAmt);
        accnum = findViewById(R.id.adpayAcc);
        paymethod = findViewById(R.id.adpayMet);
        paynotes = findViewById(R.id.adpayNotes);
        addpaymentbtn = findViewById(R.id.addpaybtn);


        // Set click listeners for the date input fields
        paydate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog(paydate);
            }
        });

        payremdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog(payremdate);
            }
        });

        // Set a click listener for the "Add" button
        addpaymentbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Retrieve user input from the UI elements
                String paymentnameStr = payn.getText().toString();
                String paytoStr = payto.getText().toString();
                String payforStr = payfor.getText().toString();
                String paydateStr = paydate.getText().toString();
                String payremdateStr = payremdate.getText().toString();
                String payamountStr = payamount.getText().toString();
                String accnumStr = accnum.getText().toString();
                String paymethodStr = paymethod.getText().toString();
                String paynotesStr = paynotes.getText().toString();


                // Check if the required fields are filled
                if (paymentnameStr.isEmpty() || paytoStr.isEmpty() || payforStr.isEmpty() || paydateStr.isEmpty() || payremdateStr.isEmpty() || payamountStr.isEmpty() || accnumStr.isEmpty() || paymethodStr.isEmpty() ){
                    showToast("Please fill in all required fields.");
                    return;
                }

                // Open the database and insert food information
                DatabaseHelper dbHelper = new DatabaseHelper(addpayment.this);
                SQLiteDatabase db = dbHelper.getWritableDatabase();

                ContentValues paymentinfo = new ContentValues();
                paymentinfo.put(DatabaseHelper.COLUMN_PAYMENTNAME, paymentnameStr);
                paymentinfo.put(DatabaseHelper.COLUMN_PAYTO, paytoStr);
                paymentinfo.put(DatabaseHelper.COLUMN_PAYFOR, payforStr);
                paymentinfo.put(DatabaseHelper.COLUMN_PAYDATE, paydateStr);
                paymentinfo.put(DatabaseHelper.COLUMN_PAYRDATE, payremdateStr);
                paymentinfo.put(DatabaseHelper.COLUMN_PAYAMOUNT, payamountStr);
                paymentinfo.put(DatabaseHelper.COLUMN_PAYACCNUM, accnumStr);
                paymentinfo.put(DatabaseHelper.COLUMN_PAYMETHOD, paymethodStr);
                paymentinfo.put(DatabaseHelper.COLUMN_PAYNOTE, paynotesStr);

                long newRowId = db.insert(DatabaseHelper.TABLE_NAME_PAYMENT, null, paymentinfo);

                // Show a toast message based on the insert result
                if (newRowId != -1) {
                    setReminder(payremdateStr, paymentnameStr);
                    showToast("Payment item added successfully!");
                    finish();
                } else {
                    showToast("Failed to add payment item.");
                }

                // Close the database connection
                db.close();
            }
        });

        // Set a click listener for the back button
        ImageButton addpaymentback = findViewById(R.id.apdback);
        addpaymentback.setOnClickListener(new View.OnClickListener() {
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
    private void setReminder(String reminderDate, String paymentName) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, ReminderReceiverPayment.class);
        intent.putExtra("payment_name", paymentName);

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
