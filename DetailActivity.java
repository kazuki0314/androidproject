package com.example.expirypal;




import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class DetailActivity extends AppCompatActivity {

    private Button backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.faqpage1);

        // 詳細情報を受け取る
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String selectedItem = extras.getString("selectedItem");
            String itemDetail = extras.getString("itemDetail");

            // `faqpage1.xml` の適切なビューに設定して表示
            TextView selectedItemTextView = findViewById(R.id.titleTextView);
            TextView itemDetailTextView = findViewById(R.id.detailTextView);

            selectedItemTextView.setText(selectedItem);
            itemDetailTextView.setText(itemDetail);
        }

        // 「戻る」ボタンを取得
        backButton = findViewById(R.id.fqlistbackButton);

        // ボタンにクリックリスナーを設定
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // DetailActivity を終了して元のアクティビティ (faq) に戻る
                finish();
            }
        });
    }
}

