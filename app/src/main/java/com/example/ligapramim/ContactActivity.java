package com.example.ligapramim;

import android.os.Bundle;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class ContactActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        final EditText name = (EditText) findViewById(R.id.txtContactName);
        final EditText number = (EditText) findViewById(R.id.txtContactNumber);
    }
}
