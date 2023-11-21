package com.example.expirypal;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class home extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_page); // Set the content view to the home_page layout

        ImageButton logoutButton = findViewById(R.id.logoutbtn); // Find the logout button by its ID
        ImageButton faqButton = findViewById(R.id.faqbtn); // Find the FAQ button by its ID
        ImageButton fooditembtn = findViewById(R.id.imgFood); // Find the food item button by its ID
        ImageButton paymentbtn = findViewById(R.id.imgPayment); // Find the payment button by its ID
        ImageButton docbtn = findViewById(R.id.imgDoc); // Find the document button by its ID

        // Set click listener for the "Logout" button
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show a confirmation dialog
                AlertDialog.Builder builder = new AlertDialog.Builder(home.this);
                builder.setMessage("Are you sure you want to log out?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // User clicked Yes button
                                // Perform logout action

                                // Then, navigate back to the login page
                                Intent logoutintent = new Intent(home.this, loginapp.class);
                                startActivity(logoutintent);
                                finish(); // Finish the current activity to prevent going back to it
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // User clicked No button
                                // Dismiss the dialog, no action needed
                                dialog.dismiss();
                            }
                        });
                // Create and show the AlertDialog
                builder.create().show();
            }
        });


        // Set click listener for the "FAQ" button
        faqButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to the FAQ page

                // Here, you can navigate to the FAQ page
                Intent faqgointent = new Intent(home.this, faq.class);
                startActivity(faqgointent);
            }
        });

        // Set click listener for the "Food Item" button
        fooditembtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Navigate to the food item page
                Intent fooditemintent = new Intent(home.this, food.class);
                startActivity(fooditemintent);
            }
        });

        // Set click listener for the "Payment" button
        paymentbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Navigate to the payment page
                Intent paymentitemintent = new Intent(home.this, payment.class);
                startActivity(paymentitemintent);
            }
        });

        // Set click listener for the "Document" button
        docbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Navigate to the document page
                Intent documentitemintent = new Intent(home.this, document.class);
                startActivity(documentitemintent);
            }
        });
    }
}
