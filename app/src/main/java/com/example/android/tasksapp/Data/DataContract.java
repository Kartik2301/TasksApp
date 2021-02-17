package com.example.android.tasksapp.Data;

import android.provider.BaseColumns;

public final class DataContract {
    private DataContract(){}

    public static final class NoteEntry implements BaseColumns {
        public final static String TABLE_NAME = "notes";
        public final static String _ID = BaseColumns._ID;
        public final static String COLUMN_NOTE_TITLE = "title";
        public final static String COLUMN_NOTE_CONTENT = "content";
        public final static String COLUMN_CREATED = "created";
        public final static String COLUMN_IMAGE = "image";
        public final static String COLUMN_CONTAINS_IMAGE = "contains_image";
    }
}
