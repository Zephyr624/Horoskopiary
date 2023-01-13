package com.example.horoskopiary;

import android.content.Intent;
import android.os.Bundle;



import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;



import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.TextView;


import java.text.SimpleDateFormat;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final int NEW_NOTE_ACTIVITY_REQUEST_CODE = 1;
    public static final int EDIT_NOTE_ACTIVITY_REQUEST_CODE = 2;
    public static final int HOROSCOPE_ACTIVITY_REQUEST_CODE = 3;
    public List<Note> notes;
    SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
    private NoteViewModel noteViewModel;
    private Note editedNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("@string/app_name");

        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        final NoteAdapter adapter = new NoteAdapter();
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        noteViewModel = ViewModelProviders.of(this).get(NoteViewModel.class);
        noteViewModel.findAll().observe(this, adapter::setNotes);

        FloatingActionButton addNoteButton = findViewById(R.id.add_button);
        addNoteButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, EditNoteActivity.class);
            startActivityForResult(intent, NEW_NOTE_ACTIVITY_REQUEST_CODE);
        });
        FloatingActionButton horoscopeButton = findViewById(R.id.horoscope_button);
        horoscopeButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, HoroscopeActivity.class);
            startActivityForResult(intent, HOROSCOPE_ACTIVITY_REQUEST_CODE);
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        final NoteAdapter adapter = new NoteAdapter();
        String string = getString(R.string.notes_count, notes.size());
        getSupportActionBar().setSubtitle(string);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == NEW_NOTE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            Note note = new Note(data.getStringExtra(EditNoteActivity.EXTRA_EDIT_NOTE_TITLE),
                    data.getStringExtra(EditNoteActivity.EXTRA_EDIT_NOTE_DESCRIPTION));
            noteViewModel.insert(note);
            Snackbar.make(findViewById(R.id.coordinator_layout), getString(R.string.book_added),
                    Snackbar.LENGTH_LONG).show();
        } else if (requestCode == EDIT_NOTE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            editedNote.setTitle(data.getStringExtra(EditNoteActivity.EXTRA_EDIT_NOTE_TITLE));
            editedNote.setDescription(data.getStringExtra(EditNoteActivity.EXTRA_EDIT_NOTE_DESCRIPTION));
            noteViewModel.update(editedNote);
            editedNote = null;
            Snackbar.make(findViewById(R.id.coordinator_layout), getString(R.string.book_edited),
                    Snackbar.LENGTH_LONG).show();
        } else if(requestCode == EDIT_NOTE_ACTIVITY_REQUEST_CODE &&resultCode==RESULT_CANCELED){
            Snackbar.make(findViewById(R.id.coordinator_layout),
                            getString(R.string.empty_not_saved),
                            Snackbar.LENGTH_LONG)
                    .show();
        }
    }

    private class NoteHolder extends RecyclerView.ViewHolder {
        private final TextView noteTextView;
        private final TextView noteTitleTextView;
        private final TextView noteDateTextView;
        private Note note;

        public NoteHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.note_list_item, parent, false));

            noteTextView = itemView.findViewById(R.id.note_description);
            noteTitleTextView = itemView.findViewById(R.id.note_title);
            noteDateTextView = itemView.findViewById(R.id.note_date);
            View noteItem = itemView.findViewById(R.id.note_item);
            noteItem.setOnLongClickListener(v -> {
                noteViewModel.delete(note);
                Snackbar.make(findViewById(R.id.coordinator_layout),
                                getString(R.string.book_deleted),
                                Snackbar.LENGTH_LONG)
                        .show();
                return true;
            });
            noteItem.setOnClickListener(v -> {
                editedNote = note;
                Intent intent = new Intent(MainActivity.this, EditNoteActivity.class);
                intent.putExtra(EditNoteActivity.EXTRA_EDIT_NOTE_TITLE, noteTitleTextView.getText());
                intent.putExtra(EditNoteActivity.EXTRA_EDIT_NOTE_DESCRIPTION, noteTextView.getText());
                startActivityForResult(intent, EDIT_NOTE_ACTIVITY_REQUEST_CODE);
            });
        }

        public void bind(Note note) {
            noteTextView.setText(note.getDescription());
            noteTitleTextView.setText(note.getTitle());
            noteDateTextView.setText( dateFormat.format(note.getNoteDate()));
            this.note = note;
        }
    }

    private class NoteAdapter extends RecyclerView.Adapter<NoteHolder> {


        @NonNull
        @Override
        public NoteHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new NoteHolder(getLayoutInflater(), parent);
        }

        @Override
        public void onBindViewHolder(NoteHolder holder, int position) {

            if (notes != null) {
                Note note = notes.get(position);
                holder.bind(note);
            } else {
                Log.d("MainActivity", "No notes");
            }
        }

        public int getItemCount() {
            if (notes != null) {
                return notes.size();
            } else {
                return 0;
            }
        }

        void setNotes(List<Note> notess) {
            notes = notess;
            notifyDataSetChanged();
        }
    }
}