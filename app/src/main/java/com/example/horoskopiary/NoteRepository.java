package com.example.horoskopiary;

import android.app.Application;

import androidx.lifecycle.LiveData;



import java.util.List;

public class NoteRepository {
    private final NoteDao noteDao;
    private final LiveData<List<Note>> notes;
    NoteRepository(Application application) {
        NoteDatabase database = NoteDatabase.getDatabase(application);
        noteDao = database.noteDao();
        notes = noteDao.findAll();
    }

    LiveData<List<Note>> findAllNotes(){

        return notes; }

    void insert(Note note){
        NoteDatabase.databaseWriteExecutor.execute(()-> noteDao.insert(note));
    }
    void update(Note note){
        NoteDatabase.databaseWriteExecutor.execute(()-> noteDao.update(note));
    }
    void delete(Note note){
        NoteDatabase.databaseWriteExecutor.execute(()-> noteDao.delete(note));
    }
}
