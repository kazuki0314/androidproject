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
    private static final String DATABASE_NAME = "ep.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_NAME_USERS = "users";
    public static final String COLUMN_USERID = "userid";
    public static final String COLUMN_USERNAME = "usern";
    public static final String COLUMN_PASSWORD = "pass";
    public static final String COLUMN_EMAIL = "email";

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

    private static final String DATABASE_CREATE_USERS = "create table " + TABLE_NAME_USERS +
            " (" + COLUMN_USERID + " integer primary key autoincrement, " +
            COLUMN_USERNAME + " text not null, " +
            COLUMN_PASSWORD + " text not null, " +
            COLUMN_EMAIL + " text not null);";

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




    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public boolean updateFoodDetails(String originalFoodName, String newFoodName, String expiryDate, String reminderDate, String quantity, String category, String note) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_FOODNAME, newFoodName);
        values.put(COLUMN_EDATE, expiryDate);
        values.put(COLUMN_RDATE, reminderDate);
        values.put(COLUMN_QUANTITY, quantity);
        values.put(COLUMN_CATEGORY, category);
        values.put(COLUMN_NOTE, note);

        int rowsUpdated = db.update(TABLE_NAME_FOODS, values, COLUMN_FOODNAME + " = ?", new String[]{originalFoodName});

        return rowsUpdated > 0;
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


    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE_USERS);
        database.execSQL(DATABASE_CREATE_FOODS);
        database.execSQL(DATABASE_CREATE_DOCUMENTS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_FOODS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_DOCUMENTS);
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
