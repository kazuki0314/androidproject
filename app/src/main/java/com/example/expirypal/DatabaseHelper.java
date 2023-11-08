package com.example.expirypal;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "ep.db"; // Name of the SQLite database
    private static final int DATABASE_VERSION = 1; // Database version

    // Define table and column names for the "users" table
    public static final String TABLE_NAME_USERS = "users"; // Name of the users table
    public static final String COLUMN_USERNAME = "usern"; // Username column name
    public static final String COLUMN_PASSWORD = "pass"; // Password column name
    public static final String COLUMN_EMAIL = "email"; // Email column name

    // Define table and column names for the "foods" table
    public static final String TABLE_NAME_FOODS = "foods"; // Name of the foods table
    public static final String COLUMN_FOODNAME = "foodname"; // Food name column name
    public static final String COLUMN_EDATE = "expdate"; // Expiry date column name
    public static final String COLUMN_RDATE = "remdate"; // Reminder date column name
    public static final String COLUMN_QUANTITY = "quantity"; // Quantity column name
    public static final String COLUMN_CATEGORY = "category"; // Category column name
    public static final String COLUMN_NOTE = "note"; // Note column name

    // SQL statement to create the "users" table
    private static final String DATABASE_CREATE_USERS = "create table " + TABLE_NAME_USERS +
            " (" + COLUMN_USERNAME + " text not null, " +
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

    @Override
    public void onCreate(SQLiteDatabase database) {
        // Create the "users" table by executing the SQL statement
        database.execSQL(DATABASE_CREATE_USERS);

        // Create the "foods" table by executing the SQL statement
        database.execSQL(DATABASE_CREATE_FOODS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop the "users" and "foods" tables if they exist
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_FOODS);

        // Recreate the tables by calling the onCreate method
        onCreate(db);
    }
}
