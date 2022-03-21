package com.example.notepadv20.Model;

import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.notepadv20.Controller.NoteEditActivity;
import com.example.notepadv20.R;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;
import java.util.TimeZone;

public class FileManagerModel {

    AppCompatActivity activity;
    File noteFolder;
    String subFolder;

    // --- Konstruktor till FileManagerModel ---

    public FileManagerModel(AppCompatActivity activity) {

        this.activity = activity;

        this.subFolder = "notes"; //sätter namn på subFoldern till "notes".

        this.noteFolder = new File(this.activity.getFilesDir(), this.subFolder);
        // Sätter noteFolder till en File med pathen till "notes"-mappen

        // v Skapar en mapp, getFilesDir kollar i filerna efter en mapp som heter notes.
        if (!noteFolder.exists()) {
            noteFolder.mkdir();
            // ^^ Om det INTE finns en fil (file.exists) då skapar vi directoryn via file.mkdir som heter
            // "notes".
        }
    }

    // --- DELETE metoden ---

    public void delete(String fileName) {
        try {
            // textFile är den fil på disk med samma namn som fileName
            // t.ex "C:/Programs/NotePadv20/notes/fileName.txt
            File textFile = new File(noteFolder, fileName);
            // Buntar ihop pathen (noteFolder) med filnamnen (fileName)

            if (textFile.exists()) { // om filen existerar
                textFile.delete(); // så deletar den
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // --- SAVE metoden ---

    public void save(String title, String note) {

        try {

            File textFile = new File(noteFolder, title);
            // Buntar ihop pathen (noteFolder) med filnamnen (title)

            PrintWriter writer = new PrintWriter(textFile);
            // Skapar en ny text fil i mappen notes

            writer.append(note); // Skriv ner texten från "note" på disk
            writer.close();
            Toast.makeText(this.activity, "Saved to file", Toast.LENGTH_SHORT).show();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    // --- LOAD metoden ---

    public String load(String fileName) {

        String noteText = "";
        // Skapar en tom sträng som heter noteText

        try {

            File textFile = new File(noteFolder, fileName);
            // Buntar ihop pathen (noteFolder) med filnamnen (fileName)

            Scanner scanner = new Scanner(textFile);
            // Öppnar en anslutning till filen "textFile"
            while (scanner.hasNext()) { // om filen har en nästa rad att läsa
                noteText = noteText + scanner.nextLine() + "\n";
                // Mata in raden i noteText och avsluta med newline
            }

        } catch (IOException e) {
            noteText = "failed to load"; //Incase den misslyckas med att ladda in anteckningen.
            e.printStackTrace();
        }

        return noteText;

    }

    /**
     * @loadAll() metoden som returnerar en ArrayList med namnet "notes". Körs sedan i en
     * try / catch. File 'location' med pathnamnet/directoryn this.activity + / + this.subFolder
     * (dvs "notes"). File arrayen files listar sedan location och körs sedan i en for-each
     * loop där notes skapas / läggs till. Sedan körs en snabb felsökning och metoden returnerar
     * "notes".
     */

    // --- LoadAll metoden ---

    public ArrayList<String> loadAll() {
        ArrayList<String> notes = new ArrayList<>();

        try {

            File[] files = noteFolder.listFiles();

            // Returnerar en array med alla filerna som är i mappen notes (this.subFolder).

            for (File file : files) {

                // Kollar igenom files efter filnamnet

                notes.add(file.getName());

                //Returnerar bara namnet på filen

            }

        } catch (Exception e) {

            e.printStackTrace();

        }

        return notes;

        // Här returneras namnet på alla filer i stringArrayen notes!
    }
}



