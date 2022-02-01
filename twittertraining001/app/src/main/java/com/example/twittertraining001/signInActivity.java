package com.example.twittertraining001;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class signInActivity extends AppCompatActivity {

    // Access a Cloud Firestore instance from your Activity
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    private FirebaseAuth mAuth; // recupera a instância do Firebase com var global
    private FirebaseAuth.AuthStateListener mAuthListener; // listener para acompanhar as mudanças de auth

    EditText emailUser;
    EditText passwordUser;
    Button signInEmailBtn;
    Button loginGoogleBtn;
    Button signupIntentBtn;
    Intent signUpIntent;
    Intent userFeedIntent;
    ConstraintLayout background;

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signin_loginactivity);

        emailUser = findViewById(R.id.editTextTextEmailAddress);
        passwordUser = findViewById(R.id.editTextNumberPassword);
        signInEmailBtn = findViewById(R.id.signInEmail); //Sign In é quando já possui conta
        loginGoogleBtn = findViewById(R.id.signInGoogle);
        signupIntentBtn = findViewById(R.id.signupIntentBtn);
        background = findViewById(R.id.backG);

        mAuth = FirebaseAuth.getInstance(); // recupera a instância

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null){
                    Log.d("AUTH", "onAuthStateChanged:signed_in: " + user.getUid());
                } else {
                    Log.d("AUTH", "onAuthStateChanged:signed_out: ");
                }
            }
        };


        signInEmailBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    try {
                        mAuth.signInWithEmailAndPassword(emailUser.getText().toString().trim(), passwordUser.getText().toString().trim()).addOnCompleteListener(signInActivity.this, new OnCompleteListener<AuthResult>() {
                            //Insted of this use ActivityName.this.
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (!task.isSuccessful()) {
                                    // Log.w("AUTH", "Falha ao efetuar o Login: ", task.getException());
                                    Toast.makeText(signInActivity.this, "Falha ao efetuar o Login", Toast.LENGTH_SHORT).show();
                                } else {
                                    //Log.d("AUTH", "Login Efetuado com sucesso!!!");
                                    Toast.makeText(signInActivity.this, "Login Efetuado com sucesso!!!", Toast.LENGTH_SHORT).show();
                                    userFeedIntent = new Intent(signInActivity.this, userList.class);
                                    startActivity(userFeedIntent);
                                }

                            }
                        });
                    }catch(Exception e){
                        e.printStackTrace();
                        Toast.makeText(signInActivity.this, "Your E-mail/password is empty", Toast.LENGTH_SHORT).show();
                    }
            }
        });

        signupIntentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUpIntent = new Intent(signInActivity.this, signupActivity.class);
                startActivity(signUpIntent);
            }
        });

        background.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        });
    }

}