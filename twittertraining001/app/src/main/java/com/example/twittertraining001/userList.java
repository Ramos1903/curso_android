package com.example.twittertraining001;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.core.UserData;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Objects;

public class userList extends AppCompatActivity {

    ListView listViewUsers;
    ArrayList<String> users = new ArrayList<>();
    ArrayAdapter arrayAdapter;
    // Access a Cloud Firestore instance from your Activity
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_options, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {


            switch (item.getItemId()) {
        case R.id.tweet:
            startActivity(new Intent(this, tweet.class));
            return true;
        case R.id.myFeed:
            startActivity(new Intent(this, myFeed.class));
            return true;
        case R.id.logout:

            FirebaseAuth.getInstance().signOut();
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (user != null){
                Toast.makeText(this, "Falha ao deslogar", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Deslogado com sucesso!", Toast.LENGTH_SHORT).show();
                userList.this.finish();
            }

            return true;
        default:
            return super.onOptionsItemSelected(item);
            }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.userlist_layout);




        listViewUsers = findViewById(R.id.listView);

        listViewUsers.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);

        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_checked, users);

        listViewUsers.setAdapter(arrayAdapter);

        listViewUsers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CheckedTextView checkedTextView = (CheckedTextView) view;
                if(checkedTextView.isChecked()){
                    Log.i("Info", "CHEKED!!");
                } else {
                    Log.i("INFO", "NOT CHEKED!!!!");
                }

            }

        });


        try {
            db.collection("users")
                    .whereEqualTo("user", true)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                                    Log.i("document", String.valueOf(document.getData()));
                                  

                                }
                            } else {
                                Log.d("TAGGER", "Error getting documents: ", task.getException());
                            }
                        }
                    });
        }catch (Exception e){
            e.printStackTrace();
        }


    }
}
