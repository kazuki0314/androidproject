package com.example.expirypal;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class documentdetails extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.document_detail);

        ImageButton docdetback = findViewById(R.id.dnback);
        ImageButton docdetedit = findViewById(R.id.dnedit);


        docdetback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent docdetbkintent = new Intent(documentdetails.this, document.class);
                startActivity(docdetbkintent);
            }
        });

        docdetedit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent docdetdtintent = new Intent(documentdetails.this, editdocument.class);
                startActivity(docdetdtintent);
            }
        });
    }
}
