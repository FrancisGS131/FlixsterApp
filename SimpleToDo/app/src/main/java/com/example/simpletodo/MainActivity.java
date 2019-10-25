package com.example.simpletodo;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ClipData;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final String  KEY_ITEM_TEXT = "item_text";
    public static final String KEY_ITEM_POSITION = "item_position";
    public static final int EDIT_TEXT_CODE = 20;

    //ArrayList storing items to be displayed
    List<String> items;

    //Adding a member variable to each view
    Button btnAdd;
    EditText etItem;
    RecyclerView rvItems;
    ItemsAdapter itemsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Linking member variables to their appropriate views
        btnAdd = findViewById(R.id.btnAdd);
        etItem = findViewById(R.id.etItem);
        rvItems = findViewById(R.id.rvItems);

        /*
          ~Showcasing control of member variables with their respective views!
          ~Each of the views have a different set of methods that you can call on them
          etItem.setText("I'm doing this in java!");
        */

        //Instantiating ArrayList and storing items inside of it
        loadItems();

        ItemsAdapter.OnLongClickListener onLongClickListener = new ItemsAdapter.OnLongClickListener() {
            @Override
            public void onItemLongClicked(int position) {
                //Delete the item from te model
                items.remove(position);
                // Notify the adapter which position we deleted
                itemsAdapter.notifyItemRemoved(position);
                Toast.makeText(getApplicationContext(), "Item was removed", Toast.LENGTH_SHORT).show();

                saveItems();
            }
        };

        ItemsAdapter.OnClickListener clickListener = new ItemsAdapter.OnClickListener() {
            @Override
            public void onItemClicked(int position) {
                Log.d("MainActivity","Single click at position "+position);
                /*
                    Intents are requests made to the Android system. In our case, our intent is to open up the new activity
                    ~ Can have intents to open up URLs, browsers, or camera
                    > More information on intents here: https://guides.codepath.com/android/Using-Intents-to-Create-Flows
                 */

                // > Create the new activity
                // Two parameters in this example: (Context in which we're using this, where we want to go). The .this refers to the
                // current instance of Main Activity (this class we're in right now). There is no instance for 2nd, just the class we'd
                // like to go to. Android system will take care of this for us
                Intent i = new Intent(MainActivity.this,EditActivity.class);


                // Pass the data being edited - actual contents of ToDo and the position
                // Parameters for .putExtra method in intents: (key,value)
                i.putExtra(KEY_ITEM_TEXT,items.get(position));
                i.putExtra(KEY_ITEM_POSITION,position);

                // Display the activity
                //Parameters: (intent i, key). Key is needed in order to differentiate between intents. In our case, we only have
                // one intent, but for a fully built app, there are multiple intents that need differentiation!
                startActivityForResult(i,EDIT_TEXT_CODE);

            }
        };

        itemsAdapter = new ItemsAdapter(items, onLongClickListener, clickListener);
        rvItems.setAdapter(itemsAdapter);
        rvItems.setLayoutManager(new LinearLayoutManager(this));

        // Programs the button so that it adds whatever is set in the EditText view
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String todoItem = etItem.getText().toString();
                // Add item to the model
                items.add(todoItem);

                // Notify adapter that an item is inserted
                itemsAdapter.notifyItemInserted(items.size() - 1); // Sets bottom of RecycleView to the new item
                etItem.setText(""); // Reset EditText view to blank
                Toast.makeText(getApplicationContext(), "Item was added", Toast.LENGTH_SHORT).show();

                saveItems();
            }
        });
    }

    //Purpose is to handle the result of the Edit Activity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        //Check that the actual request code matches this request code
        if(resultCode == RESULT_OK && requestCode == EDIT_TEXT_CODE){
            //Retrieve tbe updated text value
            String itemText = data.getStringExtra(KEY_ITEM_TEXT);
            // Extract the original position of the edited item form the position key
            int position = data.getExtras().getInt(KEY_ITEM_POSITION);

            // Update the model at the right position with the new item text
            items.set(position,itemText);

            // Notify the adapter
            itemsAdapter.notifyItemChanged(position);

            // Persist the changes
            saveItems();
            Toast.makeText(getApplicationContext(),"Item updated successfully",Toast.LENGTH_SHORT).show();
        }
        else{
            Log.w("Main Activity","Unknown call to onActivityResult");
        }
    }

    // This method returns the file
    private File getDataFile() {
        //First parameter of this constructor is the directory, and second parameter is the name of the file
        return new File(getFilesDir(),"data.txt");
    }

    //This function will load items by reading every line of the data file
    private void loadItems(){
        try {
            items = new ArrayList<>(FileUtils.readLines(getDataFile(),Charset.defaultCharset()));
        } catch (IOException e) {
            // Convention for logs: ( Name of Class as tag name, Message naming the Error, the actual exception e)
            Log.e("Main Activity","Error reading items",e);
            items = new ArrayList<>();//In the case that there is an exception, it initializes the recycler view as an empty ArrayList
        }
    }

    //This function saves items by writing them into the data file
    private void saveItems() {
        try {
            FileUtils.writeLines(getDataFile(),items);
        } catch (IOException e) {
            Log.e("Main Activity","Error writing items",e);
        }
    }
}
