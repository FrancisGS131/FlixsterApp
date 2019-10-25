package com.example.simpletodo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class EditActivity extends AppCompatActivity {

    EditText etItem;
    Button btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        etItem = findViewById(R.id.etItem);
        btnSave = findViewById(R.id.btnSave);

        getSupportActionBar().setTitle("Edit Item");

        etItem.setText(getIntent().getStringExtra(MainActivity.KEY_ITEM_TEXT));

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //When the user has tapped on the save button, we want to go back to the main screen! To do this,
                // we need an intent :)

                //Create an intent which will contain the results
                Intent intent = new Intent(); //Left as empty constructor, used as shell to pass data

                // Pass the data (results of editing)
                intent.putExtra(MainActivity.KEY_ITEM_TEXT,etItem.getText().toString());
                //Let's the MainActivity figure out at what point should the list be updated
                intent.putExtra(MainActivity.KEY_ITEM_POSITION,getIntent().getExtras().getInt(MainActivity.KEY_ITEM_POSITION));

                //Set the results of the intent
                setResult(RESULT_OK,intent);

                //Finish activity - close screen and go back
                finish();
            }
        });
    }
}
