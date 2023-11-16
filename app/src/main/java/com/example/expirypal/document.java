package com.example.expirypal;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

public class document extends AppCompatActivity {
    private ListView docListView;
    private DatabaseHelper dbHelper2;
    private ArrayAdapter<String> adapter;

    private static final int ADD_DOCUMENT_REQUEST = 1; // Request code for adding documents

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.document_page);

        dbHelper2 = new DatabaseHelper(this);

        ImageButton docBackBtn = findViewById(R.id.docbackbtn);
        ImageButton docAddBtn = findViewById(R.id.docadd);
        EditText docSearchEditText = findViewById(R.id.docsearch);
        docListView = findViewById(R.id.docListView);

        docBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent homedocbackfromIntent = new Intent(document.this, home.class);
                startActivity(homedocbackfromIntent);
            }
        });

        docAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addDocumentIntent = new Intent(document.this, adddocument.class);
                startActivityForResult(addDocumentIntent, ADD_DOCUMENT_REQUEST);
            }
        });

        docSearchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        docListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedDocumentDetails = adapter.getItem(position);

                Intent editDocumentIntent = new Intent(document.this, editdocument.class);
                editDocumentIntent.putExtra("selectedDocumentDetails", selectedDocumentDetails);
                startActivity(editDocumentIntent);
            }
        });

        docListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                showDeleteConfirmationDialog(position);
                return true;
            }
        });

        refreshDocumentList();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ADD_DOCUMENT_REQUEST && resultCode == RESULT_OK) {
            // If a new document was added successfully, refresh the list
            refreshDocumentList();
        }
    }

    private void refreshDocumentList() {
        ArrayList<String> documentItemsWithDetails = new ArrayList<>();
        SQLiteDatabase db = dbHelper2.getReadableDatabase();
        Cursor cursor2 = db.query(DatabaseHelper.TABLE_NAME_DOCUMENTS,
                new String[]{
                        DatabaseHelper.COLUMN_DOCNAME,
                        DatabaseHelper.COLUMN_RENEWAL_DATE,
                        DatabaseHelper.COLUMN_RDATE_DOC,
                        DatabaseHelper.COLUMN_DOCNUM,
                        DatabaseHelper.COLUMN_SENDTO,
                        DatabaseHelper.COLUMN_LOCATION,
                        DatabaseHelper.COLUMN_NOTES,
                        DatabaseHelper.COLUMN_ATTACHMENT},
                null, null, null, null, null);

        while (cursor2.moveToNext()) {
            String docName = cursor2.getString(cursor2.getColumnIndex(DatabaseHelper.COLUMN_DOCNAME));
            String renewalDate = cursor2.getString(cursor2.getColumnIndex(DatabaseHelper.COLUMN_RENEWAL_DATE));
            String reminder = cursor2.getString(cursor2.getColumnIndex(DatabaseHelper.COLUMN_RDATE_DOC));
            String documentNumber = cursor2.getString(cursor2.getColumnIndex(DatabaseHelper.COLUMN_DOCNUM));
            String sendTo = cursor2.getString(cursor2.getColumnIndex(DatabaseHelper.COLUMN_SENDTO));
            String locationStored = cursor2.getString(cursor2.getColumnIndex(DatabaseHelper.COLUMN_LOCATION));
            String notes = cursor2.getString(cursor2.getColumnIndex(DatabaseHelper.COLUMN_NOTES));
            String attachment = cursor2.getString(cursor2.getColumnIndex(DatabaseHelper.COLUMN_ATTACHMENT));

            String details = "Document Name: " + docName +
                    "\nRenewal Date: " + renewalDate +
                    "\nReminder: " + reminder +
                    "\nDocument Number: " + documentNumber +
                    "\nSend To: " + sendTo +
                    "\nLocation Stored: " + locationStored +
                    "\nNotes: " + notes +
                    "\nAttachment: " + attachment;

            documentItemsWithDetails.add(details);
        }

        cursor2.close();
        db.close();

        if (adapter == null) {
            adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, documentItemsWithDetails);
            docListView.setAdapter(adapter);
        } else {
            adapter.clear();
            adapter.addAll(documentItemsWithDetails);
            adapter.notifyDataSetChanged();
        }
    }

    private void showDeleteConfirmationDialog(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Do you want to delete this document?")
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        deleteDocument(position);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User canceled the deletion, so do nothing
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void deleteDocument(int position) {
        String selectedDocumentDetails = adapter.getItem(position);
        String docName = getDocumentNameFromDetails(selectedDocumentDetails);

        SQLiteDatabase db = dbHelper2.getWritableDatabase();
        int rowsDeleted = db.delete(DatabaseHelper.TABLE_NAME_DOCUMENTS, DatabaseHelper.COLUMN_DOCNAME + "=?", new String[]{docName});
        db.close();

        if (rowsDeleted > 0) {
            refreshDocumentList();
        }
    }

    private String getDocumentNameFromDetails(String details) {
        String[] lines = details.split("\n");
        if (lines.length > 0) {
            String line = lines[0];
            int colonIndex = line.indexOf(": ");
            if (colonIndex >= 0 && colonIndex < line.length() - 2) {
                return line.substring(colonIndex + 2);
            }
        }
        return "";
    }
}
