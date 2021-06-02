package com.example.ligapramim;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ligapramim.banco.BDSQLiteHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

//import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class MainActivity extends AppCompatActivity implements ContactAdapter.OnContactListener {

    private final int REQUEST_PHONE_CALL = 4;
    private final int PERMISSION_REQUEST = 2;
    ArrayList<Contact> contactsList;
    private final ContactAdapter adapter = new ContactAdapter(contactsList, this);
    private RecyclerView recyclerView;
    private BDSQLiteHelper bd;
    Contact deletedContact = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bd = new BDSQLiteHelper(this);

        // Pede permissão para acessar as mídias gravadas no dispositivo
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            if (!ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        PERMISSION_REQUEST);
            }
        }

        // Pede permissão para fazer ligações
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_PHONE_CALL);
        }

        // Pede permissão para escrever arquivos no dispositivo
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            if (!ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        PERMISSION_REQUEST);
            }
        }

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

        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    @Override
    protected void onStart() {
        super.onStart();
        //bd.onUpgrade(bd.getWritableDatabase(), 0, 0);
        contactsList = bd.getAllContacts();

        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        ContactAdapter adapter = new ContactAdapter(contactsList, this);
        recyclerView.setAdapter(adapter);

    }

    int direction = 0;

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
                    Intent intent = new Intent(MainActivity.this, ContactActivity.class);
                    intent.putExtra("ID", contactsList.get(position).getId());
                    startActivity(intent);
                    break;
                case ItemTouchHelper.RIGHT:
                    delete(position);
                    break;
            }
        }

        @Override
        public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder,
                                float dX, float dY, int actionState, boolean isCurrentlyActive) {
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            int iconResource = 0;
            int left = 0, top = 0, right = 0, bottom = 0;

            if (dX > 0) {
                c.clipRect(dX, viewHolder.itemView.getTop(), 0f, viewHolder.itemView.getBottom());
                c.drawColor(Color.RED);
                iconResource = R.drawable.ic_baseline_delete_24;
                        top =viewHolder.itemView.getTop()+50;
                                right = 200;
                                        bottom = viewHolder.itemView.getBottom()-50;

            } else {
                c.clipRect(viewHolder.itemView.getRight() + dX, viewHolder.itemView.getTop(), viewHolder.itemView.getRight(), viewHolder.itemView.getBottom());
                c.drawColor(Color.BLUE);
                iconResource = R.drawable.ic_baseline_edit_24;
                left = viewHolder.itemView.getRight() - 200;
                top = viewHolder.itemView.getTop() + 50;
                right = viewHolder.itemView.getRight();
                bottom = viewHolder.itemView.getBottom() - 50;
            }

            Drawable icon = ContextCompat.getDrawable(getBaseContext(), iconResource);
            icon.setBounds(left, top, right, bottom);
            icon.draw(c);
        }
    };

    private void delete(int position) {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        deletedContact = contactsList.get(position);
                        contactsList.remove(position);
                        adapter.notifyItemRemoved(position);
                        bd.deleteContact(deletedContact);
                        break;
                    case DialogInterface.BUTTON_NEGATIVE:
                        break;
                }
                startActivity(getIntent());
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setMessage("Tem certeza que deseja deletar?").setPositiveButton("SIM", dialogClickListener)
                .setNegativeButton("NÃO", dialogClickListener).

                show();
    }

    @Override
    public void onContactClick(int position) {
        Intent intent = new Intent(MainActivity.this, CallMakerActivity.class);
        intent.putExtra("ID", contactsList.get(position).getId());
        startActivity(intent);
    }
}