package com.example.expirypal;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class payment extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.payment_page);

        ImageButton paybackbtn = findViewById(R.id.pyback);
        ImageButton billdetails = findViewById(R.id.imgBill);
        ImageButton payadd = findViewById(R.id.pyadd);


        paybackbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent paygointent = new Intent(payment.this, home.class);
                startActivity(paygointent);
            }
        });


        //Example on how it works
        billdetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent billdetailintent = new Intent(payment.this, paymentdetails.class);
                startActivity(billdetailintent);
            }
        });

        payadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent paymenntaddintent = new Intent(payment.this, addpayment.class);
                startActivity(paymenntaddintent);
            }
        });

    }
}
