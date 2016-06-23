package com.example.rasityakisir.todoapp;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends Activity {

    private ArrayList<String> todoItems;
    private ArrayAdapter<String> aTodoAdapter;

    private ListView lvItems;
    private EditText etEditText;

    private final int REQUEST_CODE =20;

    public static final String INTENT_EXTRAS_ITEM_TEXT = "TEXT";
    public static final String INTENT_EXTRAS_ITEM_POSITION = "POSITION";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

         /* init UI */
        initUI();

        /* Read Data and Set Adapter */
        initAdapter();
    }

    /**
     * Inits Adapter and Sets data Set.
     */
    private void initAdapter() {

        /* Init To-do items from Text File */
        initItemsFromTextFile();

        /* Create new Adapter with To-do Items */
        aTodoAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, todoItems);

        /* Set Adapter of List View */
        lvItems.setAdapter(aTodoAdapter);
    }

    /**
     * initializes UI Elements.
     */
    private void initUI() {

        setContentView(R.layout.activity_main);
        lvItems = (ListView) findViewById(R.id.lvItems);
        etEditText = (EditText) findViewById(R.id.etEditText);

        /* Set Listeners */
        setupEditItemListener();
        setupListViewListener();
    }

    /**
     * Starts Edit Activity to Edit the ITEM.
     *
     * @param item
     */
    private void launchEditItem(String item, int position) {

        Intent i = new Intent(MainActivity.this, EditList.class);

        i.putExtra(INTENT_EXTRAS_ITEM_TEXT,item);
        i.putExtra(INTENT_EXTRAS_ITEM_POSITION, position);

        startActivityForResult(i, REQUEST_CODE);
    }

    private void setupEditItemListener() {

        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapter, View item, int position, long id) {
                String text = (String) lvItems.getItemAtPosition(position);
                launchEditItem(text, position);
            }
        });
    }

    private void setupListViewListener() {

        lvItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long l) {

                String selectedItem = ((TextView) view).getText().toString();

                if (selectedItem.trim().equals(todoItems.get(position).trim())) {
                    removeElement(selectedItem, position);
                } else {
                    Toast.makeText(getApplicationContext(),"Error Removing Element", Toast.LENGTH_LONG).show();
                }

                return true;
            }
        });
    }

    /**
     * Read Items from Text File.
     */
    private void initItemsFromTextFile() {

        File filesDir = getFilesDir();
        File file = new File(filesDir,"todo.txt");
        try{
            todoItems = new ArrayList<>(FileUtils.readLines(file));
        }catch (IOException e) {
            todoItems = new ArrayList<>();
        }
    }

    /**
     * Write Items to the Text File.
     */
    private void writeItems() {

        File filesDir = getFilesDir();
        File file = new File(filesDir,"todo.txt");
        try{
            FileUtils.writeLines(file, todoItems);
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Adds the new Item to the TODO List.
     *
     * @param view
     */
    public void onAddItem(View view) {

        /* Add new item to the List */
        todoItems.add(etEditText.getText().toString());
        aTodoAdapter.notifyDataSetChanged();

        /* Clean Text View */
        etEditText.setText("");

        /* Write Item to Text File */
        writeItems();
    }

    /**
     * Modifies Item  at the position.
     *
     * @param text
     * @param position
     */
    private void updateItem(String text, int position) {

        /* Update Item in Position */
        todoItems.set(position, text);

        /* Notify */
        aTodoAdapter.notifyDataSetChanged();

        /* Write */
        writeItems();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {

            /* Check if Null */
            if (data == null || data.getExtras() == null) return;

            String text = data.getExtras().getString(INTENT_EXTRAS_ITEM_TEXT);
            int position = data.getExtras().getInt(INTENT_EXTRAS_ITEM_POSITION);

            /* Update Item */
            updateItem(text, position);

        }
    }

    /**
     * Removes Item from the List and updates Text File.
     *
     * @param selectedItem
     * @param position
     */
    public void removeElement(String selectedItem, final int position) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete " + selectedItem + "?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                /* Remove From Data Set */
                todoItems.remove(position);

                /* Notify Adapter */
                aTodoAdapter.notifyDataSetChanged();

                /* Write */
                writeItems();
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }
}
