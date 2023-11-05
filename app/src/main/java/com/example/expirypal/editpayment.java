package com.example.expirypal;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class editpayment extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_payment_details);

        ImageButton editpayback = findViewById(R.id.epdback);


        editpayback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent payeditbackintent = new Intent(editpayment.this, paymentdetails.class);
                startActivity(payeditbackintent);
            }
        });
    }
}
