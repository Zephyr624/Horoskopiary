package com.example.horoskopiary;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class EditNoteActivity extends AppCompatActivity {

    public static final String EXTRA_EDIT_NOTE_TITLE = "pb.edu.pl.EDIT_BOOK_TITLE";
    public static final String EXTRA_EDIT_NOTE_DESCRIPTION = "pb.edu.pl.EDIT_BOOK_AUThOR";

    private EditText editTitleEditText;
    private EditText editDescriptionEditText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);


        editDescriptionEditText = findViewById(R.id.edit_note_title);
        editTitleEditText = findViewById(R.id.edit_note_description);
        if (getIntent().hasExtra(EXTRA_EDIT_NOTE_TITLE)) {
            editTitleEditText.setText(getIntent().getStringExtra(EXTRA_EDIT_NOTE_TITLE));
            editDescriptionEditText.setText(getIntent().getStringExtra(EXTRA_EDIT_NOTE_DESCRIPTION));
        }
        final Button button = findViewById(R.id.button_save);
        button.setOnClickListener(e -> {
            Intent replyIntent = new Intent();
            if (TextUtils.isEmpty(editTitleEditText.getText())
                    || TextUtils.isEmpty(editDescriptionEditText.getText())) {
                setResult(RESULT_CANCELED, replyIntent);
            } else {
                String title = editTitleEditText.getText().toString();
                replyIntent.putExtra(EXTRA_EDIT_NOTE_TITLE, title);
                String description = editDescriptionEditText.getText().toString();
                replyIntent.putExtra(EXTRA_EDIT_NOTE_DESCRIPTION, description);
                setResult(RESULT_OK, replyIntent);
            }
            finish();
        });

    }
}