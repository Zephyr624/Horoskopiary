package com.example.horoskopiary;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;



import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities={Note.class}, version=1, exportSchema=false)
@TypeConverters({DateConverter.class})
public abstract class NoteDatabase extends RoomDatabase {
    private static NoteDatabase databaseInstance;
    static final ExecutorService databaseWriteExecutor= Executors.newSingleThreadExecutor();

    public abstract NoteDao noteDao();

    static NoteDatabase getDatabase(final Context context)
    {
        if(databaseInstance==null){
            databaseInstance= Room.databaseBuilder(context.getApplicationContext(),
                            NoteDatabase.class,"note_database")
                    .addCallback(roomDatabaseCallback)
                    .build();
        }
        return databaseInstance;
    }

    private static final RoomDatabase.Callback roomDatabaseCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);

            /*databaseWriteExecutor.execute(() -> {
                NoteDao dao = databaseInstance.noteDao();
                Note note = new Note("Clean Code", "Robert C. Martin");
                dao.insert(note);
                note=new Note("C# w pigulce 9.0","Joseph Albahari");
                dao.insert(note);
                note = new Note("Eragorn","Christopher Paolini");
                dao.insert(note);
                note=new Note("C# w pigulce 9.0","Joseph Albahari");
                dao.insert(note);

            });*/
        }
    };
}