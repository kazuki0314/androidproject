package com.example.expirypal;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class addpayment extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_payment_details);

        ImageButton addpyback = findViewById(R.id.apdback);

        addpyback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addpaybackintent = new Intent(addpayment.this, payment.class);
                startActivity(addpaybackintent);
            }
        });
    }
}
