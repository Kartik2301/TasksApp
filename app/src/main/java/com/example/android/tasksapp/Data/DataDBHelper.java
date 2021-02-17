package com.example.android.tasksapp.Data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DataDBHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Notes18.db";

    public DataDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String SQL_CREATE_NOTES_TABLE = "CREATE TABLE " + DataContract.NoteEntry.TABLE_NAME + "("
                + DataContract.NoteEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + DataContract.NoteEntry.COLUMN_NOTE_TITLE + " TEXT NOT NULL, "
                + DataContract.NoteEntry.COLUMN_CREATED + " DATETIME DEFAULT (datetime('now', 'localtime')), "
                + DataContract.NoteEntry.COLUMN_IMAGE + " BLOB, "
                + DataContract.NoteEntry.COLUMN_CONTAINS_IMAGE + " INTEGER, "
                + DataContract.NoteEntry.COLUMN_NOTE_CONTENT + " TEXT NOT NULL);";

        sqLiteDatabase.execSQL(SQL_CREATE_NOTES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        onUpgrade(sqLiteDatabase, oldVersion, newVersion);
    }
}
