package com.example.expirypal;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class document extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.document_page);

        ImageButton docback = findViewById(R.id.docbackbtn);
        ImageButton docdetails = findViewById(R.id.imgDoc1);
        ImageButton docaddbtn = findViewById(R.id.docadd);

        docback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent docgointent = new Intent(document.this, home.class);
                startActivity(docgointent);
            }
        });


        docdetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent docdetailintent = new Intent(document.this, documentdetails.class);
                startActivity(docdetailintent);
            }
        });

        docaddbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent docaddintent = new Intent(document.this, adddocument.class);
                startActivity(docaddintent);
            }
        });
    }
}
