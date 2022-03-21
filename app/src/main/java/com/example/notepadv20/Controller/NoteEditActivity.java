package com.example.notepadv20.Controller;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.notepadv20.Model.FileManagerModel;
import com.example.notepadv20.R;

import java.util.ArrayList;
import java.util.Scanner;

public class NoteEditActivity extends AppCompatActivity {

    ImageButton btn_save;
    EditText editText;
    EditText editTitle;
    FileManagerModel fileManagerModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_edit);

        fileManagerModel = new FileManagerModel(this);

        btn_save = findViewById(R.id.btn_save);
        editText = findViewById(R.id.editText);
        editTitle = findViewById(R.id.editText_title);

        String title = getIntent().getStringExtra("key");
        // Tar ut inskickad extradata på nyckeln "key" som innehåller titeln

        boolean editMode = getIntent().getBooleanExtra("editMode", false);
        // Tar ut inskickad extradata på nyckeln "editNote" som anger om NoteEditActivity är i
        // editMode eller "addMode" (dvs. man går via inflatorMenyn och skapar en anteckning).
        if (editMode) { // Tillåt inte att ändra filnamnen i editMode
            editTitle.setEnabled(false);
        }

        // --- Att ha title !=null är ett sätt att komma runt try/catch-problematiken, dvs. att
        // problemhanteringen kostar CPU och tydlighet för andra som läser koden

        if (title != null && title.length() > 0) { // Om titeln inte är tom
            editTitle.setText(title); // sätt titeln
            editText.setText(fileManagerModel.load(title)); // Ladda in anteckningen från filen
        }

        // -- SAVE KNAPPEN --

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String text = editText.getText().toString();

                String title = editTitle.getText().toString();

                ArrayList<String> fileNames = fileManagerModel.loadAll();
                // Skapar en strängArray som heter fileNames som laddar in alla filnamn via fileManagerModel.

                if (!editMode) { // Om det är editMode bryr vi oss inte om att skriva över en gammal fil
                    for (String fileName : fileNames) { // För varje filnamn i filnames
                        if (fileName.equals(title)) { // om den nuvarande anteckningens namn är samma som någon av fileName
                            Toast.makeText(
                                    NoteEditActivity.this,
                                    "Duplicate filename",
                                    Toast.LENGTH_SHORT
                            ).show(); // Låt användaren veta att det är duplicate
                            return; // returnera utan att spara
                        }
                    }
                }

                fileManagerModel.save(title, text);
                // Om det är editMode eller om ingen duplicate fanns, spara ner string Title och
                // string Text.

                finish();  // Tar oss tillbaks till MainActivity!
            }
        });
    }
}