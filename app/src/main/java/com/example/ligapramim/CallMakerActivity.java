package com.example.ligapramim;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ligapramim.banco.BDSQLiteHelper;

public class CallMakerActivity extends AppCompatActivity {
    private BDSQLiteHelper bd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call_maker);

        Intent intent = getIntent();
        final int id = intent.getIntExtra("ID", 0);
        bd = new BDSQLiteHelper(this);
        Contact contact = bd.getContact(id);

        final TextView name =  findViewById(R.id.txtContactName);
        final TextView number = findViewById(R.id.txtContactNumber);
        final ImageView photoBtn = findViewById(R.id.photoBtn);

        name.setText(contact.getName());
        number.setText(contact.getPhoneNumber());
        photoBtn.setImageBitmap(contact.getPhoto());
    }

    public void call(View view){
        TextView number = findViewById(R.id.txtContactNumber);
        String numberTxt = number.getText().toString();
        Intent phoneIntent = new Intent(Intent.ACTION_CALL);
        phoneIntent.setData(Uri.parse("tel:"+numberTxt));
        startActivity(phoneIntent);
}
    public void cancel(View view){
        returnToMainActivity();
    }

    private void returnToMainActivity(){
        Intent intent = new Intent(CallMakerActivity.this, MainActivity.class);
        startActivity(intent);
    }
}
