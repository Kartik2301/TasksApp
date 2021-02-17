package com.example.android.tasksapp;

import java.util.Comparator;

public class Constants {
}

class customSortDatetimeAsc implements Comparator<Note> {
    public int compare(Note a, Note b) {
        return a.getCreated().compareTo(b.getCreated());
    }
}

class customSortDatetimeDesc implements Comparator<Note> {
    public int compare(Note a, Note b) {
        return b.getCreated().compareTo(a.getCreated());
    }
}

class customSortTitleAsc implements Comparator<Note> {
    public int compare(Note a, Note b) {
        return a.getTitle().compareTo(b.getTitle());
    }
}

class customSortTitleDesc implements Comparator<Note> {
    public int compare(Note a, Note b) {
        return b.getTitle().compareTo(a.getTitle());
    }
}