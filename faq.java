package com.example.expirypal;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class faq extends AppCompatActivity {
    private ListView faqListView;
    private ArrayAdapter<String> faqAdapter;
    private String[] faqItems;
    private String[] itemDetails;

    private Button feedback, support;
    private ImageButton hbackp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.faq_page);

        feedback = findViewById(R.id.fbbutton);
        support = findViewById(R.id.spbutton);
        hbackp = findViewById(R.id.hpback);

        faqListView = findViewById(R.id.faqlist);

        // FAQ アイテムを作成
        faqItems = new String[]{
                "How To Add Item",
                "Test2",
                "Test3"
        };

        // それぞれのアイテムに詳細情報を関連付ける
        itemDetails = new String[]{
                "From Home Page Select Add Food and enter your item details",
                "これはオレンジです。",
                "これはブドウです。"
        };

        // リストビューにアイテムを追加するためのアダプターを作成
        faqAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, faqItems);

        // リストビューにアダプターを設定
        faqListView.setAdapter(faqAdapter);

        faqListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = faqItems[position];
                String itemDetail = itemDetails[position];

                Intent detailIntent = new Intent(faq.this, DetailActivity.class);
                detailIntent.putExtra("selectedItem", selectedItem);
                detailIntent.putExtra("itemDetail", itemDetail);
                startActivity(detailIntent);
            }
        });

        feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent feedintent = new Intent(faq.this, feedbackp.class);
                startActivity(feedintent);
            }
        });

        support.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent supportintent = new Intent(faq.this, supportp.class);
                startActivity(supportintent);
            }
        });

        hbackp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent helpintent = new Intent(faq.this, home.class);
                startActivity(helpintent);
            }
        });
    }
}