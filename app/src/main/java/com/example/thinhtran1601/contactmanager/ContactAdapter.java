package com.example.thinhtran1601.contactmanager;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

/**
 * Created by thinh on 02/10/2016.
 */
public class ContactAdapter extends ArrayAdapter<Contact> {
    List<Contact> list;
    final Activity main;
    SQLiteDatabase database;
    public ContactAdapter(Activity context, int resource, List<Contact> objects, SQLiteDatabase database) {
        super(context, resource, objects);
        list = objects;
        main = context;
        this.database = database;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(
                    R.layout.item, parent, false);
        }
        final Contact contact = getItem(position);

        TextView nameTextView = (TextView) view.findViewById(R.id.name_text_view);
        nameTextView.setText(contact.getName());

        TextView phoneTextView = (TextView) view.findViewById(R.id.phone_number_text_view);
        phoneTextView.setText(contact.getPhoneNumber());

        ImageButton call = (ImageButton) view.findViewById(R.id.call_button);
        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uri = "tel:" + contact.getPhoneNumber().trim();
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse(uri));
                main.startActivity(intent);
            }
        });

        ImageButton removeImageButton = (ImageButton)view.findViewById(R.id.remove_button);
        removeImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String deleteQuery = "delete from Contact where Name = '" + contact.getName() +  "' " +
                        "and PhoneNumber = '"+ contact.getPhoneNumber() +"'";
                database.execSQL(deleteQuery);
                list.remove(contact);
                ContactAdapter.this.notifyDataSetChanged();
                Toast.makeText(main, "Delete contact successfully", Toast.LENGTH_SHORT).show();
            }
        });

        ImageButton smsImageButton = (ImageButton)view.findViewById(R.id.sms_button);
        smsImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(main,SmsSendingActivity.class);
                intent.putExtra("phoneNumber",contact.getPhoneNumber());
                main.startActivity(intent);
            }
        });
        return view;
    }
}
