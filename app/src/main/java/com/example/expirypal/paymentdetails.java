package com.example.expirypal;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class paymentdetails extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.payment_detail);

        ImageButton pdetailsback = findViewById(R.id.pdback);
        ImageButton pdeditbtn = findViewById(R.id.pdedit);

        pdetailsback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent paydetailsintent = new Intent(paymentdetails.this, payment.class);
                startActivity(paydetailsintent);
            }
        });

        pdeditbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent payeditintent = new Intent(paymentdetails.this, editpayment.class);
                startActivity(payeditintent);
            }
        });
    }
}
