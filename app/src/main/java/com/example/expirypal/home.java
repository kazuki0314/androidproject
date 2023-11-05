package com.example.expirypal;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import androidx.appcompat.app.AppCompatActivity;

public class home extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_page);


        ImageButton logoutButton = findViewById(R.id.logoutbtn);
        ImageButton faqButton = findViewById(R.id.faqbtn);
        ImageButton fooditembtn = findViewById(R.id.imgFood);
        ImageButton paymentbtn = findViewById(R.id.imgPayment);
        ImageButton docbtn = findViewById(R.id.imgDoc);

        // Set click listener for the "Logout" button
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Then, navigate back to the login page
                Intent logoutintent = new Intent(home.this, MainActivity.class);
                startActivity(logoutintent);
                finish();
            }
        });

        // Set click listener for the "FAQ" button
        faqButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Here, you can navigate to the FAQ page
                Intent faqgointent = new Intent(home.this, faq.class);
                startActivity(faqgointent);

            }
        });

        fooditembtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent fooditemintent = new Intent(home.this, food.class);
                startActivity(fooditemintent);
            }
        });

        paymentbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent paymentitemintent = new Intent(home.this, payment.class);
                startActivity(paymentitemintent);
            }
        });

        docbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent documentinten = new Intent(home.this, document.class);
                startActivity(documentinten);
            }
        });

    }
}
