package com.example.android.tasksapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.tasksapp.Data.DataContract;
import com.example.android.tasksapp.Data.DataDBHelper;

import java.io.IOException;

public class detailActivity extends AppCompatActivity {
    private TextView title, update_note;
    private ImageView image;
    private EditText note_content;
    private ImageButton delete_button;
    private DataDBHelper dataDBHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        dataDBHelper = new DataDBHelper(this);
        final SQLiteDatabase sqLiteDatabase = dataDBHelper.getWritableDatabase();

        title = (TextView) findViewById(R.id.title);
        image = (ImageView) findViewById(R.id.note_image);
        note_content = (EditText) findViewById(R.id.note_content);
        delete_button = (ImageButton) findViewById(R.id.delete_button);
        update_note = (TextView) findViewById(R.id.update_note);

        Intent intent = getIntent();
        final Note note = (Note) intent.getSerializableExtra("note");
        title.setText("Title :- " + note.getTitle());
        note_content.setText(note.getContent());

        delete_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String selection = DataContract.NoteEntry._ID + "=?";
                String [] selectionArgs = {String.valueOf(note.getID())};
                int deletedRows = sqLiteDatabase.delete(DataContract.NoteEntry.TABLE_NAME, selection, selectionArgs);
                startActivity(new Intent(detailActivity.this, MainActivity.class));
            }
        });

        update_note.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String new_content = note_content.getText().toString();
                if(new_content.equals(note.getContent())) {
                    Toast.makeText(getApplicationContext(), "No changes in the description made", Toast.LENGTH_SHORT).show();
                } else {
                    ContentValues contentValues = new ContentValues();
                    contentValues.put(DataContract.NoteEntry.COLUMN_NOTE_CONTENT, new_content);
                    contentValues.put(DataContract.NoteEntry.COLUMN_NOTE_TITLE, note.getTitle());
                    contentValues.put(DataContract.NoteEntry.COLUMN_IMAGE, note.getImageBytes());
                    contentValues.put(DataContract.NoteEntry.COLUMN_CONTAINS_IMAGE, note.getContains_image());
                    contentValues.put(DataContract.NoteEntry.COLUMN_CREATED, note.getCreated().toString());
                    String selection = selection = DataContract.NoteEntry._ID + "=?";
                    String[] selectionArgs = {String.valueOf(note.getID())};
                    int count = sqLiteDatabase.update(
                            DataContract.NoteEntry.TABLE_NAME,
                            contentValues,
                            selection,
                            selectionArgs
                    );
                    Toast.makeText(getApplicationContext(), "Description updated successfully", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(detailActivity.this, MainActivity.class));
                }
            }
        });

        if(note.getContains_image() == 1) {
            byte [] imageBytes = note.getImageBytes();
            Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes,0, imageBytes.length);
            image.setImageBitmap(bitmap);

            image.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    ColorMatrix matrix = new ColorMatrix();
                    ColorMatrixColorFilter filter;
                    if(motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                        matrix.setSaturation(0);
                        filter = new ColorMatrixColorFilter(matrix);
                        image.setColorFilter(filter);
                    } else if(motionEvent.getAction() == MotionEvent.ACTION_UP) {
                        matrix.setSaturation(1);
                        filter = new ColorMatrixColorFilter(matrix);
                        image.setColorFilter(filter);
                    }
                    return true;
                }
            });
        }
    }
}
