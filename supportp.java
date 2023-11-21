package com.example.expirypal;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.graphics.Rect;
import android.view.View;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

public class supportp extends AppCompatActivity {
    EditText editspname, editspPhone, editsptext, editspMultiLine;
    Spinner spdrop1;
    Button spsendbt, spaddbt;
    ImageButton spback;
    ArrayList<Uri> attachments = new ArrayList<>(); // Store attached files URIs
    AttachmentListAdapter attachmentListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.support_page);

        // Initialize your EditText and Spinner views
        editspname = findViewById(R.id.editspname);
        editspPhone = findViewById(R.id.editspPhone);
        editsptext = findViewById(R.id.editsptext);
        editspMultiLine = findViewById(R.id.editspMultiLine);
        spdrop1 = findViewById(R.id.spdrop1);

        // Create an ArrayAdapter for the Spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.problem_categories, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spdrop1.setAdapter(adapter);

        // Set a listener for the Spinner to get the selected item
        spdrop1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // You can get the selected category here if needed
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Do nothing
            }
        });

        // Initialize the attachmentsListView and create an adapter
        ListView attachmentsListView = findViewById(R.id.attachmentsListView);
        attachmentListAdapter = new AttachmentListAdapter(this, attachments);
        attachmentsListView.setAdapter(attachmentListAdapter);

        // Initialize the spaddbt Button and set an OnClickListener
        spaddbt = findViewById(R.id.spaddbt);
        spaddbt.setOnClickListener(v -> onAddButtonClick());

        // Initialize the spsendbt Button and set an OnClickListener
        spsendbt = findViewById(R.id.spsendbt);
        spsendbt.setOnClickListener(v -> {
            if (validateFields()) {
                sendEmail();
            }
        });

        spback = findViewById(R.id.spback);
        spback.setOnClickListener(v -> onBackButtonClick());

        // キーボード表示状態を監視するリスナーを追加
        View rootView = findViewById(android.R.id.content);
        rootView.getViewTreeObserver().addOnGlobalLayoutListener(() -> {
            Rect r = new Rect();
            rootView.getWindowVisibleDisplayFrame(r);
            int screenHeight = rootView.getHeight();

            int keypadHeight = screenHeight - r.bottom;

            if (keypadHeight > screenHeight * 0.15) { // キーボードが15%以上表示されている場合
                // キーボードが表示されたときの処理をここに追加

                // 例: リストビューをスクロールしないようにする
                attachmentsListView.setSelection(0); // リストビューの位置を最上部に設定
            } else {
                // キーボードが非表示になったときの処理をここに追加
            }
        });
    }

    private void onAddButtonClick() {
        // Use an Intent to open the file picker
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*"); // Allow all file types to be selected
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        // Start the file picker activity
        startActivityForResult(intent, 1);
    }

    // Handle the result of the file picker activity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK) {
            if (data != null) {
                Uri selectedFileUri = data.getData();
                attachments.add(selectedFileUri); // Store the selected file URI

                // Notify the adapter that the data set has changed
                attachmentListAdapter.notifyDataSetChanged();

                // You can display the attached file name or other information as needed
                // For example, you can show a list of attached files on the UI
            }
        }
    }

    private boolean validateFields() {
        String senderName = editspname.getText().toString();
        String content = editspMultiLine.getText().toString();
        String category = spdrop1.getSelectedItem().toString();

        if (senderName.isEmpty()) {
            Toast.makeText(this, "Please enter your name", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (category.equals("Select Categories")) {
            Toast.makeText(this, "Please select a category",Toast.LENGTH_SHORT).show();
            return false;
        }

        if (content.isEmpty()) {
            Toast.makeText(this, "Please enter your problem", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private void sendEmail() {
        String recipient = "seproject5001@gmail.com";
        String content = editspMultiLine.getText().toString();
        String senderName = editspname.getText().toString();
        String senderPhone = editspPhone.getText().toString();
        String category = spdrop1.getSelectedItem().toString();

        String emailBody = "Sender Name: " + senderName + "\n"
                + "Sender Phone: " + senderPhone + "\n"
                + "Problem Category: " + category + "\n\n"
                + "Message:\n" + content;

        // Create an Intent with ACTION_SEND
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setType("text/plain");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{recipient});
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, editsptext.getText().toString()); // Set the subject here
        emailIntent.putExtra(Intent.EXTRA_TEXT, emailBody);

        // Attach the selected files
        for (Uri attachment : attachments) {
            emailIntent.putExtra(Intent.EXTRA_STREAM, attachment);
        }

        try {
            startActivity(Intent.createChooser(emailIntent, "Send Email"));
            // Show a success Toast message
            Toast.makeText(this, "Email draft created successfully", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            // Show an error Toast message if sending fails
            Toast.makeText(this, "Failed to create email draft", Toast.LENGTH_SHORT).show();
        }
    }

    private void onBackButtonClick() {
        // Finish the current activity to go back to the previous one (faq.class)
        finish();
    }
}
