package com.example.horoskopiary;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;




import java.util.List;

@Dao
public interface NoteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Note note);
    @Update
    void update(Note note);
    @Delete
    void delete(Note note);
    @Query("DELETE FROM Note")
    void deleteAll();
    @Query("SELECT * FROM Note ORDER BY noteDate DESC")
    LiveData<List<Note>> findAll();
    @Query("SELECT * FROM Note WHERE title LIKE :title")
    List<Note> findNoteWithTitle(String title);
}
