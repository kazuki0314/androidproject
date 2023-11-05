package com.example.expirypal;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class feedbackp extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feedback);

        ImageButton feedbackback = findViewById(R.id.fbback);

        feedbackback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent feedintent = new Intent(feedbackp.this, faq.class);
                startActivity(feedintent);

            }
        });


    }
}

