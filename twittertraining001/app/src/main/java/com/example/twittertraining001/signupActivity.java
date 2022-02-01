package com.example.twittertraining001;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

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

public class signupActivity extends AppCompatActivity {

    // Access a Cloud Firestore instance from your Activity
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    private FirebaseAuth mAuth; // recupera a instância do Firebase com var global
    private FirebaseAuth.AuthStateListener mAuthListener; // listener para acompanhar as mudanças de auth

    EditText registerEmailUser;
    EditText registerPasswordUser;
    Button signUpBtn;
    Button signIntentBtn;
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

    private void addData() {
        // Create a new user with a first and last name
        Map<String, Object> user = new HashMap<>();
        user.put("user", Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getEmail());
        //user.put("first", "Ada");
        //user.put("last", "Lovelace");
        //user.put("born", 1815);

// Add a new document with a generated ID
        db.collection("users")
                .add(user)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d("TAG", "DocumentSnapshot added with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("TAG", "Error adding document", e);
                    }
                });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_registeractivity);

        registerEmailUser = findViewById(R.id.editTextTextRegisterEmailAddress);
        registerPasswordUser = findViewById(R.id.editTextRegisterNumberPassword);
        signUpBtn = findViewById(R.id.signUpBtnlayout);
        signIntentBtn = findViewById(R.id.signIntentBtn);
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

        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    mAuth.createUserWithEmailAndPassword(registerEmailUser.getText().toString().trim(), registerPasswordUser.getText().toString().trim()).addOnCompleteListener(signupActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (!task.isSuccessful()) {
                                //Log.w("AUTH", "Falha ao efetuar o Login: ", task.getException());
                                Toast.makeText(signupActivity.this, "Falha ao efetuar o cadastro", Toast.LENGTH_SHORT).show();
                            } else {
                                // Log.d("AUTH", "Register Efetuado com sucesso!!!");
                                addData();
                                Toast.makeText(signupActivity.this, "Registrado com sucesso!!", Toast.LENGTH_SHORT).show();
                                signupActivity.this.finish();
                            }

                        }
                    });

                } catch (Exception e){
                    e.printStackTrace();
                    Toast.makeText(signupActivity.this, "Your E-mail/password is empty", Toast.LENGTH_SHORT).show();
                }
            }
        });

        signIntentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signupActivity.this.finish();
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
