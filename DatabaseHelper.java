package com.example.expirypal;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "ep.db"; // Name of the SQLite database
    private static final int DATABASE_VERSION = 1; // Database version

    // Define table and column names for the "users" table
    public static final String TABLE_NAME_USERS = "users";
    public static final String COLUMN_USERID = "userid";
    public static final String COLUMN_USERNAME = "usern";
    public static final String COLUMN_PASSWORD = "pass";
    public static final String COLUMN_EMAIL = "email";

    // Define table and column names for the "foods" table
    public static final String TABLE_NAME_FOODS = "foods";
    public static final String COLUMN_FOODNAME = "foodname";
    public static final String COLUMN_EDATE = "expdate";
    public static final String COLUMN_RDATE = "remdate";
    public static final String COLUMN_QUANTITY = "quantity";
    public static final String COLUMN_CATEGORY = "category";
    public static final String COLUMN_NOTE = "note";

    // Define table and column names for the "documents" table
    public static final String TABLE_NAME_DOCUMENTS = "documents"; // Name of the documents table
    public static final String COLUMN_DOCNAME = "docname"; // Document name column name
    public static final String COLUMN_RENEWAL_DATE = "renewaldatedocument";
    public static final String COLUMN_RDATE_DOC = "remdate_doc"; // Reminder date column name for documents
    public static final String COLUMN_LOCATION = "location"; // Location column name
    public static final String COLUMN_DOCNUM = "docnum"; // Document number column name
    public static final String COLUMN_SENDTO = "sendto"; // Send to column name
    public static final String COLUMN_ATTACHMENT = "attachment"; // Attachment column name
    public static final String COLUMN_NOTES = "notes"; // Notes column name

    // Define table and column names for the "payment" table
    public static final String TABLE_NAME_PAYMENT = "payment"; // Name of the payment table
    public static final String COLUMN_PAYMENTNAME = "paymentname"; // payment name for payment column name
    public static final String COLUMN_PAYTO = "payto"; // pay to name column name
    public static final String COLUMN_PAYFOR = "payfor"; // pay for column name
    public static final String COLUMN_PAYDATE = "paydate"; // payment date column name
    public static final String COLUMN_PAYRDATE = "payremdate"; // Reminder date column name
    public static final String COLUMN_PAYACCNUM = "payaccnum"; // Payment amount column name
    public static final String COLUMN_PAYAMOUNT = "payamount"; // Payment amount column name
    public static final String COLUMN_PAYMETHOD = "paymethod"; // Payment method column name
    public static final String COLUMN_PAYNOTE = "paynote"; // Notes column name

    // SQL statement to create the "users" table
    private static final String DATABASE_CREATE_USERS = "create table " + TABLE_NAME_USERS +
            " (" + COLUMN_USERID + " integer primary key autoincrement, " +
            COLUMN_USERNAME + " text not null, " +
            COLUMN_PASSWORD + " text not null, " +
            COLUMN_EMAIL + " text not null);";

    // SQL statement to create the "foods" table
    private static final String DATABASE_CREATE_FOODS = "create table " + TABLE_NAME_FOODS +
            " (" + COLUMN_FOODNAME + " text not null, " +
            COLUMN_EDATE + " text not null, " +
            COLUMN_RDATE + " text not null, " +
            COLUMN_QUANTITY + " text not null, " +
            COLUMN_CATEGORY + " text not null, " +
            COLUMN_NOTE + " text not null);";

    private static final String DATABASE_CREATE_DOCUMENTS = "create table " + TABLE_NAME_DOCUMENTS +
            " (" + COLUMN_DOCNAME + " text not null, " +
            COLUMN_RENEWAL_DATE + " text not null, " +
            COLUMN_RDATE_DOC + " text not null, " +
            COLUMN_LOCATION + " text not null, " +
            COLUMN_DOCNUM + " text not null, " +
            COLUMN_SENDTO + " text not null, " +
            COLUMN_ATTACHMENT + " text not null, " +
            COLUMN_NOTES + " text not null);";


    // SQL statement to create the "payment" table
    private static final String DATABASE_CREATE_PAYMENT = "create table " + TABLE_NAME_PAYMENT +
            " (" + COLUMN_PAYMENTNAME + " text not null, " +
            COLUMN_PAYTO + " text not null, " +
            COLUMN_PAYFOR + " text not null, " +
            COLUMN_PAYDATE + " text not null, " +
            COLUMN_PAYRDATE + " text not null, " +
            COLUMN_PAYAMOUNT + " text not null," +
            COLUMN_PAYACCNUM + " text not null," +
            COLUMN_PAYMETHOD + " text not null," +
            COLUMN_PAYNOTE + " text not null);";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION); // Initialize the SQLiteOpenHelper
    }

    public boolean updateFoodDetails(String originalFoodName, String newFoodName, String expiryDate, String reminderDate, String quantity, String category, String note) {
        SQLiteDatabase db = this.getWritableDatabase(); // Get a writable database
        ContentValues values = new ContentValues();
        values.put(COLUMN_FOODNAME, newFoodName);
        values.put(COLUMN_EDATE, expiryDate);
        values.put(COLUMN_RDATE, reminderDate);
        values.put(COLUMN_QUANTITY, quantity);
        values.put(COLUMN_CATEGORY, category);
        values.put(COLUMN_NOTE, note);

        // Update the "foods" table with new values for a specific food
        int rowsUpdated = db.update(TABLE_NAME_FOODS, values, COLUMN_FOODNAME + " = ?", new String[]{originalFoodName});

        return rowsUpdated > 0; // Return true if one or more rows were updated
    }

    public boolean updateDocumentDetails(String originalDocName, String newDocName, String renewalDate, String reminderDate, String location, String docNum, String sendTo, String notes) {
        SQLiteDatabase db = this.getWritableDatabase(); // Get a writable database
        ContentValues values = new ContentValues();
        values.put(COLUMN_DOCNAME, newDocName);
        values.put(COLUMN_RENEWAL_DATE, renewalDate);
        values.put(COLUMN_RDATE_DOC, reminderDate);
        values.put(COLUMN_LOCATION, location);
        values.put(COLUMN_DOCNUM, docNum);
        values.put(COLUMN_SENDTO, sendTo);
        //values.put(COLUMN_ATTACHMENT, attachment);
        values.put(COLUMN_NOTES, notes);

        // Update the "documents" table with new values for a specific document
        int rowsUpdated = db.update(TABLE_NAME_DOCUMENTS, values, COLUMN_DOCNAME + " = ?", new String[]{originalDocName});

        return rowsUpdated > 0; // Return true if one or more rows were updated
    }


    public boolean updatePaymentDetails(String originalPaymentName, String newPaymentName, String payTo, String payFor, String payDate, String payRDate, String payAmount, String payAccNum, String payMethod, String payNote) {
        SQLiteDatabase db = this.getWritableDatabase(); // Get a writable database
        ContentValues values = new ContentValues();
        values.put(COLUMN_PAYMENTNAME, newPaymentName);
        values.put(COLUMN_PAYTO, payTo);
        values.put(COLUMN_PAYFOR, payFor);
        values.put(COLUMN_PAYDATE, payDate);
        values.put(COLUMN_PAYRDATE, payRDate);
        values.put(COLUMN_PAYAMOUNT, payAmount);
        values.put(COLUMN_PAYACCNUM, payAccNum);
        values.put(COLUMN_PAYMETHOD, payMethod);
        values.put(COLUMN_PAYNOTE, payNote);


        // Update the "foods" table with new values for a specific food
        int paymentrowsUpdated = db.update(TABLE_NAME_PAYMENT, values, COLUMN_PAYMENTNAME + " = ?", new String[]{originalPaymentName});

        return paymentrowsUpdated > 0; // Return true if one or more rows were updated
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        // Create the "users" table by executing the SQL statement
        database.execSQL(DATABASE_CREATE_USERS);

        // Create the "foods" table by executing the SQL statement
        database.execSQL(DATABASE_CREATE_FOODS);

        // Create the "payment" table by executing the SQL statement
        database.execSQL(DATABASE_CREATE_PAYMENT);

        database.execSQL(DATABASE_CREATE_DOCUMENTS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop the "users" and "foods" tables if they exist
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_FOODS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_PAYMENT);

        // Recreate the tables by calling the onCreate method
        onCreate(db);
    }
    public static String getCurrentDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }

    public static boolean isDateNearExpiry(String currentDate, String expiryDate) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

        try {
            Date currentDateObj = dateFormat.parse(currentDate);
            Date expiryDateObj = dateFormat.parse(expiryDate);

            // Define your threshold here (for example, 7 days)
            long thresholdMillis = 7 * 24 * 60 * 60 * 1000;

            return (expiryDateObj.getTime() - currentDateObj.getTime()) <= thresholdMillis;
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
    }
}
