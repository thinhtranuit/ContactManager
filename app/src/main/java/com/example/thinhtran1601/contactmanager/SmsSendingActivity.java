package com.example.thinhtran1601.contactmanager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class SmsSendingActivity extends AppCompatActivity {
    EditText phoneNumber, Sms;
    Button sendButton;
    static final String SharedPreferences = "SaveSms";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms_sending);
        phoneNumber = (EditText) findViewById(R.id.phone_number_edit_text);
        Sms = (EditText) findViewById(R.id.sms_edit_text);
        sendButton = (Button) findViewById(R.id.send_button);
        phoneNumber.setText(getIntent().getStringExtra("phoneNumber"));
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uri = "sms:" + phoneNumber.getText().toString();
                Intent smsIntent = new Intent(Intent.ACTION_VIEW);
                smsIntent.setData(Uri.parse(uri));
                smsIntent.putExtra("sms_body",Sms.getText().toString());
                startActivity(smsIntent);
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences sharedPreferences = getSharedPreferences(SharedPreferences,MODE_PRIVATE);
        android.content.SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("sms_body",Sms.getText().toString());
        editor.apply();
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences sharedPreferences = getSharedPreferences(SharedPreferences, MODE_PRIVATE);
        String smsBody = sharedPreferences.getString("sms_body", "");
        if (smsBody.length() != 0){
            Sms.setText(smsBody);
        }
    }
}
