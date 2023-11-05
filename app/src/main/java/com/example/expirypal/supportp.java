package com.example.expirypal;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class supportp extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.support_page);



       ImageButton supportback = findViewById(R.id.spback);

        supportback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent feedintent = new Intent(supportp.this, faq.class);
                startActivity(feedintent);

            }
        });

    }
}
