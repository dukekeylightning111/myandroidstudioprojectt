package com.example.myapplication;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.text.BreakIterator;

public class ContactMeActivity extends AppCompatActivity {
    private Button contactViaPhone;
    private Button contactViaEmail;
    private BroadcastReceiver broadcastReceiver;
    private EditText editTextMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_me);
        init();
        func_contact_via_email();
        func_contact_via_phone();
    }
    public void init(){
        contactViaEmail = findViewById(R.id.contactViaEmailBtn);
        contactViaPhone = findViewById(R.id.contactViaPhoneBtn);
        editTextMessage = findViewById(R.id.editTextMessage);
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
                boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting() && activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE;
                if (!isConnected) {
                    Toast.makeText(context, "אין קליטה", Toast.LENGTH_SHORT).show();
                }
            }
        };

        registerReceiver(broadcastReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }
    public void showNoDataConnectionDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("אין קליטה")
                .setMessage("האם אתם מעוניינים להמשיך?")
                .setPositiveButton("כן", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("לא", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        finish();
                    }
                })
                .setCancelable(false)
                .show();
    }

    public void func_contact_via_email() {
        Toast.makeText(ContactMeActivity.this, "צריך להוסיף", Toast.LENGTH_SHORT).show();
    }

    public void func_contact_via_phone() {
        String phoneNumber = "123456789";
        String message = editTextMessage.getText().toString().trim();

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting() && activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE;

        if (isConnected) {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNumber, null, message, null, null);
            Toast.makeText(ContactMeActivity.this, "הודעה נשלחה", Toast.LENGTH_SHORT).show();
        } else {
            showNoDataConnectionDialog();
        }
    }
}