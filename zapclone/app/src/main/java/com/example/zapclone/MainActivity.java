package com.example.zapclone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    TextView email;
    TextView password;
    Button button;

    public void verifyUser() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            Intent intent = new Intent(MainActivity.this, userFriendsActivity.class);
            startActivity(intent);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        email = findViewById(R.id.editTexEmail);
        password = findViewById(R.id.editTextPassword);
        button = findViewById(R.id.buttonSignin);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        verifyUser();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               try {

                    mAuth.signInWithEmailAndPassword(email.getText().toString().trim(), password.getText().toString().trim()).addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                FirebaseUser user = mAuth.getCurrentUser();
                                Toast.makeText(MainActivity.this, "Bem vindo, " + user.getEmail(), Toast.LENGTH_SHORT).show();
                               verifyUser();

                            } else {
                                mAuth.createUserWithEmailAndPassword(email.getText().toString().trim(), password.getText().toString().trim()).addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {

                                            mDatabase.child("users").child(mAuth.getUid()).child("email").setValue(email.getText().toString().trim());

                                            FirebaseUser user = mAuth.getCurrentUser();
                                            Toast.makeText(MainActivity.this, "Bem vindo, " + user.getEmail(), Toast.LENGTH_SHORT).show();
                                            verifyUser();

                                        } else {
                                            Log.w("TAG", "signInWithEmail:failed", task.getException());
                                            Toast.makeText(MainActivity.this, "E-mail / Senha inválidos", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }
                        }


                    });

               } catch(Exception e){
                   e.printStackTrace();
                   Toast.makeText(MainActivity.this, "Insira e-mail E senha válidos", Toast.LENGTH_SHORT).show();
               }

            }
        });
    }
}