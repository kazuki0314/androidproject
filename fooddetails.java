package com.example.expirypal;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class fooddetails extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.food_detail);

        ImageButton fdback = findViewById(R.id.fdbkbutton);
        ImageButton fdeedit = findViewById(R.id.fdeditbutton);

        fdback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent fdintent = new Intent(fooddetails.this, food.class);
                startActivity(fdintent);
            }
        });

        fdeedit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent fdedtintent = new Intent(fooddetails.this, editfood.class);
                startActivity(fdedtintent);
            }
        });

    }
}
