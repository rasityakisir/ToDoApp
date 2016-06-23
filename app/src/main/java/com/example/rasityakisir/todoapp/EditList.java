package com.example.rasityakisir.todoapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class EditList extends Activity {

    /* UI */
    private EditText editToDoText;
    private Button saveBtn;

    /* Data */
    private String editTextData;
    private int dataPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_list);

        editToDoText = (EditText) findViewById(R.id.editText);
        saveBtn = (Button) findViewById(R.id.saveBtn);

        Bundle extras = getIntent().getExtras();

        /* Check if Extras are Null */
        if (extras != null) {

            /* Parse String from Extras */
            editTextData = extras.getString(MainActivity.INTENT_EXTRAS_ITEM_TEXT);
            dataPosition = extras.getInt(MainActivity.INTENT_EXTRAS_ITEM_POSITION);
        }

        /* Set Data To the Edit Text */
        if (editTextData != null) editToDoText.setText(editTextData);

        /* Set Click listener for Save Button */
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSaveItem();
            }
        });
    }

    public void onSaveItem() {

        /* Get Text from EditText */
        String modifiedData = editToDoText.getText().toString();

        Intent data = new Intent();

        /* Put Data */
        data.putExtra(MainActivity.INTENT_EXTRAS_ITEM_TEXT, modifiedData);
        data.putExtra(MainActivity.INTENT_EXTRAS_ITEM_POSITION, dataPosition);

        setResult(RESULT_OK, data);
        super.finish();

    }

}
