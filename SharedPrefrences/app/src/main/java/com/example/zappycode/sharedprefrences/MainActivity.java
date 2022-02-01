package com.example.zappycode.sharedprefrences;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences sharedPreferences = this.getSharedPreferences("com.example.zappycode.sharedprefrences", Context.MODE_PRIVATE);

        /*
        ArrayList<String> friends = new ArrayList<>();

        friends.add("Fido");
        friends.add("Sarah");
        friends.add("Jones");

        try {
            sharedPreferences.edit().putString("friends",ObjectSerializer.serialize(friends)).apply();

            Log.i("friends",ObjectSerializer.serialize(friends));

        } catch (Exception e) {
            e.printStackTrace();
        }
        */

        ArrayList<String> newFriends = new ArrayList<>();

        try {
            newFriends = (ArrayList<String>) ObjectSerializer.deserialize(sharedPreferences.getString("friends", ObjectSerializer.serialize(new ArrayList<String>())));
        } catch (Exception e) {
            e.printStackTrace();
        }

        Log.i("new Friends",newFriends.toString());

        //sharedPreferences.edit().putString("username","nick").apply();

        //String username = sharedPreferences.getString("username","");

        //Log.i("This is the username",username);

    }
}
