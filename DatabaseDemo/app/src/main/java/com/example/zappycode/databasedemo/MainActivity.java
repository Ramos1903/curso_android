package com.example.zappycode.databasedemo;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {

            SQLiteDatabase sqLiteDatabase = this.openOrCreateDatabase("Users", MODE_PRIVATE, null);
            sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS theNewUsers (name VARCHAR, age INT(3), id INTEGER PRIMARY KEY)");
            sqLiteDatabase.execSQL("INSERT INTO theNewUsers (name, age) VALUES ('Nick',23)");
            sqLiteDatabase.execSQL("INSERT INTO theNewUsers (name, age) VALUES ('Nick',43)");
            sqLiteDatabase.execSQL("INSERT INTO theNewUsers (name, age) VALUES ('Dave',14)");

            sqLiteDatabase.execSQL("DELETE FROM theNewUsers WHERE id = 2");

            Cursor c = sqLiteDatabase.rawQuery("SELECT * FROM theNewUsers", null);
            int nameIndex = c.getColumnIndex("name");
            int ageIndex = c.getColumnIndex("age");
            int idIndex = c.getColumnIndex("id");
            c.moveToFirst();

            while (c != null) {
                Log.i("UserResults - name", c.getString(nameIndex));
                Log.i("userResults - age", Integer.toString(c.getInt(ageIndex)));
                Log.i("userResults - id", Integer.toString(c.getInt(idIndex)));

                c.moveToNext();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
