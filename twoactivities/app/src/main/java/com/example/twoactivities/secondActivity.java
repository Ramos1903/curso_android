package com.example.twoactivities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class secondActivity extends AppCompatActivity {

    public void bntSecond(View v) {
        //Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        //startActivity(intent);

        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.second_activity);

        Intent intent1 = getIntent();
        int age = intent1.getIntExtra("age", 0);
        Intent intent = getIntent();
        String StringListItem = intent.getStringExtra("listItem");

        //Toast.makeText(this, Integer.toString(age), Toast.LENGTH_SHORT).show();
        Toast.makeText(this,StringListItem,Toast.LENGTH_LONG).show();
    }
}