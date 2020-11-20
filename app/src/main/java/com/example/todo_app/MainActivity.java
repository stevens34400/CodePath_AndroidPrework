package com.example.todo_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.*;

public class MainActivity extends AppCompatActivity {

    List<String> items;

    Button addBtn;
    EditText addTxt;
    RecyclerView recyclerView;
    ItemAdapter itemAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addBtn = findViewById(R.id.addBtn);
        addTxt = findViewById(R.id.addTxt);
        recyclerView = findViewById(R.id.recyclerView);

        //Instantiate ArrayList
        loadItems();

        ItemAdapter.OnLongClickListener longClickListener = new ItemAdapter.OnLongClickListener(){

            @Override
            public void onItemLongClicked(int position) {
                //Delete item from model
                items.remove(position);

                //Notify adapter
                itemAdapter.notifyItemRemoved(position);

                //Toast
                Toast.makeText(getApplicationContext(), "Item was removed!", Toast.LENGTH_LONG).show();
                savedItems();
            }
        };

        itemAdapter = new ItemAdapter(items, longClickListener);
        recyclerView.setAdapter(itemAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String item = addTxt.getText().toString();
                // add item into model
                items.add(item);
                //Notify adapter that we inserted item
                itemAdapter.notifyItemInserted(items.size()-1);
                //Clear addTxt user input
                addTxt.setText("");

                //Toast
                Toast.makeText(getApplicationContext(), "Item was added!", Toast.LENGTH_SHORT).show();
                savedItems();
            }
        });
    }

    private File getDataFile(){
        return new File(getFilesDir(),"data.txt");
    }

    //load items by reading every line of datafile
    private void loadItems(){
        try {
            items = new ArrayList<String>(FileUtils.readLines(getDataFile(), Charset.defaultCharset()));
        } catch (IOException e) {
            Log.e("MainActivity","Error reading item",e);
            items = new ArrayList<String>();
        }
    }

    //Function to save items into datafile
    private void savedItems(){
        try {
            FileUtils.writeLines(getDataFile(), items);
        } catch (IOException e) {
            Log.e("MainActivity", "Error writing item",e);
        }
    }

}