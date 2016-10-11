package com.example.thinhtran1601.contactmanager;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    //Item of layout
    private EditText phoneEditText, nameEditText;
    private Button saveButton;
    private ListView contactListView;
    private List<Contact> contacts;
    private ContactAdapter contactAdapter;

    //Use for database
    private String DATABASE_NAME = "dbContact.sqlite";
    private static final String DB_Path = "/databases/";
    SQLiteDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupDatabase();
        addControls();
        addItemFromDatabase();
        addEvents();
    }

    private void setupDatabase() {
        File dbFile = new File(getApplicationInfo().dataDir + DB_Path + DATABASE_NAME);
        if (!dbFile.exists()) {
            copyDataBaseFromAsset();
            Toast.makeText(this, "Create Database from Assests folder", Toast.LENGTH_SHORT).show();
        }
    }

    private void copyDataBaseFromAsset() {
        try {
            InputStream inputStream = getAssets().open(DATABASE_NAME);
            String outFileName = getApplicationInfo().dataDir + DB_Path + DATABASE_NAME;

            // if the path doesn't exist first, create it
            File f = new File(getApplicationInfo().dataDir + DB_Path);
            if (!f.exists()) {
                f.mkdir();
            }

            OutputStream outputStream = new FileOutputStream(outFileName);

            //
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }
            outputStream.flush();
            outputStream.close();
            inputStream.close();
        } catch (Exception e) {
        }
    }

    private void addEvents() {
        contactListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.button_list);
                linearLayout.setVisibility(View.VISIBLE);
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //get info from form
                String name = nameEditText.getText().toString();
                String phoneNumber = phoneEditText.getText().toString();

                //create a Contact Object to add
                Contact contact1 = new Contact(name, phoneNumber);

                //add contact1 object to database
                int temp = 0;
                for (Contact contact : contacts) {
                    if (contact.getName().equals(name) && contact.getPhoneNumber().equals(phoneNumber)){
                        Toast.makeText(MainActivity.this, "This contact does exists", Toast.LENGTH_SHORT).show();
                        temp = 1;
                        break;
                    }
                    if (contact.getName().equals(name)) {
                        replaceContact(contact1);
                        temp = 1;
                        break;
                    }
                }
                if (temp == 0) {
                    addContact(contact1);
                }
            }
        });
    }

    private void replaceContact(Contact contact) {
        String updateQuery = "update Contact set PhoneNumber = '"
                +contact.getPhoneNumber() + "' where Name = '" + contact.getName()+ "'";
        database.execSQL(updateQuery);
        Toast.makeText(this, "Update contact successfully", Toast.LENGTH_SHORT).show();
        contacts.add(contact);
        contactAdapter.notifyDataSetChanged();
    }

    private void addContact(Contact contact) {
        String insertQuery = "insert into Contact('Name','PhoneNumber') " +
                "values('" + contact.getName() + "','" + contact.getPhoneNumber() + "')";
        database.execSQL(insertQuery);
        contacts.add(contact);
        contactAdapter.notifyDataSetChanged();
        Toast.makeText(this, "Add contact successfully", Toast.LENGTH_SHORT).show();
    }

    private void addControls() {
        phoneEditText = (EditText) findViewById(R.id.phone_number_edit_text);
        nameEditText = (EditText) findViewById(R.id.name_edit_text);
        saveButton = (Button) findViewById(R.id.save_button);
        contactListView = (ListView) findViewById(R.id.contact_list_view);

        contacts = new ArrayList<>();
        database = openOrCreateDatabase(DATABASE_NAME, MODE_PRIVATE, null);
        contactAdapter = new ContactAdapter(this, R.layout.item, contacts, database);
        contactListView.setAdapter(contactAdapter);
    }

    private void addItemFromDatabase() {
        Cursor cursor = database.rawQuery("select * from Contact order by Name ASC", null);
        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String name = cursor.getString(1);
            String phoneNumber = cursor.getString(2);
            contacts.add(new Contact(name, phoneNumber));
        }
        cursor.close();
        contactAdapter.notifyDataSetChanged();
    }
}
