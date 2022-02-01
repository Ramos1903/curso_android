package com.santtytech.directfirst;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.os.Handler;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity implements Runnable {

    private EditText mEditEmail;
    private EditText mEditSenha;
    private Button mBtnEntra;
    private TextView mLblCadastro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );

        Handler handler = new Handler();
        handler.postDelayed( this, 2000 );

    }

    @Override
    public void run() {
        mEditEmail = findViewById( R.id.textEmail );
        mEditSenha = findViewById( R.id.textPassword );
        mBtnEntra = findViewById( R.id.btnSend );
        mLblCadastro = findViewById( R.id.lblCadastre );


        mBtnEntra.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = mEditEmail.getText().toString();
                String password = mEditSenha.getText().toString();

                Log.i( "Texto", email );
                Log.i( "Texto", password );

                if (email == null || email.isEmpty() || password == null || password.isEmpty()) {
                    Toast.makeText( LoginActivity.this, "Email e senha são campos obrigatórios!", Toast.LENGTH_SHORT ).show();
                    return;
                }

                FirebaseAuth.getInstance().signInWithEmailAndPassword( email, password )
                        .addOnCompleteListener( new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Log.i( "Sucesso", task.getResult().getUser().getUid() );
                                    Toast.makeText( LoginActivity.this, "Usuário logado com sucesso!", Toast.LENGTH_LONG ).show();
                                }

                                Intent intent = new Intent( LoginActivity.this, MessagesActivity.class );

                                intent.setFlags( Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK );

                                startActivity( intent );
                            }
                        } )
                        .addOnFailureListener( new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.i( "Falha ", e.getMessage() );
                                Toast.makeText( LoginActivity.this, "Usuário ou senha incorretos", Toast.LENGTH_LONG ).show();

                            }
                        } );
            }
        } );

        mLblCadastro.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent( LoginActivity.this, RegisterActivity.class );
                startActivity( intent );
                finish();
            }
        } );

    }
}

