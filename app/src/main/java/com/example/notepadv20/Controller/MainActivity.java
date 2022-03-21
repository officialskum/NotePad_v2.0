package com.example.notepadv20.Controller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.notepadv20.Model.FileManagerModel;
import com.example.notepadv20.R;

import java.util.ArrayList;
import java.util.HashSet;

public class MainActivity extends AppCompatActivity {

    // -- Först instansieras variablerna ListViewen, arrayListen, arrayAdaptern och FileManagerModellen --

    ListView listView;
    static ArrayList<String> notes = new ArrayList<>();
    ArrayAdapter arrayAdapter;
    FileManagerModel fileManagerModel;

    /** Först rensas alla element från arrayAdaptern, sedan läggs fileManagerModels loadAll funktion
     * in via arrayAdaptern varpå notifyDataSetChanged berättar för listan att källan till data har förändrats
     * och att den måste ladda om sig själv för att visa den nya datan.
     *  Laddar in alla notes OnResume();
     */

    @Override
    protected void onResume() {
        super.onResume();

       arrayAdapter.clear();
       arrayAdapter.addAll(fileManagerModel.loadAll());
       arrayAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.list_view);

        fileManagerModel = new FileManagerModel(this);

        arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, notes);

        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Intent intent = new Intent(MainActivity.this, NoteEditActivity.class);

                TextView current = (TextView) view;

                // Skapar en TextView som heter current stöpt som (TextView).

                String value = current.getText().toString();

                // Current är en textView och hämtar texten från View (current) och gör om det till en String.

                intent.putExtra("key", value);

                // Skickar över "key" och value (TextViewen) till nästa aktivitet.

                intent.putExtra("editMode", true);

                // Skicka med nyckeln "editNote" och boolean värdet True
                // Anledning till detta är pga att vi behöver ha ett boolean
                // värde för att veta om vi är i "EditMode" i NoteEditActivity

                startActivity(intent);

            }
        });

        // -- AlertDialogBuilder ifall man håller in en note (onItemLongClick) och vill deleta --

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {

                final int itemToDelete = i; // indexet på listItemet du långpressar

                // itemToDelete används då onItemLongClick innehåller två (i) referenser, så för att
                // undvika crash måste man omdefiniera i till itemToDelete.

                new AlertDialog.Builder(MainActivity.this)
                        .setIcon(android.R.drawable.ic_dialog_alert).setTitle(getString(R.string.are_you_sure))
                        .setMessage(getString(R.string.delete_this_note))
                        .setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                            @Override public void onClick(DialogInterface dialogInterface, int i) {
                                String fileNameToDelete = notes.get(itemToDelete);
                                fileManagerModel.delete(fileNameToDelete); // ta bort från disk
                                notes.remove(itemToDelete); // ta bort från "kör minnet"

                                arrayAdapter.clear();
                                arrayAdapter.addAll(fileManagerModel.loadAll());
                                arrayAdapter.notifyDataSetChanged(); // Kör metoden från OnResume igen för att uppdatera listan
                            }
                        })
                        .setNegativeButton(getString(R.string.no), null)
                        .show();

                return true;
            }
        });
    }

    // -- InflatorMenyn uppe till höger skapas här. --

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.note_add_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }



    // -- Öppnar ny Activity (NoteEditActivity.class) och skickar med boolean värdet false via nyckeln --

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);

        if (item.getItemId() == R.id.add_note) {
            // Ifall getItemId() är samma som id:et på add_note förs användaren till NoteEditActivity, dvs.
            // den andra aktiviteten.

            Intent intent = new Intent(getApplicationContext(), NoteEditActivity.class);
            intent.putExtra("editMode", false);
            // Skickar med en bool till NoteEditActivity.

            startActivity(intent);

            return true;

        }
        return false;
    }

}




