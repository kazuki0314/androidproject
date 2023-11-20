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

public class payment extends AppCompatActivity {

    private ListView paymentListView;
    private DatabaseHelper dbHelper;
    private ArrayAdapter<String> adapter;

    private static final int ADD_PAYMENT_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.payment_page);

        // Initialize the database helper
        dbHelper = new DatabaseHelper(this);

        // Set up UI components
        setupUI();

        // Retrieve and display the initial payment items
        refreshPaymentList();
    }

    private void setupUI() {
        // Set back and add button
        ImageButton paybackbtn = findViewById(R.id.pyback);
        ImageButton payadd = findViewById(R.id.pyadd);

        paybackbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Navigate to home page from payment page
                Intent paygointent = new Intent(payment.this, home.class);
                startActivity(paygointent);
            }
        });

        payadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent paymentAddIntent = new Intent(payment.this, addpayment.class);
                startActivityForResult(paymentAddIntent, ADD_PAYMENT_REQUEST);
            }
        });

        // Initialize the ListView
        paymentListView = findViewById(R.id.paymentListView);

        // Set up the search EditText and add a TextWatcher
        EditText searchEditText = findViewById(R.id.pysearch);

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

        // Set item click and long-press listeners
        paymentListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Handle item click and pass all payment details to the EditPaymentActivity
                String selectedPaymentDetails = adapter.getItem(position);

                Intent editPaymentIntent = new Intent(payment.this, editpayment.class);
                editPaymentIntent.putExtra("selectedPaymentDetails", selectedPaymentDetails);
                startActivity(editPaymentIntent);
            }
        });

        paymentListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                showDeletePaymentConfirmationDialog(position);
                return true; // Consume the long-press event
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ADD_PAYMENT_REQUEST && resultCode == RESULT_OK) {
            // If a new item was added successfully, refresh the list
            refreshPaymentList();
        }
    }

    private void refreshPaymentList() {
        ArrayList<String> paymentItemsWithDetails = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String currentDate = DatabaseHelper.getCurrentDate();

        Cursor cursor = db.query(DatabaseHelper.TABLE_NAME_PAYMENT,
                new String[]{DatabaseHelper.COLUMN_PAYMENTNAME, DatabaseHelper.COLUMN_PAYTO,
                        DatabaseHelper.COLUMN_PAYFOR, DatabaseHelper.COLUMN_PAYDATE,
                        DatabaseHelper.COLUMN_PAYRDATE, DatabaseHelper.COLUMN_PAYACCNUM,
                        DatabaseHelper.COLUMN_PAYAMOUNT, DatabaseHelper.COLUMN_PAYMETHOD,
                        DatabaseHelper.COLUMN_PAYNOTE},
                null, null, null, null, null);

        while (cursor.moveToNext()) {
            // Extract payment details from the cursor
            String paymentName = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_PAYMENTNAME));
            String payTo = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_PAYTO));
            String payFor = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_PAYFOR));
            String payDate = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_PAYDATE));
            String payRDate = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_PAYRDATE));
            String payAccNum = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_PAYACCNUM));
            String payAmount = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_PAYAMOUNT));
            String payMethod = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_PAYMETHOD));
            String payNote = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_PAYNOTE));

            // Check if the payment is near expiry and add to the list
            if (DatabaseHelper.isDateNearExpiry(currentDate, payDate)) {
                String details = "Payment Name: " + paymentName +
                        "\nPay To: " + payTo +
                        "\nPay For: " + payFor +
                        "\nDate: " + payDate +
                        "\nReminder Date: " + payRDate +
                        "\nAccount Number: " + payAccNum +
                        "\nAmount: " + payAmount +
                        "\nPayment Method: " + payMethod +
                        "\nNotes: " + payNote;

                paymentItemsWithDetails.add(details);
            }
        }

        cursor.close();
        db.close();

        if (adapter == null) {
            // Initialize the adapter if it's null
            adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, paymentItemsWithDetails);
            paymentListView.setAdapter(adapter);
        } else {
            // Update the adapter with the new data
            adapter.clear();
            adapter.addAll(paymentItemsWithDetails);
            adapter.notifyDataSetChanged();
        }
    }

    private void showDeletePaymentConfirmationDialog(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Do you want to delete this item?")
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        deletePaymentItem(position);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Do nothing
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void deletePaymentItem(int position) {
        String selectedPaymentDetails = adapter.getItem(position);
        String paymentName = getPaymentNameFromDetails(selectedPaymentDetails);

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int rowsDeleted = db.delete(DatabaseHelper.TABLE_NAME_PAYMENT, DatabaseHelper.COLUMN_PAYMENTNAME + "=?", new String[]{paymentName});
        db.close();

        if (rowsDeleted > 0) {
            // If rows were deleted, refresh the payment list
            refreshPaymentList();
        }
    }

    private String getPaymentNameFromDetails(String details) {
        // Parse the payment name from the payment details string
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
