package com.example.android.tasksapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.android.tasksapp.Data.DataContract;
import com.example.android.tasksapp.Data.DataDBHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class MainActivity extends AppCompatActivity {
    private FloatingActionButton addNoteButton;
    private DataDBHelper dataDBHelper;
    private ArrayList<Note> notes;
    private ListView NoteListView;
    private NoteAdapter noteAdapter;
    private boolean asc_sorted = false;
    private boolean title_sort = false;

    @Override
    protected void onRestart() {
        super.onRestart();
        notes.clear();
        databaseReader();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        notes = new ArrayList<>();
        NoteListView = (ListView) findViewById(R.id.notes_list);
        noteAdapter = new NoteAdapter(this, R.layout.list_item, notes);
        NoteListView.setAdapter(noteAdapter);

        dataDBHelper = new DataDBHelper(this);

        // read the database
        databaseReader();

        addNoteButton = (FloatingActionButton) findViewById(R.id.add_note);
        addNoteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddNote.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sort_by_datetime :
                if(asc_sorted == false) {
                    Collections.sort(notes, new customSortDatetimeAsc());
                    noteAdapter.notifyDataSetChanged();
                } else {
                    Collections.sort(notes, new customSortDatetimeDesc());
                    noteAdapter.notifyDataSetChanged();
                }
                asc_sorted = !asc_sorted;
                return true;
            case R.id.sort_by_title :
                if(title_sort == false) {
                    Collections.sort(notes, new customSortTitleAsc());
                    noteAdapter.notifyDataSetChanged();
                } else {
                    Collections.sort(notes, new customSortTitleDesc());
                    noteAdapter.notifyDataSetChanged();
                }
                title_sort = !title_sort;
                return true;
            case R.id.photo_attachments :
                ArrayList<Note> copy = new ArrayList<>();
                for(int i=0;i<notes.size();i++) {
                    if(notes.get(i).getContains_image() == 1) {
                        copy.add(notes.get(i));
                    }
                }
                notes.clear();
                notes.addAll(copy);
                noteAdapter.notifyDataSetChanged();
                return true;
            case R.id.show_all :
                notes.clear();
                databaseReader();
                return true;
        }
        return true;
    }

    private void databaseReader() {
        SQLiteDatabase sqLiteDatabase = dataDBHelper.getReadableDatabase();
        String [] projections = {
                DataContract.NoteEntry._ID,
                DataContract.NoteEntry.COLUMN_NOTE_TITLE,
                DataContract.NoteEntry.COLUMN_CREATED,
                DataContract.NoteEntry.COLUMN_NOTE_CONTENT,
                DataContract.NoteEntry.COLUMN_CONTAINS_IMAGE,
                DataContract.NoteEntry.COLUMN_IMAGE
        };

        Cursor cursor = sqLiteDatabase.query(
                DataContract.NoteEntry.TABLE_NAME,
                projections,
                null,
                null,
                null,
                null,
                null
        );

        try {
            while (cursor.moveToNext()) {
                long id = cursor.getLong(cursor.getColumnIndexOrThrow(DataContract.NoteEntry._ID));
                String title = cursor.getString(cursor.getColumnIndexOrThrow(DataContract.NoteEntry.COLUMN_NOTE_TITLE));
                String content = cursor.getString(cursor.getColumnIndexOrThrow(DataContract.NoteEntry.COLUMN_NOTE_CONTENT));
                Timestamp timestamp = Timestamp.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(DataContract.NoteEntry.COLUMN_CREATED)));
                int contains_image = cursor.getInt(cursor.getColumnIndexOrThrow(DataContract.NoteEntry.COLUMN_CONTAINS_IMAGE));
                byte [] imageBytes = cursor.getBlob(cursor.getColumnIndexOrThrow(DataContract.NoteEntry.COLUMN_IMAGE));
                notes.add(0, new Note(id,title,content, timestamp, imageBytes, contains_image));
            }
        } finally {
            cursor.close();
            noteAdapter.notifyDataSetChanged();
        }

        NoteListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(MainActivity.this, detailActivity.class);
                intent.putExtra("note", notes.get(i));
                startActivity(intent);
            }
        });
    }
}
