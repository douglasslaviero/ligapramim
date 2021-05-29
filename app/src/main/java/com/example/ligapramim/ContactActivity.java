package com.example.ligapramim;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ContactActivity extends AppCompatActivity {

    private final int GALERY_IMAGES = 1;
    private final int PERMISSION_REQUEST = 2;
    private final int PHOTO = 3;

    private File photoFile = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        final EditText name = (EditText) findViewById(R.id.txtContactName);
        name.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    name.setText("");
                }
            }
        });

        final EditText number = (EditText) findViewById(R.id.txtContactNumber);
        number.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    number.setText("");
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == PHOTO) {
            sendBroadcast(new Intent(
                    Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                    Uri.fromFile(photoFile)
            ));

            showPhoto(photoFile.getAbsolutePath());
        }
    }

    private void showPhoto(String path) {
        Bitmap bitmap = BitmapFactory.decodeFile(path);
        ImageButton ib = findViewById(R.id.photoBtn);
        ib.setImageBitmap(bitmap);
    }

    private File createFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_Hhmmss").format(new Date());
        File folder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        return new File(folder.getPath() + File.separator + "JPG_" + timeStamp + ".jpg");
    }

    private void returnToMainActivity(){
        Intent intent = new Intent(ContactActivity.this, MainActivity.class);
        startActivity(intent);
    }

    public void takeASelfie(View view) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            try {
                photoFile = createFile();
            } catch (IOException ex) {
                //mostraAlerta(getString(R.string.erro), getString(R.string.erro_salvando_foto));
            }

            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(
                        getBaseContext(),
                        getBaseContext().getApplicationContext().getPackageName() + ".provider",
                        photoFile);

                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, PHOTO);
            }
        }
    }

    public void save(View view){
        EditText name = (EditText) findViewById(R.id.txtContactName);

        if(name.getText().toString().trim().isEmpty()){
            name.setError("Campo não pode ser vazio");
            return;
        }

        EditText number = (EditText) findViewById(R.id.txtContactNumber);

        if(number.getText().toString().trim().isEmpty()){
            number.setError("Campo não pode ser vazio");
            return;
        }
        /*
        here goes the code to save on data base
         */

        returnToMainActivity();
    }

    public void cancel(View view){
        returnToMainActivity();
    }
}