package com.example.simpletodo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import org.apache.commons.io.FileUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    List<String> items;

    Button btnAdd;
    EditText etItem;
    RecyclerView rvItems;
    ItemsAdapter itemsAdapter;


    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); //sets layout

        //instantiating
        btnAdd = findViewById(R.id.btnAdd);
        etItem = findViewById(R.id.etitem);
        rvItems = findViewById(R.id.rvitems);

        //etItem.setText("I'm doing this in java!!!");

        loadItems();

        ItemsAdapter.OnLongClickListener onLongClickListener =  new ItemsAdapter.OnLongClickListener(){
            @Override
            public void onItemLongClick(int position) {
                //delete the item from the model
                items.remove(position);
                //notify the adpater the deleted position
                itemsAdapter.notifyItemRemoved(position);
                Toast.makeText(getApplicationContext(),"Item was removed.",Toast.LENGTH_SHORT).show();
                saveItems();
            }
        };
        //construct adpater
        itemsAdapter = new ItemsAdapter(items, onLongClickListener);
        rvItems.setAdapter(itemsAdapter);
        rvItems.setLayoutManager(new LinearLayoutManager(this));

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String todoItem = etItem.getText().toString();
                // add item to the model
                items.add(todoItem);
                // notify the adapter that item was inserted
                itemsAdapter.notifyItemInserted(items.size() - 1);

                etItem.setText("");
                Toast.makeText(getApplicationContext(),"Item was added.",Toast.LENGTH_SHORT).show();
                saveItems();
            }
        });


    }

    private File getDataFile(){
        return new File(getFilesDir(),"data.txt");
    }

    // This function will load items by reading every line of the data file
    private void loadItems() {
        try {
            items = new ArrayList<>(FileUtils.readLines(getDataFile(), Charset.defaultCharset()));
        } catch (IOException e) {
            Log.e("MainActivity", "Error reading items",e);
            items = new ArrayList<>();
        }
    }

    // This function saves items by writing them into the data file
    private void saveItems() {
        try {
            FileUtils.writeLines(getDataFile(), items);
        } catch (IOException e) {
            Log.e("MainActivity", "Error reading items",e);
        }
    }

}