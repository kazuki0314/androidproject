package com.example.expirypal;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class editfood extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_food_item);

        ImageButton efibk = findViewById(R.id.efiback);

        efibk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent efibkintent = new Intent(editfood.this, fooddetails.class);
                startActivity(efibkintent);
            }
        });
    }
}
