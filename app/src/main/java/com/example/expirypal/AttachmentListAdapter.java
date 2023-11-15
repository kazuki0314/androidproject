package com.example.expirypal;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class AttachmentListAdapter extends ArrayAdapter<Uri> {
    private Context context;
    private ArrayList<Uri> attachments;

    public AttachmentListAdapter(Context context, ArrayList<Uri> attachments) {
        super(context, 0, attachments);
        this.context = context;
        this.attachments = attachments;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(android.R.layout.simple_list_item_1, parent, false);
        }

        Uri attachmentUri = attachments.get(position);
        if (attachmentUri != null) {
            String attachmentName = getAttachmentName(attachmentUri);
            TextView attachmentTextView = convertView.findViewById(android.R.id.text1);
            attachmentTextView.setText(attachmentName);
        }

        return convertView;
    }

    private String getAttachmentName(Uri attachmentUri) {
        // Implement a method to extract the attachment name from the URI
        // For example, you can use ContentResolver to query the display name
        // Here's a simplified example:
        String attachmentName = attachmentUri.getLastPathSegment();
        return attachmentName;
    }
}