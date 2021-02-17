package com.example.android.tasksapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteBlobTooBigException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.android.tasksapp.Data.DataContract;
import com.example.android.tasksapp.Data.DataDBHelper;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class AddNote extends AppCompatActivity {
    private EditText NoteTitle;
    private EditText NoteContent;
    private Button addButton, uploadButton;
    private DataDBHelper dataDBHelper;
    private ImageView uploadedImage;
    private int IMG_UPLOAD_CATEGORY = 1;
    private int containsImage = 0;
    String filePath = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        // initialized DataDbHelper object
        dataDBHelper = new DataDBHelper(this);

        // initialize text fields and buttons
        NoteTitle = (EditText) findViewById(R.id.note_title);
        NoteContent = (EditText) findViewById(R.id.note_content);
        addButton = (Button) findViewById(R.id.confirmation);
        uploadButton = (Button) findViewById(R.id.upload_image);
        uploadedImage = (ImageView) findViewById(R.id.load_image);

        // button click listeners
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // extract text from the text fields
                String title = NoteTitle.getText().toString().trim();
                String content = NoteContent.getText().toString().trim();

                // check for empty title, or content
                if(title.length() == 0 || content.length() == 0) {
                    Toast.makeText(AddNote.this, R.string.warning, Toast.LENGTH_SHORT).show();
                } else {
                    // insert the note to the database
                    SQLiteDatabase sqLiteDatabase = dataDBHelper.getWritableDatabase();
                    ContentValues contentValues = new ContentValues();
                    contentValues.put(DataContract.NoteEntry.COLUMN_NOTE_TITLE, title);
                    contentValues.put(DataContract.NoteEntry.COLUMN_NOTE_CONTENT, content);

                    if(filePath.length() > 0) {
                        containsImage = 1;
                        try {
                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), Uri.parse(filePath));
                            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                            byte[] imageBytes = byteArrayOutputStream.toByteArray();
                            try {
                                contentValues.put(DataContract.NoteEntry.COLUMN_IMAGE, imageBytes);
                            } catch (SQLiteBlobTooBigException e) {
                                Toast.makeText(getApplicationContext(), "Image resolution too large", Toast.LENGTH_SHORT).show();
                            }
                        } catch (IOException e) {

                        }
                    } else {
                        byte[] empty_arr = new byte[1];
                        contentValues.put(DataContract.NoteEntry.COLUMN_IMAGE, empty_arr);
                    }

                    contentValues.put(DataContract.NoteEntry.COLUMN_CONTAINS_IMAGE, containsImage);

                    long newRowId = sqLiteDatabase.insert(DataContract.NoteEntry.TABLE_NAME, null, contentValues);
                    startActivity(new Intent(AddNote.this, MainActivity.class));
                }
            }
        });

        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
                } else {
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                }
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), IMG_UPLOAD_CATEGORY);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == IMG_UPLOAD_CATEGORY && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri _filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), _filePath);
                uploadedImage.setImageBitmap(bitmap);
            } catch (IOException e) {

            }
            filePath = _filePath.toString();
        }
    }
}
