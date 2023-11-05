package com.example.expirypal;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class adddocument extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_document);

        ImageButton addocbtn = findViewById(R.id.addocback);

        addocbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent adddocbkintent = new Intent(adddocument.this, document.class);
                startActivity(adddocbkintent);
            }
        });
    }
}
