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
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import android.content.Context;
import android.graphics.Color;

public class document extends AppCompatActivity {
    private ListView docListView;
    private DatabaseHelper dbHelper2;
    private CustomArrayAdapter adapter;

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
        ArrayList<DocumentDetails> documentItemsWithDetails = new ArrayList<>();
        SQLiteDatabase db = dbHelper2.getReadableDatabase();
        Cursor cursor2 = db.query(DatabaseHelper.TABLE_NAME_DOCUMENTS,
                new String[]{
                        DatabaseHelper.COLUMN_DOCNAME,
                        DatabaseHelper.COLUMN_RENEWAL_DATE,
                        DatabaseHelper.COLUMN_RDATE_DOC,
                        DatabaseHelper.COLUMN_DOCNUM,
                        DatabaseHelper.COLUMN_SENDTO,
                        DatabaseHelper.COLUMN_LOCATION,
                        DatabaseHelper.COLUMN_DOC_NOTES},
//                        DatabaseHelper.COLUMN_ATTACHMENT},
                null, null, null, null, null);

        while (cursor2.moveToNext()) {
            String docName = cursor2.getString(cursor2.getColumnIndex(DatabaseHelper.COLUMN_DOCNAME));
            String renewalDate = cursor2.getString(cursor2.getColumnIndex(DatabaseHelper.COLUMN_RENEWAL_DATE));
            String reminder = cursor2.getString(cursor2.getColumnIndex(DatabaseHelper.COLUMN_RDATE_DOC));
            String documentNumber = cursor2.getString(cursor2.getColumnIndex(DatabaseHelper.COLUMN_DOCNUM));
            String sendTo = cursor2.getString(cursor2.getColumnIndex(DatabaseHelper.COLUMN_SENDTO));
            String locationStored = cursor2.getString(cursor2.getColumnIndex(DatabaseHelper.COLUMN_LOCATION));
            String docnotes = cursor2.getString(cursor2.getColumnIndex(DatabaseHelper.COLUMN_DOC_NOTES));
//            String attachment = cursor2.getString(cursor2.getColumnIndex(DatabaseHelper.COLUMN_ATTACHMENT));

            String details = "Document Name: " + docName +
                    "\nRenewal Date: " + renewalDate +
                    "\nReminder: " + reminder +
                    "\nDocument Number: " + documentNumber +
                    "\nSend To: " + sendTo +
                    "\nLocation Stored: " + locationStored +
                    "\nNotes: " + docnotes;
//                    "\nAttachment: " + attachment;

            // Convert renewalDate to Date for sorting
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            try {
                Date renewalDateObj = dateFormat.parse(renewalDate);
                documentItemsWithDetails.add(new DocumentDetails(renewalDateObj, details));
            } catch (ParseException e) {
                e.printStackTrace();
            }

        }

        cursor2.close();
        db.close();

        // Sort documents by renewal date
        Collections.sort(documentItemsWithDetails, new Comparator<DocumentDetails>() {
            @Override
            public int compare(DocumentDetails d1, DocumentDetails d2) {
                return d1.getRenewalDate().compareTo(d2.getRenewalDate());
            }
        });

        // Convert back to String for display
        ArrayList<String> sortedDocumentItemsWithDetails = new ArrayList<>();
        for (DocumentDetails documentDetails : documentItemsWithDetails) {
            sortedDocumentItemsWithDetails.add(documentDetails.getDetails());
        }

        if (adapter == null) {
            adapter = new CustomArrayAdapter(this, android.R.layout.simple_list_item_1, sortedDocumentItemsWithDetails);
            docListView.setAdapter(adapter);
        } else {
            adapter.clear();
            adapter.addAll(sortedDocumentItemsWithDetails);
            adapter.notifyDataSetChanged();
        }
    }

    private static class DocumentDetails {
        private Date renewalDate;
        private String details;

        public DocumentDetails(Date renewalDate, String details) {
            this.renewalDate = renewalDate;
            this.details = details;
        }

        public Date getRenewalDate() {
            return renewalDate;
        }

        public String getDetails() {
            return details;
        }
    }

    private class CustomArrayAdapter extends ArrayAdapter<String> {
        public CustomArrayAdapter(Context context, int resource, List<String> objects) {
            super(context, resource, objects);
        }

        @Override
        public View getView(int position, View convertView, android.view.ViewGroup parent) {
            View view = super.getView(position, convertView, parent);

            // Set the text color to black
            ((TextView) view).setTextColor(getResources().getColor(android.R.color.black));

            return view;
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