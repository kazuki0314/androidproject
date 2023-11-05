package com.example.expirypal;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class faq extends AppCompatActivity {

    private Button feedback, support;
    private ImageButton hbackp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.faq_page);

        feedback = findViewById(R.id.fbbutton);
        support = findViewById(R.id.spbutton);
        hbackp = findViewById(R.id.hpback);


        feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent feedintent = new Intent(faq.this, feedbackp.class);
                startActivity(feedintent);
            }
        });

        support.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent supportintent = new Intent(faq.this, supportp.class);
                startActivity(supportintent);
            }
        });


        hbackp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent helpintent = new Intent(faq.this, home.class);
                startActivity(helpintent);
            }
        });
    }
}
