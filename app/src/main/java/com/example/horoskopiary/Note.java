package com.example.horoskopiary;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Calendar;
import java.util.Date;

@Entity(tableName = "Note")
public class Note {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String title;
    private String description;
    private Date noteDate;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    public Date getNoteDate() {
        return noteDate;
    }

    public void setNoteDate(Date date) {
        this.noteDate = date;
    }

    public Note(String title, String description) {
        this.title = title;
        this.description = description;
        this.noteDate= Calendar.getInstance().getTime();
    }
}
