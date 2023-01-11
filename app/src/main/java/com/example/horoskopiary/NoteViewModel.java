package com.example.horoskopiary;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;



import java.util.List;

public class NoteViewModel extends AndroidViewModel {
    private final NoteRepository noteRepository;
    private final LiveData<List<Note>> notes;
    public NoteViewModel(@NonNull Application application){
        super(application);
        noteRepository = new NoteRepository(application);
        notes = noteRepository.findAllNotes();
    }
    public LiveData<List<Note>> findAll(){return notes;}
    public void insert(Note note){
        noteRepository.insert(note);}
    public void update(Note note){
        noteRepository.update(note);}
    public void delete(Note note){
        noteRepository.delete(note);}
}