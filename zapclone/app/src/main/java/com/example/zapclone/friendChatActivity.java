package com.example.zapclone;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class friendChatActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    ListView messagesListview;
    ArrayList<String> messages = new ArrayList<>();

    EditText messagesSend;
    Button buttonSend;

    String activeUser = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_chat);

        messagesListview = findViewById(R.id.listViewMessages);
        messagesSend = findViewById(R.id.editTextWriteSend);
        buttonSend = findViewById(R.id.buttonSend);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        ArrayAdapter messagesAdapter = new ArrayAdapter(friendChatActivity.this, android.R.layout.simple_list_item_1, messages);
        messagesListview.setAdapter(messagesAdapter);
        messages.add("TEXT");

        Intent intent = getIntent();

        activeUser = intent.getStringExtra("email");

        setTitle("Chat with " + activeUser);

        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mAuth = FirebaseAuth.getInstance();
                mDatabase = FirebaseDatabase.getInstance().getReference();

                if (messagesSend != null) {
                    buttonSend.setEnabled(true);

                    mDatabase.child("users").child(mAuth.getUid()).child("message").push().child("sender").setValue(mAuth.getCurrentUser().getEmail());
                    mDatabase.child("users").child(mAuth.getUid()).child("message").push().child("recipient").setValue(activeUser);
                    mDatabase.child("users").child(mAuth.getUid()).child("message").push().child("messageContent").setValue(messagesSend.getText().toString().trim());

                    mDatabase.child("users").child(mAuth.getUid()).child("message").addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                            for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                                messages.add(postSnapshot.getValue().toString());
                            }
                            messagesAdapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                        }

                        @Override
                        public void onChildRemoved(@NonNull DataSnapshot snapshot) {

                        }

                        @Override
                        public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });


                } else {
                    buttonSend.setEnabled(false);
                }



                messagesSend.setText("");
            }
        });


    }
}