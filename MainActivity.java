package com.example.todoapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.FileUtils;
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

    List<String> items = new ArrayList<>();
    Button button;
    EditText editem;
    RecyclerView rvItems;
    itemAdaptor itemAdaptor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = findViewById(R.id.button);
        editem = findViewById(R.id.editem);
        rvItems = findViewById(R.id.rvItems);

        loadItems();
        items = new ArrayList<>();
        items.add("Buy milk");
        items.add("Go to the gym");
        items.add("Have fun");

        itemAdaptor.onLongClickListener onLongClickListener = new itemAdaptor.onLongClickListener(){
            @Override
            public void onItemLongClicked(int position) {
                items.remove(position);

                itemAdaptor.notifyItemRemoved(position);
                Toast.makeText(getApplicationContext(), "Item was removed", Toast.LENGTH_SHORT).show();
                saveItem();
            }
        };

        final itemAdaptor itemAdaptor = new itemAdaptor(items, onLongClickListener);
        rvItems.setAdapter(itemAdaptor);
        rvItems.setLayoutManager(new LinearLayoutManager(this));

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String todoItem = editem.getText().toString();
                items.add(todoItem);
                itemAdaptor.notifyItemInserted(items.size()-1);
                editem.setText("");
                Toast.makeText(getApplicationContext(), "Item was added", Toast.LENGTH_SHORT).show();
                saveItem();
            }
        });
    }
    private File getDataFile() {
        return new File(getFilesDir(), "data.txt");
    }
    private void loadItems() {
        try {
            items = new ArrayList<>(FileUtils.readLines(getDataFile(), Charset.defaultCharset()));
        } catch (IOException e) {
            Log.e("MainActivity", "Error reading items", e);
            items = new ArrayList<>();
        }
    }
    private void saveItem() {
        try{
            FileUtils.writeLines(getDataFile(), items);
        }catch (IOException e) {
            Log.e("MainActivity", "Error writing items", e);
        }
    }

}