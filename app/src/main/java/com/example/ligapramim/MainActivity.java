package com.example.ligapramim;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.example.ligapramim.banco.BDSQLiteHelper;
import com.google.android.material.snackbar.Snackbar;

import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

//import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class MainActivity extends AppCompatActivity implements ContactAdapter.OnContactListener {

    private final int REQUEST_PHONE_CALL = 4;
    private final int PERMISSION_REQUEST = 2;
    ArrayList<Contact> contactsList;
    private final ContactAdapter adapter = new ContactAdapter(contactsList, this);
    private BDSQLiteHelper bd;
    private Contact deletedContact;
    SwipeController swipeController = null;

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

        contactsList = bd.getAllContacts();

        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        swipeController = new SwipeController(new SwipeControllerActions() {
            @Override
            public void onLeftClicked(int position) {
                delete(position, recyclerView);
            }

            @Override
            public void onRightClicked(int position) {
                Intent intent = new Intent(MainActivity.this, ContactActivity.class);
                intent.putExtra("ID", contactsList.get(position).getId());
                startActivity(intent);
            }
        });

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(swipeController);
        itemTouchHelper.attachToRecyclerView(recyclerView);

        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
                swipeController.onDraw(c);
            }
        });
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

//    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
//        @Override
//        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
//            return false;
//        }
//
//        @Override
//        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
//            int position = viewHolder.getAdapterPosition();
//            switch (direction) {
//                case ItemTouchHelper.LEFT:
//                    Intent intent = new Intent(MainActivity.this, ContactActivity.class);
//                    intent.putExtra("ID", contactsList.get(position).getId());
//                    startActivity(intent);
//                    break;
//                case ItemTouchHelper.RIGHT:
//                    if (confirmDelete()) {
//                        deletedContact = contactsList.get(position);
//                        contactsList.remove(position);
//                        adapter.notifyItemRemoved(position);
//                        Snackbar.make(recyclerView, deletedContact.getName(), Snackbar.LENGTH_LONG)
//                                .setAction("Undo", new View.OnClickListener() {
//                                    @Override
//                                    public void onClick(View v) {
//                                        contactsList.add(position, deletedContact);
//                                        adapter.notifyItemInserted(position);
//                                    }
//                                }).show();
//                        bd.deleteContact(deletedContact);
//                    }
//                    break;
//            }
//        }
//
//        int trashBinIcon = R.drawable.ic_baseline_delete_24;
//
//        @Override
//        public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder,
//                                float dX, float dY, int actionState, boolean isCurrentlyActive) {
//            int pos = viewHolder.getAdapterPosition();
//            View itemView = viewHolder.itemView;
//
//            c.clipRect(0f, viewHolder.itemView.getTop(), dX, viewHolder.itemView.getBottom());
//            if (pos < 0)
//                c.drawColor(Color.RED);
//            else
//                c.drawColor(Color.BLUE);
//
//            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
//        }
//    };

    @Override
    public void onContactClick(int position) {
        Intent intent = new Intent(MainActivity.this, CallMakerActivity.class);
        intent.putExtra("ID", contactsList.get(position).getId());
        startActivity(intent);
    }

    private void delete(int position, RecyclerView recyclerView) {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        deletedContact = contactsList.get(position);
                        contactsList.remove(position);
                        adapter.notifyItemRemoved(position);
                        Snackbar.make(recyclerView, deletedContact.getName(), Snackbar.LENGTH_LONG)
                                .setAction("Undo", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        contactsList.add(position, deletedContact);
                                        bd.addContact(deletedContact);
                                        adapter.notifyItemInserted(position);
                                    }
                                }).show();
                        bd.deleteContact(deletedContact);
                        break;
                    case DialogInterface.BUTTON_NEGATIVE:
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setMessage("Tem certeza que deseja deletar?").setPositiveButton("SIM", dialogClickListener)
                .setNegativeButton("NÃO", dialogClickListener).

                show();
    }
}