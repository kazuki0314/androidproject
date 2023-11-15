package com.example.expirypal;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class feedbackp extends AppCompatActivity {
    EditText editfbname, editfbPhone, editTextText2, editTextTextMultiLine;
    Spinner fbdrop1;
    Button sendEmailButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feedback);

        // Initialize your EditText and Spinner views
        editfbname = findViewById(R.id.editfbname);
        editfbPhone = findViewById(R.id.editfbPhone);
        editTextText2 = findViewById(R.id.editTextText2);
        editTextTextMultiLine = findViewById(R.id.editTextTextMultiLine);
        fbdrop1 = findViewById(R.id.fbdrop1);

        // Create an ArrayAdapter for the Spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.feedback_categories, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        fbdrop1.setAdapter(adapter);

        // Set a listener for the Spinner to get the selected item
        fbdrop1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // You can get the selected category here if needed
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Do nothing
            }
        });

        // Initialize the sendEmailButton and set an OnClickListener
        sendEmailButton = findViewById(R.id.fbsendbt);
        sendEmailButton.setOnClickListener(v -> {
            if (validateFields()) {
                sendEmail();
            }
        });

        ImageButton backButton = findViewById(R.id.fbback);
        backButton.setOnClickListener(v -> {
            // Call finish() to close the current activity and go back to the previous one
            finish();
        });
    }

    private boolean validateFields() {
        String senderName = editfbname.getText().toString();
        String content = editTextTextMultiLine.getText().toString();
        String category = fbdrop1.getSelectedItem().toString();

        if (senderName.isEmpty()) {
            Toast.makeText(this, "Please enter your name", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (category.equals("Select Categories")) {
            Toast.makeText(this, "Please select a category", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (content.isEmpty()) {
            Toast.makeText(this, "Please enter your feedback", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private void sendEmail() {
        String recipient = "seproject5001@gmail.com";
        String content = editTextTextMultiLine.getText().toString();
        String senderName = editfbname.getText().toString();
        String senderPhone = editfbPhone.getText().toString();
        String category = fbdrop1.getSelectedItem().toString();

        String emailBody = "Sender Name: " + senderName + "\n"
                + "Sender Phone: " + senderPhone + "\n"
                + "Feedback Category: " + category + "\n\n"
                + "Message:\n" + content;

        // Create an Intent with ACTION_SEND
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setType("text/plain");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{recipient});
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, editTextText2.getText().toString()); // Set the subject here
        emailIntent.putExtra(Intent.EXTRA_TEXT, emailBody);

        try {
            startActivity(Intent.createChooser(emailIntent, "Send Email"));
            // 送信成功のトーストメッセージを表示
            Toast.makeText(this, "Draft Copied Successfully", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            // 送信に失敗した場合のエラーをキャッチし、トーストメッセージで通知
            Toast.makeText(this, "Failed to copy draft", Toast.LENGTH_SHORT).show();
        }
    }
}
