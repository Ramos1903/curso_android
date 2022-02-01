package com.example.memorableplaces;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    String clickedItem;
    Intent intentList;

    TextView textViewPlaces;

    public void onClickShowMap(View v){

        Intent intent = new Intent(getApplicationContext(), MapsActivity.class);

        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textViewPlaces = findViewById(R.id.textViewPlaces);
        final ListView maplist = findViewById(R.id.listMap);

        ArrayList<String> places = new ArrayList<>();

        places.add("Add a new place...");

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, places);
        maplist.setAdapter(arrayAdapter);


        maplist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                clickedItem =(String) maplist.getItemAtPosition(position);

                intentList = new Intent(getApplicationContext(), MapsActivity.class);
                intentList.putExtra("listItem", clickedItem);

                startActivity(intentList);

            }
        });




    }
}