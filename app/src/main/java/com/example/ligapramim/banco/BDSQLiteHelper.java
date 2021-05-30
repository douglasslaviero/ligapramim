package com.example.ligapramim.banco;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.ligapramim.Contact;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;


public class BDSQLiteHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "ContactDb";
    private static final String CONTACT_TABLE = "contacts";

    private static final String ID = "id";
    private static final String NAME = "name";
    private static final String PHONE = "phone";
    private static final String OBSERVATION = "observation";
    private static final String IMAGE = "photo";

    private static final String[] COLUNAS = { ID, NAME, PHONE, OBSERVATION, IMAGE };

    public BDSQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE contacts ("+
                "id INTEGER PRIMARY KEY AUTOINCREMENT,"+
                "name TEXT,"+
                "phone INTEGER,"+
                "observation TEXT,"+
                "photo BLOB)";
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS contacts");
        this.onCreate(db);
    }

    public void addContact(Contact contact) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(NAME, contact.getName());
        values.put(PHONE, contact.getPhoneNumber());
        values.put(OBSERVATION, contact.getObservation());
        values.put(IMAGE, getImageBytes(contact.getPhoto()));
        db.insert(CONTACT_TABLE, null, values);
        db.close();
    }

    public Contact getContact(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(CONTACT_TABLE, // a. tabela
                COLUNAS, // b. colunas
                " id = ?", // c. colunas para comparar
                new String[] { String.valueOf(id) }, // d. parâmetros
                null, // e. group by
                null, // f. having
                null, // g. order by
                null); // h. limit
        if (cursor == null) {
            return null;
        } else {
            cursor.moveToFirst();
            Contact contact = cursorToContact(cursor);
            return contact;
        }
    }

    private byte[] getImageBytes(Bitmap bitmap){
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream);
        return stream.toByteArray();
    }

    private Bitmap getImageBitmap(byte[] image){
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }

    private Contact cursorToContact(Cursor cursor) {
        Contact contact = new Contact();
        contact.setId(Integer.parseInt(cursor.getString(0)));
        contact.setName(cursor.getString(1));
        contact.setPhoneNumber(cursor.getString(2));
        contact.setObservation(cursor.getString(3));
        contact.setPhoto(getImageBitmap(cursor.getBlob(4)));
        return contact;
    }

    public ArrayList<Contact> getAllContacts() {
        ArrayList<Contact> contactList = new ArrayList<Contact>();
        String query = "SELECT * FROM " + CONTACT_TABLE + " ORDER BY " + NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                Contact contact = cursorToContact(cursor);
                contactList.add(contact);
            } while (cursor.moveToNext());
        }
        return contactList;
    }

    public int updateContact(Contact contact) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(NAME, contact.getName());
        values.put(PHONE, contact.getPhoneNumber());
        values.put(OBSERVATION, contact.getObservation());
        values.put(IMAGE, getImageBytes(contact.getPhoto()));
        int i = db.update(CONTACT_TABLE, //tabela
                values, // valores
                ID+" = ?", // colunas para comparar
                new String[] { String.valueOf(contact.getId()) }); //parâmetros
        db.close();
        return i; // número de linhas modificadas
    }

    public int deleteContact(Contact contact) {
        SQLiteDatabase db = this.getWritableDatabase();
        int i = db.delete(CONTACT_TABLE, //tabela
                ID+" = ?", // colunas para comparar
                new String[] { String.valueOf(contact.getId()) });
        db.close();
        return i; // número de linhas excluídas
    }
}
