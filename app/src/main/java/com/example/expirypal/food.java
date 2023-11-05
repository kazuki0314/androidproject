package com.example.expirypal;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class food extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.food_item_page);

        ImageButton fooditemsback = findViewById(R.id.fiback);
        ImageButton food1 = findViewById(R.id.imgFood1);
        ImageButton foodadd = findViewById(R.id.fiadd);

        fooditemsback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent faqgointent = new Intent(food.this, home.class);
                startActivity(faqgointent);

            }
        });

        food1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent fooddetailintent = new Intent(food.this, fooddetails.class);
                startActivity(fooddetailintent);
            }
        });

        foodadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addfoodintent = new Intent(food.this, addfood.class);
                startActivity(addfoodintent);
            }
        });

    }
}
