package com.example.expirypal;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class faqpage1 extends AppCompatActivity {

    private Button backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.faqpage1);

        // ボタンを取得
        backButton = findViewById(R.id.fqlistbackButton);

        // ボタンにクリックリスナーを設定
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // faq アクティビティに戻る（アクティビティを終了）
                finish();
            }
        });
    }
}
