package com.example.android.tasksapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class NoteAdapter extends ArrayAdapter {
    ArrayList<Note> notes;

    public NoteAdapter(@NonNull Context context, int resource, ArrayList<Note> note_objects) {
        super(context, resource, note_objects);
        notes = note_objects;
    }

    @Override
    public int getCount() {
        return super.getCount();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View v = convertView;
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        v = inflater.inflate(R.layout.list_item, null);
        TextView titleView = (TextView) v.findViewById(R.id.title);
        TextView contentView = (TextView) v.findViewById(R.id.content);
        TextView createdView = (TextView) v.findViewById(R.id.created);
        String str = notes.get(position).getTitle();
        titleView.setText(str.substring(0, Math.min(str.length(), 20)));
        contentView.setText(notes.get(position).getContent());
        DateFormat dayFormat = new SimpleDateFormat("dd MMMM yyyy");
        DateFormat dateFormat = new SimpleDateFormat("hh:mm a");
        String strDate = dayFormat.format(notes.get(position).getCreated());
        String strTime = dateFormat.format(notes.get(position).getCreated()).toUpperCase();
        createdView.setText(strDate + " " + strTime);
        return v;
    }
}
