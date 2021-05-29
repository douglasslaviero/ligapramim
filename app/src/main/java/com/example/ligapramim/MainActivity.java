package com.example.ligapramim;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Here's a Snackbar", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        recyclerView = findViewById(R.id.recyclerview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        final List<Contact> contactsList = new ArrayList<>();
        contactsList.add(new Contact("Joao", Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888), 123456));
        contactsList.add(new Contact("Mario", Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888), 123456));
        contactsList.add(new Contact("Maria", Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888), 123456));
        contactsList.add(new Contact("Julia", Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888), 123456));
        contactsList.add(new Contact("Carlao", Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888), 123456));

        ContactAdapter adapter = new ContactAdapter(contactsList);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
}