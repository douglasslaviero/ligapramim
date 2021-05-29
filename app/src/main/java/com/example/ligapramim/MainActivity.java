package com.example.ligapramim;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.snackbar.Snackbar;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private final List<Contact> contactsList = new ArrayList<>();
    private final ContactAdapter adapter = new ContactAdapter(contactsList);
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ContactActivity.class);
                startActivity(intent);
            }
        });

        recyclerView = findViewById(R.id.recyclerview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        contactsList.add(new Contact("Joao", Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888), 123456));
        contactsList.add(new Contact("Mario", Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888), 123456));
        contactsList.add(new Contact("Maria", Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888), 123456));
        contactsList.add(new Contact("Julia", Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888), 123456));
        contactsList.add(new Contact("Carlao", Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888), 123456));

        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    Contact deletedContact = null;

    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }


        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            int position = viewHolder.getAdapterPosition();
            switch (direction) {
                case ItemTouchHelper.LEFT:
                    deletedContact = contactsList.get(position);
                    contactsList.remove(position);
                    adapter.notifyItemRemoved(position);
                    Snackbar.make(recyclerView, deletedContact.getName(), Snackbar.LENGTH_LONG)
                            .setAction("Undo", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    contactsList.add(position, deletedContact);
                                    adapter.notifyItemInserted(position);
                                }
                            }).show();
                    break;
                case ItemTouchHelper.RIGHT:

                    break;
            }
        }
    };
}