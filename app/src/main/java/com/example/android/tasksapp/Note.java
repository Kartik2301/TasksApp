package com.example.android.tasksapp;

import java.io.Serializable;
import java.sql.Time;
import java.sql.Timestamp;

public class Note implements Serializable {
    private Long ID;
    private String title;
    private String content;
    private Timestamp created;
    private byte[] imageBytes;
    private int contains_image;

    public Long getID() {
        return ID;
    }

    public void setID(Long ID) {
        this.ID = ID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public Timestamp getCreated() {
        return created;
    }

    public void setCreated(Timestamp created) {
        this.created = created;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public byte[] getImageBytes() {
        return imageBytes;
    }

    public void setImageBytes(byte[] imageBytes) {
        this.imageBytes = imageBytes;
    }

    public int getContains_image() {
        return contains_image;
    }

    public void setContains_image(int contains_image) {
        this.contains_image = contains_image;
    }

    public Note(Long ID, String title, String content, Timestamp created, byte[] imageBytes, int contains_image) {
        this.ID = ID;
        this.title = title;
        this.content = content;
        this.created = created;
        this.imageBytes = imageBytes;
        this.contains_image = contains_image;
    }
}
